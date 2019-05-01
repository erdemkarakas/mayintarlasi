package com.example.mayintarlasi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Mine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        ((TextView)findViewById(R.id.textView)).setText("ASDASDASDASD");

        Log.e("MainActivity","onCreate");
        GameEngine.getInstance().createGrid(this);
    }
}
