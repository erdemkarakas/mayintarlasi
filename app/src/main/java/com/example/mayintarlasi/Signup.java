package com.example.mayintarlasi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mayintarlasi.Helper.Validation;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Signup extends AppCompatActivity {

    Button btn_kayıt;
    TextInputLayout etl_name,etl_pass;
    TextView giris;

    Validation validationCheck = new Validation();

    MainActivity ma = new MainActivity();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_kayıt = findViewById(R.id.btn_sign);
        etl_name = findViewById(R.id.etl_name);
        etl_pass = findViewById(R.id.etl_pass);
        giris = findViewById(R.id.txt_giris);

        btn_kayıt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();


            }
        });

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Giris();
            }
        });
    }


    public void signin() {

        int errorCount=1;


        if (!validationCheck.isUserNameCorrect(etl_name.getEditText().getText().toString().trim()))
        {
            errorCount=0;
            etl_name.setError("Gecersiz kullanıcı adı");
        }


        if (validationCheck.isValidPass(etl_pass.getEditText().getText().toString().trim()))
        {
            errorCount=0;
            etl_pass.setError("Gecersiz Sifre");
        }

        if(errorCount==1) {


            HttpAsyncTask LogIn = new HttpAsyncTask();
            /* buraya webb api linki gelecek*/
            LogIn.execute("/api/values/UserAdd");


        }

    }

    public void Giris() {

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }


    public static String POST(String url, JSONObject jsonObject) {
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json, "UTF-8");
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
                        .put("kullanici", etl_name.getEditText().getText().toString().trim())
                        .put("pass", etl_pass.getEditText().getText().toString().trim());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("JsonUser",jsonObject.toString());
            return POST(urls[0], jsonObject);

        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.d("JsonResponseMain", result);

            if(result.contains("Hata"))
            {
                Toast.makeText(Signup.this, "Tc Kimlik numaranızı ve Sifrenizi Dogru Girdiğinizden Emin olun !!!", Toast.LENGTH_SHORT).show();
                Log.d("burada", "burada");

            }
            else {

                try {

//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    Log.d("JsonResponseMain", result);

                    try {

                        JSONObject jsonObject = new JSONObject(new String(result.getBytes("ISO-8859-1"), "UTF-8"));
                        ma.kullanici_id = jsonObject.getString("user_id");


                        Toast.makeText(Signup.this, "MAyın tarlasına Kayıt olusturuldu", Toast.LENGTH_SHORT).show();

                        /* buraya mayın tarlası ana sınıfı gelecek
                            Intent i = new Intent(getApplicationContext(), DesignerActivity.class);
                            startActivity(i);
                            */



                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("burada","burada2");

                        Toast.makeText(Signup.this, "Tc Kimlik numaranızı ve Sifrenizi Dogru Girdiğinizden Emin olun !!!", Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        //stopPGS();

                    }
                }
                catch (Exception ex)
                {
                    Log.d("burada","burada3");
                }

            }
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




}
