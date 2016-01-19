package com.example.min.usedbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
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


public class SellBoardBook extends FragmentActivity {

    Button btn_home;
    Button btn_myinfo;
    Button btn_enroll_sell;
    Button btn_enroll_buy;
    Button btn_buy;

    ImageButton btn_search;

    EditText edt_search;


    TabHost tabhost;
    ListView listView;

    ArrayList<MyItem> fruit = new ArrayList<MyItem>();
    MyListAdapter adapter;

    Bitmap bit=null;

    String encodedString="";
    String myname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_board_book);

        btn_home        =(Button)findViewById(R.id.btn_home);
        btn_myinfo        =(Button)findViewById(R.id.btn_myinfo);
        btn_enroll_sell        =(Button)findViewById(R.id.btn_enroll_sell);
        btn_enroll_buy        =(Button)findViewById(R.id.btn_enroll_buy);
        btn_buy        =(Button)findViewById(R.id.btn_buy);

        btn_search  =(ImageButton)findViewById(R.id.btn_search);

        edt_search  =(EditText)findViewById(R.id.edt_search);



        listView =(ListView)findViewById(R.id.listView);
        listView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        tabhost = (TabHost)findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec spec;

        spec = tabhost.newTabSpec("tab1");
        spec .setIndicator("전공");
        spec.setContent(R.id.tab1);
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("tab2");
        spec .setIndicator("교양");
        spec.setContent(R.id.tab2);
        tabhost.addTab(spec);

        spec = tabhost.newTabSpec("tab3");
        spec .setIndicator("토익");
        spec.setContent(R.id.tab3);
        tabhost.addTab(spec);

//////////////////////////tab 높이설정//////////////////////////////////////////////
        for(int i=0;i<tabhost.getTabWidget().getChildCount();++i){
            tabhost.getTabWidget().getChildAt(i).getLayoutParams().height=100;
        }

        tabhost.setCurrentTab(0);

        // fruit.add(new MyItem(R.drawable.strawberry,"열혈강의","서경대","20000"));
        // fruit.add(new MyItem(R.drawable.apple,"사과강의","한성대","15000"));
        // fruit.add(new MyItem(R.drawable.grape,"포도의사랑","국민대","34000"));

        asyncTaskCall();  //Thread 실행


        buttonClickListener();

    }

    private void buttonClickListener() {
        btn_home.setOnClickListener(ClickListener);
        btn_myinfo.setOnClickListener(ClickListener);
        btn_enroll_sell.setOnClickListener(ClickListener);
        btn_enroll_buy.setOnClickListener(ClickListener);
        btn_buy.setOnClickListener(ClickListener);
        btn_search.setOnClickListener(ClickListener);
    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_search:
                   if(edt_search.getText().toString() == null || edt_search.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "도서명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                       //listView.removeAllViews();
                       fruit.clear();
                       adapter.notifyDataSetChanged();

                       asyncTaskCall();
                        String name = edt_search.getText().toString();
                        Toast.makeText(getApplicationContext(), name + " (으)로 검색한 결과입니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;

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

        public MyItem(Bitmap image, String title, String publisher, String price, String day, int index){
            super();
            this.image=image;
            this.title=title;
            this.publisher=publisher;
            this.price=price;
            this.day= day;
            this.index=index;
        }
    }

    public class MyListAdapter extends BaseAdapter{

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


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
                    myname = sharedPreferences.getString("name", "");

                    if(!myname.equals("")) {     //로그인 됐으면
                        Intent intent = new Intent(getApplicationContext(), SellBoardInform.class);
                        //int whereindex = myItems.get(pos).index;
                        //Log.i("aaa", String.valueOf(myItems.get(pos).index));
                        //Toast.makeText(SellBoardBook.this, whereindex, Toast.LENGTH_SHORT).show();
                        intent.putExtra("index", myItems.get(pos).index);
                        startActivity(intent);

                    }
                    else{   //로그인 안됐으면
                        Toast.makeText(SellBoardBook.this, "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                        Intent newIntent = new Intent(getBaseContext(), Login.class);
                        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                        // 메인 액티비티를 다시 띄웁니다.
                        newIntent.putExtra("where", "sellBook");
                        newIntent.putExtra("index", myItems.get(pos).index);
                        //Log.i("이건 인덱스 처음 넘겨줌aaa", String.valueOf(whereindex));
                        startActivity(newIntent);
                    }

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

            HttpURLConnection urlConnection=null;
            URL url =null;
            DataOutputStream out=null;
            BufferedInputStream buf=null;
            BufferedReader bufreader=null;

            Properties prop = new Properties();
            prop.setProperty("edt_search",edt_search.getText().toString());
            // prop.setProperty("tempStandard",tempStandard);

            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/index.jsp");
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
                String ip = sharedPreferences.getString("ip", ""); //데이터 가져오기

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

            for(int i=jArr.length()-1; i>=0;i--){
                json=jArr.getJSONObject(i);

                fruit.add(new MyItem(imageAsyncTaskCall(json.getString("photo")), json.getString("title"), json.getString("publisher"), json.getString("price") + "원",json.getString("day"),json.getInt("index")));
            }
            adapter = new MyListAdapter(this,fruit,R.layout.list_row);
            listView.setAdapter(adapter);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }
}
