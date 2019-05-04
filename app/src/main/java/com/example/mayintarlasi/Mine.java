package com.example.mayintarlasi;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mine extends AppCompatActivity {


    public static Chronometer ch;
    ImageButton btn_baslat;

    public static String kullanici_id;


    public static com.example.mayintarlasi.views.grid.Grid mayintarlasiGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ch = (Chronometer) findViewById(R.id.skor);
        ch.stop();
        long elapsedMillis = SystemClock.elapsedRealtime() - ch.getBase();
        btn_baslat = findViewById(R.id.btn_baslat);
        mayintarlasiGridView = findViewById(R.id.mayintarlasiGridView);

        mayintarlasiGridView.setVisibility(View.INVISIBLE);



        Log.e("MainActivity","onCreate");

        btn_baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GameEngine.getInstance().createGrid(Mine.this);
                ch.stop();
                long elapsedMillis = SystemClock.elapsedRealtime() - ch.getBase();
                ch.setBase(SystemClock.elapsedRealtime());

                ch.start();
                mayintarlasiGridView.setVisibility(View.VISIBLE);




            }
        });




//        Toast.makeText(this, dateFormat.format(date).toString().trim(), Toast.LENGTH_SHORT).show();
    }
}
