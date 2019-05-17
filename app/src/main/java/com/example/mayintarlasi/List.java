package com.example.mayintarlasi;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mayintarlasi.Helper.RecyclerItemClickListener;
import com.example.mayintarlasi.Helper.adapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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

public class List extends AppCompatActivity {

    RecyclerView listView;
    com.example.mayintarlasi.Helper.adapter adapter;
    public static String [] ids;
    public static int controlSwitcher = 0;

    public static String user ="";
    public static String r_id ="";
    public static String record ="";

    Mine m = new Mine();

//    public static int list_item_id=0;


    public static String [] od_2;
    public static String [] od_3;
    public static int list_item_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.list_orders);


        listView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, listView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                            if(m.user_type.contains("true")){

                            list_item_id = Integer.valueOf((ids[position]));

                            r_id = ids[position];
                            record = od_3[position];


                                RemoveHttpAsyncTask LogIn = new RemoveHttpAsyncTask();
                                /* Buraya webb api linki gelecek*/
                                LogIn.execute("http://erdemkarakas.somee.com/Api/Values/postRecordDelete");

                            Toast.makeText(getApplicationContext(), user+" idli kullanıcnın "+record+" değerli rekoru uzun tıklandı", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Bu işlem için yerkiniz yok", Toast.LENGTH_SHORT).show();
                        }



                    }
                })
        );

        ListOrders();

    }


    public void ilanlistele() {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setLayoutParams(params);
        listView.setHasFixedSize(true);

        adapter = new adapter(od_2,od_3);
        listView.setAdapter(adapter);

        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));



    }

    public void ListOrders() {
        controlSwitcher=1;
        HttpAsyncTask listAllAnno = new HttpAsyncTask();

        listAllAnno.execute("http://erdemkarakas.somee.com/Api/Values/recordshow");
    }

    public static String POST(String url, JSONObject jsonObject) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

//            HttpDelete httpDelete = new HttpDelete(url);

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

            JSONObject jsonObject=new JSONObject();

            if(controlSwitcher==1){
                try {

                    jsonObject.put("token","a153dd6s33xv6uy9hgf23b16gh");


                    Log.d("JsonAllOrdersYeni",jsonObject.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            return POST(urls[0], jsonObject);
        }
        @Override
        protected void onPostExecute(String result) {



            if (controlSwitcher == 1) {

//                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                Log.d("JsonResultAllOrderYeni",result);


                try {


                    JSONObject jsonObject = new JSONObject(new String(result.getBytes("ISO-8859-1"), "UTF-8"));


                    JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));

                    try {
                        String[] od2 = new String[jsonArray.length()];
                        String[] od3 = new String[jsonArray.length()];
                        String[] idler = new String[jsonArray.length()];


                        if (jsonArray.length() > 0) {
                            //String[] idler = new String[jsonArray.length()];


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                Log.d("JsonBurada",jsonObject1.toString());

                                od2[i] = jsonObject1.getString("name").toString();
                                od3[i] = jsonObject1.getString("record").toString();

//
                                idler[i] = jsonObject1.getString("id");

                            }
                            od_2 = od2;
                            od_3 = od3;

                            ilanlistele();
                            ids = idler;
                        } else {
                            Toast.makeText(List.this, "Yeni Sipariş Yok", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(List.this, "Siparis Bilgilerini Kontrol Edin", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(List.this, "Baglantınızı Kontrol Edin", Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
    }



    private class RemoveHttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            JSONObject jsonObject=new JSONObject();

            if(controlSwitcher==1){
                try {

                    jsonObject.put("token","a153dd6s33xv6uy9hgf23b16gh")
                            .put("record_id",r_id);


                    Log.d("JsonAllOrdersYeni",jsonObject.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            return POST(urls[0], jsonObject);
        }

        @Override
        protected void onPostExecute(String result) {



            if (controlSwitcher == 1) {

//                    Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                Log.d("JsonResultAllOrderYeni",result);

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


                //Burada hata verebilir
                Intent i = new Intent(List.this, List.class);
                startActivity(i);


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
