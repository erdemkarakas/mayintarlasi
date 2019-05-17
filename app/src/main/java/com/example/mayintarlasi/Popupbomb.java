package com.example.mayintarlasi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Popupbomb extends AppCompatActivity {

    Button btn_records;
    ImageButton btn_yeni;
    TextView txt_skor;
    TextView txt_rekor;
    TextView txt_yapılanrekor;

    Mine m = new Mine();

    public static String sure = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupbomb);

        btn_yeni = findViewById(R.id.btn_yeni);
        txt_skor = findViewById(R.id.txt_skor);
        txt_skor.setText(sure);
        txt_rekor = findViewById(R.id.txt_rekor);
        txt_yapılanrekor = findViewById(R.id.txt_yapılanrekor);
        btn_records = findViewById(R.id.btn_records);

        GameEngine ga = new GameEngine();

        txt_yapılanrekor.setText(ga.sure);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.3));

        getrecord();

        btn_yeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Popupbomb.this, Mine.class));

            }
        });

    btn_records.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(), List.class);
        startActivity(i);
    }
});

    }

    private void getrecord() {
        HttpAsyncTask listAllAnno = new HttpAsyncTask();

        listAllAnno.execute("http://erdemkarakas.somee.com/Api/Values/recordshowme");
    }

    public static String POST(String url, JSONObject jsonObject) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json,"UTF-8");
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            JSONObject jsonObject = new JSONObject();


            try {

                jsonObject.put("token", "a153dd6s33xv6uy9hgf23b16gh")
                            .put("id", m.kullanici_id);

                Log.d("JsonAllOrdersYeni", jsonObject.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return POST(urls[0], jsonObject);
        }

        @Override
        protected void onPostExecute(String result) {


//                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            Log.d("JsonResultAllOrderYeni", result);


            txt_rekor.setText(result);

        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


    @Override
    public void onBackPressed() {

    }

}
