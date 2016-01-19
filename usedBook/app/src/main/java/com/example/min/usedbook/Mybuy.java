package com.example.min.usedbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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

public class Mybuy extends Activity {

    Button btn_home;
    Button btn_enroll_buy;
    Button btn_buy;
    Button btn_my_info;

    TableLayout table_my_buy;

    String encodedString = "";
    String my_ip="";
    String my_id="";

    int num_finish;
    int num_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybuy);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        my_ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기
        my_id = sharedPreferences.getString("ID", "" );

        btn_home = (Button)findViewById(R.id.btn_home);
        btn_enroll_buy = (Button)findViewById(R.id.btn_enroll_buy);
        btn_buy = (Button)findViewById(R.id.btn_buy);
        btn_my_info = (Button)findViewById(R.id.btn_my_info);

        buttonClickListener();

        table_my_buy = (TableLayout)findViewById(R.id.table_my_buy);

        jsonParserMyBuy(ReceiveMyBuy());
    }

    @Override
    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), MyInfo.class);
        startActivity(intent1);
    }

    private void buttonClickListener() {
        btn_home.setOnClickListener(ClickListener);
        btn_enroll_buy.setOnClickListener(ClickListener);
        btn_buy.setOnClickListener(ClickListener);
        btn_my_info.setOnClickListener(ClickListener);
    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.btn_home:
                    Intent intent1 = new Intent(getApplicationContext(), Main.class);
                    startActivity(intent1);
                    break;

                case R.id.btn_enroll_buy:
                    Intent intent2 = new Intent(getApplicationContext(), ChoiceBuy.class);
                    startActivity(intent2);
                    break;

                case R.id.btn_buy:
                    Intent intent3 = new Intent(getApplicationContext(), BuyBoard.class);
                    startActivity(intent3);
                    break;

                case R.id.btn_my_info:
                    Intent intent4 = new Intent(getApplicationContext(), MyInfo.class);
                    startActivity(intent4);
                    break;
            }
        }
    };

    private String ReceiveMyBuy() {
        HttpURLConnection urlConnection = null;
        URL url = null;
        BufferedInputStream buf = null;
        BufferedReader bufreader = null;
        DataOutputStream out = null;

        Properties prop = new Properties();

        prop.setProperty("my_id", my_id);

        encodedString = encodeString(prop);

        try{
            url = new URL("http://" + my_ip + ":8080/usedBook/putMyBuy.jsp");
            //url = new URL(addr_ip + "searchBook.jsp");
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

    private void jsonParserMyBuy(String result) {
        try{
            JSONObject json = new JSONObject(result);
            JSONArray jArr = json.getJSONArray("myBuy");

            for(int i=jArr.length()-1; i>=0; i--){
                json = jArr.getJSONObject(i);
                inputBuy(json.getInt("put_num"), json.getString("put_title"), json.getString("put_book"), json.getString("put_author"), json.getString("put_publisher"), json.getString("put_cost"), json.getString("put_state"), json.getString("put_other"), json.getInt("put_finish"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void inputBuy(final Integer num, final String title, final String book, final String author, final String publisher, final String cost, final String state, final String other, final int finish){

        table_my_buy.setStretchAllColumns(true);

        final TableRow row = new TableRow(this);
        row.setWeightSum(12);

        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        final TextView buy_title = new TextView(this);setting_body(buy_title);
        final Button btn_buy = new Button(this);setting_btn_body(btn_buy);

        buy_title.setText(title);

        if(finish == 0)
            btn_buy.setText("완료됨");
        else
            btn_buy.setText("완료해?");

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(0, 0, 1, 1);

        buy_title.setClickable(true);
        btn_buy.setClickable(true);

        row.addView(buy_title, params);
        row.addView(btn_buy, params);

        table_my_buy.addView(row);

        //미완료
        if(finish == 1){
            buy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MyBuyBoardInform.class);
                    i.putExtra("title", title);
                    i.putExtra("book", book);
                    i.putExtra("author", author);
                    i.putExtra("publisher", publisher);
                    i.putExtra("cost", cost);
                    i.putExtra("state", state);
                    i.putExtra("other", other);
                    i.putExtra("num", num);
                    startActivity(i);
                }
            });

            buy_title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mybuy.this);
                    alert_confirm.setMessage("골라골라").setCancelable(true).setPositiveButton("수정",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'yes'
                                    Intent i = new Intent(getApplicationContext(), ChangeBuy.class);
                                    i.putExtra("num", num);
                                    i.putExtra("title", title);
                                    i.putExtra("book", book);
                                    i.putExtra("author", author);
                                    i.putExtra("publisher", publisher);
                                    i.putExtra("cost", cost);
                                    i.putExtra("state", state);
                                    i.putExtra("other", other);
                                    startActivity(i);
                                }
                            }).setNegativeButton("삭제",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'No'

                                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mybuy.this);
                                    alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(true).setPositiveButton("확인",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // 'yes'
                                                    num_delete = num;
                                                    SendDeleteBuy();
                                                    Intent intent1 = new Intent(getApplicationContext(), Mybuy.class);
                                                    startActivity(intent1);
                                                    Toast.makeText(getApplicationContext(), "등록물품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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

                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();

                    return false;
                }
            });

            btn_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mybuy.this);
                    alert_confirm.setMessage("완료하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    btn_buy.setText("완료됨");
                                    num_finish = num;
                                    SendFinish();
                                    Intent intent1 = new Intent(getApplicationContext(), Mybuy.class);
                                    startActivity(intent1);
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
            });
        }

        //완료
        else{
            buy_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), MyBuyBoardInformFinish.class);
                    i.putExtra("title", title);
                    i.putExtra("book", book);
                    i.putExtra("author", author);
                    i.putExtra("publisher", publisher);
                    i.putExtra("cost", cost);
                    i.putExtra("state", state);
                    i.putExtra("other", other);
                    i.putExtra("num", num);
                    startActivity(i);
                }
            });

            buy_title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(Mybuy.this);
                    alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(true).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'yes'
                                    num_delete = num;
                                    SendDeleteBuy();
                                    Intent intent1 = new Intent(getApplicationContext(), Mybuy.class);
                                    startActivity(intent1);
                                    Toast.makeText(getApplicationContext(), "등록물품이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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

                    return false;
                }
            });
        }
    }

    private void setting_body(TextView v){
        v.setBackgroundColor(Color.parseColor("#ffffff"));
        v.setTextColor(Color.parseColor("#000000"));
        //v.setGravity(Gravity.CENTER);
        v.setPadding(70, 30, 70, 30);
        v.setTextSize(18);
    }

    private void setting_btn_body(TextView v){
        v.setBackgroundColor(Color.parseColor("#71bf44"));
        v.setTextColor(Color.parseColor("#ffffff"));
        v.setGravity(Gravity.CENTER);
        v.setPadding(5, 30, 5, 30);
        v.setTextSize(18);
    }

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

    private void SendFinish() {
        HttpURLConnection urlConnection = null;
        URL url = null;
        DataOutputStream out = null;
        BufferedInputStream buf = null;

        Properties prop = new Properties();
        prop.setProperty("num_finish", toString().valueOf(num_finish));

        encodedString = encodeString(prop);

        try{
            url = new URL("http://" + my_ip + ":8080/usedBook/updateFinish.jsp");
            //url = new URL(addr_ip + "updateFinish.jsp");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            out = new DataOutputStream(urlConnection.getOutputStream());

            out.writeBytes(encodedString);

            out.flush();    //서버로 버퍼의 내용 전송

            buf = new BufferedInputStream(urlConnection.getInputStream());

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            urlConnection.disconnect();//URL 연결해제
        }
    }

    private void SendDeleteBuy() {
        HttpURLConnection urlConnection = null;
        URL url = null;
        DataOutputStream out = null;
        BufferedInputStream buf = null;

        Properties prop = new Properties();
        prop.setProperty("num_delete", toString().valueOf(num_delete));

        encodedString = encodeString(prop);

        try{
            url = new URL("http://" + my_ip + ":8080/usedBook/deleteBuy.jsp");
            //url = new URL(addr_ip + "deleteBuy.jsp");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);

            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            out = new DataOutputStream(urlConnection.getOutputStream());

            out.writeBytes(encodedString);

            out.flush();    //서버로 버퍼의 내용 전송

            buf = new BufferedInputStream(urlConnection.getInputStream());

        }catch(Exception e){
            e.printStackTrace();

        }finally{
            urlConnection.disconnect();//URL 연결해제
        }
    }
}