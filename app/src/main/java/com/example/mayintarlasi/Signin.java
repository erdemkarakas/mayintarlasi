package com.example.mayintarlasi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Signin extends AppCompatActivity {

    Button btn_giris,btn_kayit;
    TextInputLayout etl_name,etl_pass;


    Validation validationCheck = new Validation();


    Mine m = new Mine();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.MyMaterialTheme);
        setContentView(R.layout.activity_signin);

        btn_giris = findViewById(R.id.btn_sign);
        etl_name = findViewById(R.id.etl_name);
        etl_pass = findViewById(R.id.etl_pass);
        btn_kayit = findViewById(R.id.btn_kayit);

        btn_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();


            }
        });

        btn_kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Kayıt();
            }
        });



    }




    public void signin() {

        int errorCount=1;


        if (!validationCheck.isUserNameCorrect(etl_name.getEditText().getText().toString().trim()))
        {
            errorCount=0;
            etl_name.setError("Gecersiz Kullanıcı Adı");
        }


        if (validationCheck.isValidPass(etl_pass.getEditText().getText().toString().trim()))
        {
            errorCount=0;
            etl_pass.setError("Geçersiz Şifre");
        }

        if(errorCount==1) {


                HttpAsyncTask LogIn = new HttpAsyncTask();
                /* Buraya webb api linki gelecek*/
                LogIn.execute("http://erdemkarakas.somee.com/Api/Values/UserCheck");


        }

    }


    public void Kayıt() {

        Intent i = new Intent(Signin.this, Signup.class);
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
                        .put("name", etl_name.getEditText().getText().toString().trim())
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

            if(result.contains("yanlıs"))
            {
                Toast.makeText(Signin.this, "Şifrenizi Doğru Girdiğinizden Emin olun", Toast.LENGTH_SHORT).show();
                Log.d("burada", "burada");

            }
            else {

                try {

//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    Log.d("JsonResponseMain", result);

                    try {

                        JSONObject jsonObject = new JSONObject(new String(result.getBytes("ISO-8859-1"), "UTF-8"));
                        m.kullanici_id = jsonObject.getString("user_id");
                        m.user_type = jsonObject.getString("user_type");

                        Toast.makeText(Signin.this, "Giriş yaptınız", Toast.LENGTH_SHORT).show();

                        /* buraya mayın tarlası ana sınıfı gelecek*/
                            Intent i = new Intent(getApplicationContext(), Mine.class);
                            startActivity(i);




                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("burada","burada2");

                        Toast.makeText(Signin.this, result, Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

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


