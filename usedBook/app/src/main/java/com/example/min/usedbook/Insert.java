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

public class Insert extends AppCompatActivity {

    TextView ID,nowPW,newPW,confirmPW,name,tel,birth;
    Button OKbtn,CANCELbtn;
    String encodedString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        ID = (TextView) findViewById(R.id.inputID);
        nowPW = (TextView) findViewById(R.id.inputnowPW);
        newPW = (TextView) findViewById(R.id.inputnewPW);
        confirmPW = (TextView) findViewById(R.id.inputconfirmPW);
        name = (TextView) findViewById(R.id.inputname);
        tel = (TextView) findViewById(R.id.inputtel);
        birth = (TextView) findViewById(R.id.inputbirth);
        OKbtn = (Button) findViewById(R.id.b1);
        //CANCELbtn = (Button) findViewById(R.id.button18);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);   //쉐어드 객체 얻기
        /*
        String ID = sharedPreferences.getString("ID", "");                                      //키값name을 가져오기
        String name = sharedPreferences.getString("name", "");
        String tel = sharedPreferences.getString("tel", "" );
        String birth = sharedPreferences.getString("birth", "" );
*/


        ID.setText(sharedPreferences.getString("ID", ""));
        ID.setFocusable(false);
        ID.setClickable(false);
        name.setText(sharedPreferences.getString("name", ""));
        tel.setText(sharedPreferences.getString("tel", ""));
        birth.setText(sharedPreferences.getString("birth", ""));

        OKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nowPW.getText().toString().equals("")) {
                    nowPW.requestFocus();
                    Toast.makeText(Insert.this, "현재 PW를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (newPW.getText().toString().equals("")) {
                    newPW.requestFocus();
                    Toast.makeText(Insert.this, "새로운 PW를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (confirmPW.getText().toString().equals("")) {
                    confirmPW.requestFocus();
                    Toast.makeText(Insert.this, "확인 PW를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!newPW.getText().toString().equals(confirmPW.getText().toString())) {
                    newPW.requestFocus();
                    Toast.makeText(Insert.this, "비밀번호가 서로 다릅니다.", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().equals("")) {
                    name.requestFocus();
                    Toast.makeText(Insert.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (tel.getText().toString().equals("")) {
                    tel.requestFocus();
                    Toast.makeText(Insert.this, "전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (birth.getText().toString().equals("")) {
                    birth.requestFocus();
                    Toast.makeText(Insert.this, "생일을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    asyncTaskCall();
                }

            }
        });

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
            prop.setProperty("nowPW",nowPW.getText().toString());
            prop.setProperty("newPW",newPW.getText().toString());
            prop.setProperty("name", name.getText().toString());
            prop.setProperty("tel",tel.getText().toString());
            prop.setProperty("birth",birth.getText().toString());


            encodedString = encodeString(prop);

            try{
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
                String ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

                url=new URL("http://" + ip + ":8080/usedBook/edit.jsp");
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
            JSONArray jArr =json.getJSONArray("RESULT");
            json=jArr.getJSONObject(0);
            if(json.getString("result").equals("성공")) {

                SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);   //쉐어드 객체 얻기
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();                        //쉐어드 쓰기
                sharedPreferencesEditor.putString("ID", ID.getText().toString());                                   //키값name에 쓰기
                sharedPreferencesEditor.putString("name", name.getText().toString());
                sharedPreferencesEditor.putString("tel", tel.getText().toString());
                sharedPreferencesEditor.putString("birth", birth.getText().toString());
                sharedPreferencesEditor.commit();

                Toast.makeText(Insert.this, "정보수정 완료", Toast.LENGTH_SHORT).show();
                finish();   //수정되면 액티비티 종료
            }else if(json.getString("result").equals("실패")){
                Toast.makeText(Insert.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(Login.this, "됐다", Toast.LENGTH_SHORT).show();
        }catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(Insert.this, "실패", Toast.LENGTH_SHORT).show();
        }

    }

    public void cancel(View v){
        finish();
    }

    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }
}
