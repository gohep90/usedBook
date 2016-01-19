package com.example.min.usedbook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class BuyBoard extends Activity {

    ImageButton btn_search;
    EditText edt_search;
    Button btn_home;
    Button btn_my_info;
    Button btn_enroll_buy;

    TableLayout table_buy;

    String encodedString = "";

    String my_ip="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_board);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        my_ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

        edt_search = (EditText)findViewById(R.id.edt_search);

        btn_search = (ImageButton)findViewById(R.id.btn_search);
        btn_home = (Button)findViewById(R.id.btn_home);
        btn_my_info = (Button)findViewById(R.id.btn_my_info);
        btn_enroll_buy = (Button)findViewById(R.id.btn_enroll_buy);

        table_buy = (TableLayout)findViewById(R.id.table_buy);

        buttonClickListener();
        header();
        jsonParserBuy(ReceiveBuy());
    }

    @Override
    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
    }

    private void buttonClickListener() {
        btn_home.setOnClickListener(ClickListener);
        btn_search.setOnClickListener(ClickListener);
        btn_my_info.setOnClickListener(ClickListener);
        btn_enroll_buy.setOnClickListener(ClickListener);
    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_search:
                    if(edt_search.getText().toString() == null || edt_search.getText().toString().length() == 0) {
                        table_buy.removeAllViews();
                        header();
                        jsonParserBuy(ReceiveBuy());
                        Toast.makeText(getApplicationContext(), "도서명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        table_buy.removeAllViews();
                        header();
                        jsonParserBuy2(ReceiveBuy2());
                        String name = edt_search.getText().toString();
                        Toast.makeText(getApplicationContext(), name + " (으)로 검색한 결과입니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btn_home:
                    Intent intent1 = new Intent(getApplicationContext(), Main.class);
                    startActivity(intent1);
                    break;

                case R.id.btn_my_info:
                    Intent intent2 = new Intent(getApplicationContext(), MyInfo.class);
                    startActivity(intent2);
                    break;

                case R.id.btn_enroll_buy:
                    Intent intent3 = new Intent(getApplicationContext(), ChoiceBuy.class);
                    startActivity(intent3);
                    break;
            }
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String encodeString(Properties params) {
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String ReceiveBuy() {
        HttpURLConnection urlConnection = null;
        URL url = null;
        BufferedInputStream buf = null;
        BufferedReader bufreader = null;

        try{
            url = new URL("http://" + my_ip + ":8080/usedBook/putBuy.jsp");

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            buf = new BufferedInputStream(urlConnection.getInputStream());

            bufreader = new BufferedReader(new InputStreamReader(buf,"utf-8"));

            String line = null;
            String result = "";

            //버퍼의 웹문서 소스를 줄 단위로 읽어(line), result에 저장
            while((line = bufreader.readLine())!=null){
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

    private void jsonParserBuy(String result) {
        try{
            JSONObject json = new JSONObject(result);
            JSONArray jArr = json.getJSONArray("Buy");

            for(int i=jArr.length()-1; i>=0; i--){
                json = jArr.getJSONObject(i);
                    inputBuy(json.getString("buy_title"), json.getString("buy_book"), json.getString("buy_author"), json.getString("buy_publisher"), json.getString("buy_cost"), json.getString("buy_state"), json.getString("buy_other"), json.getString("buy_day"), json.getInt("buy_tel"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String ReceiveBuy2() {
        HttpURLConnection urlConnection = null;
        URL url = null;
        BufferedInputStream buf = null;
        BufferedReader bufreader = null;
        DataOutputStream out = null;

        Properties prop = new Properties();

        prop.setProperty("edt_search", edt_search.getText().toString());

        encodedString = encodeString(prop);

        try{
            url = new URL("http://" + my_ip + ":8080/usedBook/searchBook.jsp");
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

            String line = null;
            String result = "";

            //버퍼의 웹문서 소스를 줄 단위로 읽어(line), result에 저장
            while((line = bufreader.readLine()) != null){
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

    private void jsonParserBuy2(String result) {
        try{
            JSONObject json = new JSONObject(result);
            JSONArray jArr = json.getJSONArray("sBuy");

            for(int i=0; i<jArr.length(); i++){
                json = jArr.getJSONObject(i);
                inputBuy(json.getString("put_title"), json.getString("put_book"), json.getString("put_author"), json.getString("put_publisher"), json.getString("put_cost"), json.getString("put_state"), json.getString("put_other"), json.getString("put_day"), json.getInt("put_tel"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 삽니다 게시판에 추가
    private void inputBuy(final String title, final String book, final String author, final String publisher, final String cost, final String state, final String other, String day, final int tel){

        table_buy.setStretchAllColumns(true);

        TableRow row = new TableRow(this);
        row.setWeightSum(12);

        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        final TextView buy_day = new TextView(this); setting_dbody(buy_day);
        final TextView buy_title = new TextView(this); setting_body(buy_title);

        buy_day.setText(day);
        buy_title.setText(title);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(0, 0, 1, 1);

        buy_day.setClickable(true);
        buy_title.setClickable(true);

        row.addView(buy_day, params);
        row.addView(buy_title, params);

        table_buy.addView(row);

        buy_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BuyBoardInform.class);
                i.putExtra("title", title);
                i.putExtra("book", book);
                i.putExtra("author", author);
                i.putExtra("publisher", publisher);
                i.putExtra("cost", cost);
                i.putExtra("state", state);
                i.putExtra("other", other);
                i.putExtra("tel", tel);

                startActivity(i);
            }
        });

        buy_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BuyBoardInform.class);
                i.putExtra("title", title);
                i.putExtra("book", book);
                i.putExtra("author", author);
                i.putExtra("publisher", publisher);
                i.putExtra("cost", cost);
                i.putExtra("state", state);
                i.putExtra("other", other);
                i.putExtra("tel", tel);

                startActivity(i);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void header(){
        table_buy.setStretchAllColumns(true);
        TableRow row = new TableRow(this);
        row.setWeightSum(12);

        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        TextView header_day = new TextView(this); setting_head(header_day);
        TextView header_title = new TextView(this); setting_head(header_title);

        header_day.setText("등록날짜");
        header_title.setText("글제목");

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(0, 0, 1, 1);


        row.addView(header_day, params);
        row.addView(header_title, params);

        table_buy.addView(row);
    }

    private void setting_head(TextView v){
        v.setBackgroundColor(Color.parseColor("#5e5e5e"));
        v.setTextColor(Color.parseColor("#ffffff"));
        v.setGravity(Gravity.CENTER);
        v.setPadding(0, 15, 0, 15);
        v.setTextSize(18);
    }

    private void setting_body(TextView v){
        v.setBackgroundColor(Color.parseColor("#ffffff"));
        v.setTextColor(Color.parseColor("#000000"));
        //v.setGravity(Gravity.CENTER);
        v.setPadding(50, 30, 50, 30);
        v.setTextSize(18);
    }

    private void setting_dbody(TextView v){
        v.setBackgroundColor(Color.parseColor("#ffffff"));
        v.setTextColor(Color.parseColor("#000000"));
        v.setGravity(Gravity.CENTER);
        v.setPadding(0, 30, 0, 30);
        v.setTextSize(18);
    }
}