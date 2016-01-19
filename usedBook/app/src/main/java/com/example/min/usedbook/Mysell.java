package com.example.min.usedbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class Mysell extends AppCompatActivity {

    Button btn_home;
    Button btn_myinfo;
    Button btn_enroll_sell;
    Button btn_enroll_buy;
    Button btn_buy;

    ListView listView;

    MyListAdapter adapter;

    ArrayList<MyItem> sellList = new ArrayList<MyItem>();

    String encodedString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysell);

        btn_home        =(Button)findViewById(R.id.btn_home);
        btn_myinfo        =(Button)findViewById(R.id.btn_myinfo);
        btn_enroll_sell        =(Button)findViewById(R.id.btn_enroll_sell);
        btn_enroll_buy        =(Button)findViewById(R.id.btn_enroll_buy);
        btn_buy        =(Button)findViewById(R.id.btn_buy);

        listView =(ListView)findViewById(R.id.listView);
        listView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        asyncTaskCall();

        buttonClickListener();

    }


    private void buttonClickListener() {
        btn_home.setOnClickListener(ClickListener);
        btn_myinfo.setOnClickListener(ClickListener);
        btn_enroll_sell.setOnClickListener(ClickListener);
        btn_enroll_buy.setOnClickListener(ClickListener);
        btn_buy.setOnClickListener(ClickListener);
    }




    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_home:
                    Intent intent1 = new Intent(getApplicationContext(), Main.class);
                    startActivity(intent1);
                    break;

                case R.id.btn_myinfo:
                    Intent intent2 = new Intent(getApplicationContext(), MyInfo.class);
                    startActivity(intent2);
                    break;

                case R.id.btn_enroll_sell:
                    Intent intent3 = new Intent(getApplicationContext(), ChoiceSell.class);
                    startActivity(intent3);
                    break;

                case R.id.btn_enroll_buy:
                    Intent intent4 = new Intent(getApplicationContext(), ChoiceBuy.class);
                    startActivity(intent4);
                    break;

                case R.id.btn_buy:
                    Intent intent5 = new Intent(getApplicationContext(), BuyBoard.class);
                    startActivity(intent5);
                    break;
            }

        }
    };




    public class MyItem{
        Bitmap image=null;
        String title="";
        String publisher="";
        String price="";
        String day="";
        int index=0;
        int finish;

        public MyItem(Bitmap image, String title,String publisher,String price, String day, int index, int finish){
            super();
            this.image=image;
            this.title=title;
            this.publisher=publisher;
            this.price=price;
            this.day=day;
            this.index=index;
            this.finish=finish;
        }
    }

    public class MyListAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<MyItem> myItems;
        int layout;

        public MyListAdapter(Context context, ArrayList<MyItem> myItems, int layout){
            this.context=context;
            this.myItems=myItems;
            this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.layout=layout;
        }

        @Override
        public int getCount() {
            return myItems.size();
        }

        @Override
        public Object getItem(int position) {return position;}

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos =position;
            if(convertView == null){
                convertView = inflater.inflate(layout,parent,false);
            }

            final ImageView imageView=(ImageView)convertView.findViewById(R.id.image);
            imageView.setImageBitmap(myItems.get(pos).image);

            TextView title=(TextView)convertView.findViewById(R.id.txt_title);
            title.setText(myItems.get(pos).title);

            TextView publisher=(TextView)convertView.findViewById(R.id.txt_publisher);
            publisher.setText(myItems.get(pos).publisher);

            TextView price=(TextView)convertView.findViewById(R.id.txt_price);
            price.setText(myItems.get(pos).price);

            TextView day=(TextView)convertView.findViewById(R.id.txt_day);
            day.setText(myItems.get(pos).day);

            final Button btn_finish = (Button)convertView.findViewById(R.id.btn_finish);

            Log.i("aaaa", String.valueOf(myItems.get(pos).finish));

            if(0 == myItems.get(pos).finish){
                btn_finish.setClickable(false);
                btn_finish.setText("완료");
                btn_finish.setBackgroundColor(Color.parseColor("#8C8C8C"));
            }else{
                btn_finish.setClickable(true);
                btn_finish.setText("클릭");
                btn_finish.setBackgroundColor(Color.parseColor("#71bf44"));
            }


            btn_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (1 == myItems.get(pos).finish) {
                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mysell.this);
                        alert_confirm.setMessage("완료하시겠습니까?").setCancelable(true).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FinishasyncTaskCall(String.valueOf(myItems.get(pos).index));
                                        btn_finish.setText("완료");
                                        btn_finish.setBackgroundColor(Color.parseColor("#8C8C8C"));
                                        btn_finish.setClickable(false);
                                    }
                                }).setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 'No'
                                        return;
                                    }
                                });
                        AlertDialog alert = alert_confirm.create();
                        alert.show();

                    }
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(), "selected Item = " + myItems.get(pos).index, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MySellBoardInform.class);
                    intent.putExtra("index", myItems.get(pos).index);
                    startActivity(intent);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mysell.this);
                    alert_confirm.setMessage("골라골라?").setCancelable(true).setPositiveButton("수정",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent2 = new Intent(getApplicationContext(), SellUpdateBook.class);
                                    intent2.putExtra("index", myItems.get(pos).index);
                                    startActivity(intent2);

                                }
                            }).setNegativeButton("삭제",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mysell.this);
                                    alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(true).setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(getApplicationContext(), "등록물품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                    DeleteasyncTaskCall(String.valueOf(myItems.get(pos).index));
                                                    myItems.remove(pos);
                                                    listView.clearChoices();
                                                    adapter.notifyDataSetChanged();
                                                   // table_my_buy.removeView(row);
                                                }
                                            }).setNegativeButton("취소",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // 'No'
                                                    return;
                                                }
                                            });
                                    AlertDialog alert = alert_confirm.create();
                                    alert.show();
                                    // 'No'
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();

                    return false;
                }
            });

            return convertView;
        }
    }

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

            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
            HttpURLConnection urlConnection=null;
            URL url =null;
            DataOutputStream out=null;
            BufferedInputStream buf=null;
            BufferedReader bufreader=null;

            Properties prop = new Properties();
            prop.setProperty("id",sharedPreferences.getString("ID", "" ));
            // prop.setProperty("tempStandard",tempStandard);

            encodedString = encodeString(prop);

            try{

                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/mySell.jsp");
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
        protected void onPostExecute(Bitmap result){  //Thread 이후 UI 처리 result는 Thread의 리턴값!!!
            // bit=result;
        }
    }


    private void jsonParserList(String recv) {

        Log.i("서버에서 받은 전체 내용 : ", recv);

        try{
            JSONObject json=new JSONObject(recv);
            JSONArray jArr =json.getJSONArray("List");

            for(int i=0; i<jArr.length();i++){
                json=jArr.getJSONObject(i);

                Log.i("bbb", String.valueOf(i));
                sellList.add(new MyItem(imageAsyncTaskCall(json.getString("photo")), json.getString("title"), json.getString("publisher"), json.getString("price") + "원", json.getString("day"), json.getInt("index"), json.getInt("finish")));
            }
            adapter = new MyListAdapter(this,sellList,R.layout.mysell_list);
            listView.setAdapter(adapter);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    public void FinishasyncTaskCall(String index){
        new FinishAsyncTask().execute(index);
    }

    public class FinishAsyncTask extends AsyncTask<String,Integer,String> {

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

                url=new URL("http://" + ip + ":8080/usedBook/finishSell.jsp");
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
          //  jsonParserList(result);

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
        protected void onPostExecute(String result){  //Thread 이후 UI 처리 result는 Thread의 리턴값!!!
           // jsonParserList(result);

        }
    }

    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }
}
