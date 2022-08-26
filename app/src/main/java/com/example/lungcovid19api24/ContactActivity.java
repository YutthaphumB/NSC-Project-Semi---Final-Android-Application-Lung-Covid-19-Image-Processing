package com.example.lungcovid19api24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {

    TextView txtHead, txtHead2, txtName, txtHead3, txtName2, txtAdd;
    String head[] = {"GENius-Aided System (GENA)\nfor National Software Contest\n24th time.", "โครงการแข่งขันพัฒนาโปรแกรม\nคอมพิวเตอร์แห่งประเทศไทย\nครั้งที่ 24"};
    String head2[] = {"Developer", "ผู้พัฒนา"};
    String head3[] = {"Advisor", "อาจารย์ที่ปรึกษา"};
    String name[] = {"Mr.Yutthaphum Boonmak\nMr.Kajonwit Seesunton\nMs.Sarinya Pakdee", "นายยุทภูมิ บุญมาก\nนายขจรวิทย์ ศรีสุนทร\nนางสาวศรินยา ภักดี"};
    String name2[] = {"Assistant Professor Rujijan Vichivanives", "ผู้ช่วยศาสตราจารย์รุจิจันทร์ วิชิวานิเวศน์"};
    String add[] = {"SUAN SUNANDHA RAJABHAT UNIVERSITY.\n1 U-Thong nok Road\n Dusit, Bangkok 10300\nE-mail : s62122201008@ssru.ac.th", "มหาวิทยาลัยราชภัฏสวนสุนันทา\nเลขที่ 1 ถนนอู่ทองนอก\nเขตดุสิต กรุงเทพมหานคร 10300\nอีเมล : s62122201008@ssru.ac.th"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();

        txtHead = findViewById(R.id.textHeadCo);
        txtHead2 = findViewById(R.id.textHead2);
        txtHead3 = findViewById(R.id.textHead3);
        txtName = findViewById(R.id.textName);
        txtName2 = findViewById(R.id.textName2);
        txtAdd = findViewById(R.id.textAdd);

        txtHead.setText(head[GlobalVariable.language]);
        txtHead2.setText(head2[GlobalVariable.language]);
        txtHead3.setText(head3[GlobalVariable.language]);
        txtName.setText(name[GlobalVariable.language]);
        txtName2.setText(name2[GlobalVariable.language]);
        txtAdd.setText(add[GlobalVariable.language]);


    }
}