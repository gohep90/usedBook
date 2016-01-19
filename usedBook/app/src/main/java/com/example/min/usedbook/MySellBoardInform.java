package com.example.min.usedbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

public class MySellBoardInform extends AppCompatActivity {

    ImageView image;

    TextView info_day;
    TextView info_book;
    TextView info_author;
    TextView info_publisher;
    TextView info_cost;
    TextView info_state;
    TextView info_other;

    Button btn_update;
    Button btn_delete;

    int index=0;
    String encodedString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sell_board_inform);

        image =(ImageView)findViewById(R.id.info_image);

        info_day        =   (TextView)findViewById(R.id.info_day);
        info_book       =   (TextView)findViewById(R.id.info_book);
        info_author     =   (TextView)findViewById(R.id.info_author);
        info_publisher  =   (TextView)findViewById(R.id.info_publisher);
        info_cost       =   (TextView)findViewById(R.id.info_cost);
        info_state      =   (TextView)findViewById(R.id.info_state);
        info_other      =   (TextView)findViewById(R.id.info_other);

        btn_update      =   (Button)findViewById(R.id.btn_update);
        btn_delete      =   (Button)findViewById(R.id.btn_delete);

        Intent i = getIntent();
        index = i.getExtras().getInt("index");

        asyncTaskCall();

        buttonClickListener();

    }


    private void buttonClickListener() {
        btn_update.setOnClickListener(ClickListener);
        btn_delete.setOnClickListener(ClickListener);
    }


    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_update:
                    Intent intent1 = new Intent(getApplicationContext(), SellUpdateBook.class);
                    intent1.putExtra("index",index);
                    startActivity(intent1);
                    break;

                case R.id.btn_delete:
                    DeleteasyncTaskCall(String.valueOf(index));
                    Intent intent2 = new Intent(getApplicationContext(), MyInfo.class);
                    startActivity(intent2);
                    Toast.makeText(getApplicationContext(), "등록물품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    };


    public void asyncTaskCall(){
        new MyAsyncTask().execute();
    }

    public class MyAsyncTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {  // 통신을 위한 Thread
            String result =recvList();
            return result;
        }

        public String encodeString(Properties params) {  //한글 encoding??
            StringBuffer sb = new StringBuffer(256);
            Enumeration names = params.propertyNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = params.getProperty(name);
                sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value) );

                if (names.hasMoreElements()) sb.append("&");
            }
            return sb.toString();
        }

        private String recvList() { //데이터 보내고 받아오기!!

            HttpURLConnection urlConnection=null;
            URL url =null;
            DataOutputStream out=null;
            BufferedInputStream buf=null;
            BufferedReader bufreader=null;

            Properties prop = new Properties();
            prop.setProperty("index", String.valueOf(index));

            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/detailSell.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                out = new DataOutputStream(urlConnection.getOutputStream());

                out.writeBytes(encodedString);

                out.flush();    //서버로 버퍼의 내용 전송

                buf = new BufferedInputStream(urlConnection.getInputStream());
                bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));

                String line=null;
                String result="";

                while((line=bufreader.readLine())!=null){
                    result += line;
                }

                return result;

            }catch(Exception e){
                e.printStackTrace();
                return "";
            }finally{
                urlConnection.disconnect();  //URL 연결해제
            }
        }
        protected void onPostExecute(String result){  //Thread 이후 UI 처리 result는 Thread의 리턴값!!!
            jsonParserList(result);
        }
    }


    private void jsonParserList(String recv) {

        Log.i("서버에서 받은 전체 내용 : ", recv);

        try{
            JSONObject json=new JSONObject(recv);
            JSONArray jArr =json.getJSONArray("List");

            for(int i=0; i<jArr.length();i++){
                json=jArr.getJSONObject(i);

                info_day.setText(json.getString("info_day"));
                info_book.setText(json.getString("info_book"));
                info_author.setText(json.getString("info_author"));
                info_publisher.setText(json.getString("info_publisher"));
                info_cost.setText(json.getString("info_cost"));
                info_state.setText(json.getString("info_state"));
                info_other.setText(json.getString("info_other"));
                image.setImageBitmap(imageAsyncTaskCall(json.getString("photo")));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public Bitmap imageAsyncTaskCall(String image){
        try {
            return new ImageAsyncTask().execute(image).get();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public class ImageAsyncTask extends AsyncTask<String,Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... image) {  // 통신을 위한 Thread
            Bitmap bm=null;
            try {
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                URL url=new URL("http://" + ip + ":8080/usedBook/image/"+image[0]);
                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }
    }



    public void DeleteasyncTaskCall(String index){
        new DeleteAsyncTask().execute(index);
    }

    public class DeleteAsyncTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... index) {  // 통신을 위한 Thread
            String result =recvList(index[0]);
            return result;
        }

        public String encodeString(Properties params) {  //한글 encoding??
            StringBuffer sb = new StringBuffer(256);
            Enumeration names = params.propertyNames();

            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                String value = params.getProperty(name);
                sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value) );

                if (names.hasMoreElements()) sb.append("&");
            }
            return sb.toString();
        }

        private String recvList(String index) { //데이터 보내고 받아오기!!

            HttpURLConnection urlConnection=null;
            URL url =null;
            DataOutputStream out=null;
            BufferedInputStream buf=null;
            BufferedReader bufreader=null;

            Properties prop = new Properties();
            prop.setProperty("index",index);
            // prop.setProperty("tempStandard",tempStandard);

            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/deleteSell.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                out = new DataOutputStream(urlConnection.getOutputStream());

                out.writeBytes(encodedString);

                out.flush();    //서버로 버퍼의 내용 전송

                buf = new BufferedInputStream(urlConnection.getInputStream());
                bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));

                String line=null;
                String result="";

                while((line=bufreader.readLine())!=null){
                    result += line;
                }

                return result;

            }catch(Exception e){
                e.printStackTrace();
                return "";
            }finally{
                urlConnection.disconnect();  //URL 연결해제
            }
        }
    }



    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }




}
