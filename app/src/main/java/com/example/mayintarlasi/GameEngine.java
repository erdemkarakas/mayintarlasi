package com.example.mayintarlasi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.mayintarlasi.util.Generator;
import com.example.mayintarlasi.util.PrintGrid;
import com.example.mayintarlasi.views.grid.Cell;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class GameEngine {

    private static GameEngine instance;

    public static final int BOMB_NUMBER = 10;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 16;

    Mine m = new Mine();


    private Context context;

    public static String sure;

    private Cell[][] MinesweeperGrid = new Cell[WIDTH][HEIGHT];

    public static GameEngine getInstance() {
        if( instance == null ){
            instance = new GameEngine();
        }
        return instance;
    }

    public GameEngine(){ }

    public void createGrid(Context context){
        Log.e("GameEngine","createGrid is working");
        this.context = context;


        int[][] GeneratedGrid = Generator.generate(BOMB_NUMBER,WIDTH, HEIGHT);
        PrintGrid.print(GeneratedGrid,WIDTH,HEIGHT);
        setGrid(context,GeneratedGrid);
    }

    private void setGrid( final Context context, final int[][] grid ){
        for( int x = 0 ; x < WIDTH ; x++ ){
            for( int y = 0 ; y < HEIGHT ; y++ ){
                if( MinesweeperGrid[x][y] == null ){
                    MinesweeperGrid[x][y] = new Cell( context , x,y);
                }
                MinesweeperGrid[x][y].setValue(grid[x][y]);
                MinesweeperGrid[x][y].invalidate();
            }
        }
    }

    public Cell getCellAt(int position) {
        int x = position % WIDTH;
        int y = position / WIDTH;

        return MinesweeperGrid[x][y];
    }

    public Cell getCellAt( int x , int y ){
        return MinesweeperGrid[x][y];
    }

    public void click( int x , int y ){
        if( x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT && !getCellAt(x,y).isClicked() ){
            getCellAt(x,y).setClicked();

            if( getCellAt(x,y).getValue() == 0 ){
                for( int xt = -1 ; xt <= 1 ; xt++ ){
                    for( int yt = -1 ; yt <= 1 ; yt++){
                        if( xt != yt ){
                            click(x + xt , y + yt);
                        }
                    }
                }
            }

            if( getCellAt(x,y).isBomb() ){
                onGameLost();
            }
        }

        checkEnd();
    }




    private boolean checkEnd(){
        int bombNotFound = BOMB_NUMBER;
        int notRevealed = WIDTH * HEIGHT;
        for ( int x = 0 ; x < WIDTH ; x++ ){
            for( int y = 0 ; y < HEIGHT ; y++ ){
                if( getCellAt(x,y).isRevealed() || getCellAt(x,y).isFlagged() ){
                    notRevealed--;
                }

                if( getCellAt(x,y).isFlagged() && getCellAt(x,y).isBomb() ){
                    bombNotFound--;
                }
            }
        }

        if( bombNotFound == 0 && notRevealed == 0 ){
            Toast.makeText(context,"Game won", Toast.LENGTH_SHORT).show();


            m.ch.stop();
            long elapsedMillis = SystemClock.elapsedRealtime() - m.ch.getBase();


            sure = String.format("%d",
                    TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)*60+TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)));

            m.ch.setBase(SystemClock.elapsedRealtime());


            recordsave();



        }
        return false;
    }

    private void recordsave() {

        HttpAsyncTask LogIn = new HttpAsyncTask();
        /* buraya webb api linki gelecek*/
        LogIn.execute("http://erdemkarakas.somee.com/Api/Values/recordsave");



        context.startActivity(new Intent(context, Popupbomb.class));
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

            Date d = new Date();

            DateFormat dateFormat = new SimpleDateFormat("HH:mm");


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("token", "a153dd6s33xv6uy9hgf23b16gh")
                        .put("id", m.kullanici_id)
                        .put("sure", sure);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("recordpost",jsonObject.toString());
            return POST(urls[0], jsonObject);

        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Log.d("recordresult", result);

            if(result.contains("Hata"))
            {
                Toast.makeText(context, "Güvenlik", Toast.LENGTH_SHORT).show();
                Log.d("burada", "burada");

            }
            else {

                try {

//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                    Log.d("recordresult", result);

                    if(result.contains("true")) {
                        Toast.makeText(context, "Rekor Kayıt Edildi", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            Toast.makeText(context, "Hata", Toast.LENGTH_SHORT).show();
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

    public void flag( int x , int y ){
        boolean isFlagged = getCellAt(x,y).isFlagged();
        getCellAt(x,y).setFlagged(!isFlagged);
        getCellAt(x,y).invalidate();
    }

    private void onGameLost(){
        // handle lost game
        Toast.makeText(context,"Game lost", Toast.LENGTH_SHORT).show();

        for ( int x = 0 ; x < WIDTH ; x++ ) {
            for (int y = 0; y < HEIGHT; y++) {
                getCellAt(x,y).setRevealed();
            }
        }
        Mine m = new Mine();

        m.ch.stop();
        long elapsedMillis = SystemClock.elapsedRealtime() - m.ch.getBase();


        sure = String.format("%d sn",
                TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)*60+TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)));

        m.ch.setBase(SystemClock.elapsedRealtime());


        Popupbomb pb= new Popupbomb();
        pb.sure = sure;

        context.startActivity(new Intent(context, Popupbomb.class));





    }
}