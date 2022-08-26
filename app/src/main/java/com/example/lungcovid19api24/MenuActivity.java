package com.example.lungcovid19api24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity{
    Button btnlang, btnana, btnupdate, btnabo;
    TextView txtVer;
    private SharedPreferences sharedPreferences;
    String head[] = {"COVID-19\nANALYZER", "วิเคราะห์ปอดติดเชื้อ\nโควิด 19"};
    String ana[] = {"Process Image", "ประมวลผลภาพ"};
    String use[] = {"Update", "อัพเดท"};
    String abo[] = {"contact us", "ติดต่อเรา"};
    String ver = "Version 1.0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        LoadInt();
        btnlang = findViewById(R.id.buttonlang);
        btnana = findViewById(R.id.buttonAna);
        btnupdate = findViewById(R.id.buttonUse);
        btnabo = findViewById(R.id.buttonAbout);
        txtVer = findViewById(R.id.textVersion);

        btnana.setText(ana[GlobalVariable.language]);
        btnupdate.setText(use[GlobalVariable.language]);
        btnabo.setText(abo[GlobalVariable.language]);

        txtVer.setText(ver);



        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/folders/1auIOk4Mp_WpyWWrH8QucKAqUHkb7R2Nn?usp=sharing"));
                startActivity(browserIntent);
            }
        });

        btnlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalVariable.language == 0){
                    GlobalVariable.language = 1;
                }else {
                    GlobalVariable.language = 0;
                }

                SaveInt("lang", GlobalVariable.language);
                Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();


            }
        });

        btnana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ThaiActivity.class);
                startActivity(intent);
            }
        });

        btnabo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });


    }
    public void SaveInt(String key, int value){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public void LoadInt(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        GlobalVariable.language = sharedPreferences.getInt("lang", 0);
    }
}