package com.example.lungcovid19api24;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.lungcovid19api24.ml.Covidmodel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ThaiActivity extends AppCompatActivity {

    ImageView imageView;
    TextView txtHead;
    Button picture, selectIMG;
    int imageSize = 224;
    int progressInt = 0;
    String result = "";
    Dialog dialog;
    float[] confidences;
    String head[] = {"Lung X-RAY film scan", "สแกนฟิล์ม X-RAY ปอด"};
    String select[] = {"upload image", "อัปโหลดภาพ"};
    String pic[] = {"scan image", "สแกนภาพ"};
    String ok[] = {"OK", "ตกลง"};
    String unkAl[] = {"Please take a picture or upload a new one.", "กรุณาถ่ายหรืออัพโหลดรูปใหม่"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thai);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.buttonPhoto);
        selectIMG = findViewById(R.id.buttonUpload);
        txtHead = findViewById(R.id.textHead);
        dialog = new Dialog(this);

        txtHead.setText(head[GlobalVariable.language]);
        selectIMG.setText(select[GlobalVariable.language]);
        picture.setText(pic[GlobalVariable.language]);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        selectIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
    }

    public void classifyImage(Bitmap image){
        try {
            Covidmodel model = Covidmodel.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer  byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Covidmodel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[][] classes = {{"Infected Lung", "Unknown", "Normal lung"}, {"เป็นโควิด 19", "ไม่รู้จัก", "ปอดปกติ"}};
            result = classes[GlobalVariable.language][maxPos];
            if (result == "เป็นโควิด 19" || result == "Infected Lung"){
                openCovidDialog();
            }else if (result == "ปอดปกติ" || result == "Normal lung"){
                openNormalDialog();
            }else {
                openUnknownDialog();
            }

            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            //confidence.setText(s);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }

    private void openUnknownDialog() {
        dialog.setContentView(R.layout.unknown_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnOK = dialog.findViewById(R.id.buttonOK);
        dialog.show();
        TextView txtresult = dialog.findViewById(R.id.textresult);
        TextView txtconfidence = dialog.findViewById(R.id.textconfidence);
        txtresult.setText(result);
        txtconfidence.setText(unkAl[GlobalVariable.language]);
        btnOK.setText(ok[GlobalVariable.language]);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openNormalDialog() {
        dialog.setContentView(R.layout.normal_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnOK = dialog.findViewById(R.id.buttonOK);
        dialog.show();
        TextView txtresult = dialog.findViewById(R.id.textresult);
        TextView txtconfidence = dialog.findViewById(R.id.textconfidence);
        txtresult.setText(result);
        txtconfidence.setText(Float.toString(confidences[2] * 100) + " %" );
        btnOK.setText(ok[GlobalVariable.language]);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openCovidDialog() {
        dialog.setContentView(R.layout.covid_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnOK = dialog.findViewById(R.id.buttonOK);
        dialog.show();
        TextView txtresult = dialog.findViewById(R.id.textresult);
        TextView txtconfidence = dialog.findViewById(R.id.textconfidence);
        txtresult.setText(result);
        txtconfidence.setText(Float.toString(confidences[0] * 100) + " %" );
        btnOK.setText(ok[GlobalVariable.language]);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);

        }else if(requestCode == 2) {
            imageView.setImageURI(data.getData());
            Bitmap image;
            Uri uri = data.getData();
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}