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

public class FindPassword extends AppCompatActivity {
    TextView ID,name,tel,birth;
    Button OKbtn;
    String encodedString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        ID = (TextView) findViewById(R.id.inputID);
        name = (TextView) findViewById(R.id.inputname);
        tel = (TextView) findViewById(R.id.inputtel);
        birth = (TextView) findViewById(R.id.inputbirth);
        OKbtn = (Button) findViewById(R.id.b1);

        OKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ID.getText().toString().equals("")) {
                    ID.requestFocus();
                    Toast.makeText(FindPassword.this, "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().equals("")) {
                    name.requestFocus();
                    Toast.makeText(FindPassword.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (tel.getText().toString().equals("")) {
                    tel.requestFocus();
                    Toast.makeText(FindPassword.this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if (birth.getText().toString().equals("")) {
                    birth.requestFocus();
                    Toast.makeText(FindPassword.this, "생일을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    asyncTaskCall();
                }
            }
        });

    }

    public void asyncTaskCall(){

        new MyAsyncTask().execute();
        Toast.makeText(this, "보냄", Toast.LENGTH_SHORT).show();
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
            prop.setProperty("name",name.getText().toString());
            prop.setProperty("tel",tel.getText().toString());
            prop.setProperty("birth",birth.getText().toString());

            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/findPW.jsp");
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
            JSONArray jArr =json.getJSONArray("PW");
            json=jArr.getJSONObject(0);
            if(!json.getString("pw").equals("")) {
                Toast.makeText(FindPassword.this,"너의 비번은 : " + json.getString("pw"), Toast.LENGTH_SHORT).show();
                finish();   //수정되면 액티비티 종료
            }else{
                Toast.makeText(FindPassword.this, "정보가 없습니다.", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(Login.this, "됐다", Toast.LENGTH_SHORT).show();
        }catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(FindPassword.this, "실패", Toast.LENGTH_SHORT).show();
        }

    }

    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }
}
