package com.example.min.usedbook;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

public class Login extends AppCompatActivity {


    TextView ID,PW;
    Button login;
    String encodedString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
        information who = (information) getApplication();   //로그인 상태 가져오기
        who.setLoginstate("yet");                         //로그인 셋팅
        Toast.makeText(Login.this, who.getLoginstate(), Toast.LENGTH_SHORT).show();
        */
        ID = (TextView) findViewById(R.id.id);
        PW = (TextView) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.log);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ID.getText().toString().equals("")) {
                    Toast.makeText(Login.this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (PW.getText().toString().equals("")) {
                    Toast.makeText(Login.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    //ID,PW 전송 해야해
                    asyncTaskCall();
                }
            }
        });

    }

    public void join2(View v){          //회원가입
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }

    public void loginfindid(View v){    //아이디 찾기
        Intent intent = new Intent(this, FindId.class);
        startActivity(intent);
    }

    public void loginfindpassword(View v){      //비번찾기
        Intent intent = new Intent(this, FindPassword.class);
        startActivity(intent);
    }

    public void asyncTaskCall(){

        new MyAsyncTask().execute();
        //Toast.makeText(this, "보냄", Toast.LENGTH_SHORT).show();
    }

    public class MyAsyncTask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {  // 통신을 위한 Thread
            String result = recvList();
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

            HttpURLConnection urlConnection = null;
            URL url = null;
            DataOutputStream out = null;
            BufferedInputStream buf = null;
            BufferedReader bufreader = null;


            Properties prop = new Properties();

            prop.setProperty("ID",ID.getText().toString());   //키값 "temGoods"으로 보낼 값 tempGoods을 보낸다.(사실 버퍼에 넣는거)
            prop.setProperty("PW",PW.getText().toString());


            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/login.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);

                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                out = new DataOutputStream(urlConnection.getOutputStream());

                out.writeBytes(encodedString);

                out.flush();    //서버로 버퍼의 내용 전송 후 버퍼를 비워줌


                buf = new BufferedInputStream(urlConnection.getInputStream());
                bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));

                String line=null;
                String result="";

                while((line = bufreader.readLine()) != null){   //이건 내용을 받기
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

        protected void onPostExecute(String result){  //Thread 가 끝나고 받은 데이터(result)를 다른 쓰레드로 어케 넘겨 줄거냐

            jsonParserList(result);
        }
    }

    private void jsonParserList(String recv) {  //다른 쓰레드로 이렇게 넘겨 주겠다

        Log.i("서버에서 받은 전체 내용 : ", recv);
        //Toast.makeText(Login.this, recv, Toast.LENGTH_SHORT).show();
        try{
            JSONObject json = new JSONObject(recv);
            JSONArray jArr =json.getJSONArray("NAME");
            json=jArr.getJSONObject(0);
            if(!json.getString("name").equals("")) {
                Toast.makeText(Login.this, json.getString("name") + "님 환영합니다", Toast.LENGTH_SHORT).show();

                /*
                information who = (information) getApplication();   //로그인 상태 가져오기
                who.setLoginstate("login");                         //로그인 셋팅
                Toast.makeText(Login.this, who.getLoginstate(), Toast.LENGTH_SHORT).show();
                information id = (information) getApplication();   //아이디 가져오기
                id.setmyid(json.getString("name"));                 //아이디 셋팅
                */

                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);   //쉐어드 객체 얻기
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();                        //쉐어드 쓰기
                sharedPreferencesEditor.putString("ID", ID.getText().toString());                                   //키값name에 쓰기
                sharedPreferencesEditor.putString("name", json.getString("name"));
                sharedPreferencesEditor.putString("tel", json.getString("tel"));
                sharedPreferencesEditor.putString("birth", json.getString("birth"));
                sharedPreferencesEditor.commit();                                                                   //제출
                /*
                Toast.makeText(Login.this, ID.getText().toString() + " 아이디", Toast.LENGTH_SHORT).show();
                Toast.makeText(Login.this, json.getString("name") + " 이름", Toast.LENGTH_SHORT).show();
                Toast.makeText(Login.this, json.getString("tel") + " 번호", Toast.LENGTH_SHORT).show();
                */
                String Where = getIntent().getStringExtra("where");

                if(Where.equals("login")) {


                    Intent newIntent = new Intent(getBaseContext(), MyInfo.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    startActivity(newIntent);
                }else if(Where.equals("mysell")){
                    Intent newIntent = new Intent(getBaseContext(), ChoiceSell.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    startActivity(newIntent);
                }else if(Where.equals("mybuy")){
                    Intent newIntent = new Intent(getBaseContext(), ChoiceBuy.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    startActivity(newIntent);
                }else if(Where.equals("sellBook")) {
                    Intent i = getIntent();
                    int whereindex = i.getExtras().getInt("index");
                    //Log.i("로그인aaa", Whereindex);
                    Intent newIntent = new Intent(getBaseContext(), SellBoardInform.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?

                    newIntent.putExtra("index", whereindex);
                    // 메인 액티비티를 다시 띄웁니다.
                    startActivity(newIntent);
                }
                //intent.putExtra("index", myItems.get(pos).index);

                else{
                    Toast.makeText(Login.this, "오류", Toast.LENGTH_SHORT).show();
                }
                finish();

            }else{
                Toast.makeText(Login.this, "아이디, 비밀번호 확인하세요", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(Login.this, "됐다", Toast.LENGTH_SHORT).show();
        }catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(Login.this, "실패", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }

}