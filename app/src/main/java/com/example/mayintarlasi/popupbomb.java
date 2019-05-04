package com.example.mayintarlasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class popupbomb extends AppCompatActivity {

    ImageButton btn_yeni;
    TextView txt_skor;

    public static String sure = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupbomb);

        btn_yeni = findViewById(R.id.btn_yeni);
        txt_skor = findViewById(R.id.txt_skor);
        txt_skor.setText(sure);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.3));

        btn_yeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(popupbomb.this, Mine.class));

            }
        });



    }

    @Override
    public void onBackPressed() {

    }

}
