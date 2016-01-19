package com.example.min.usedbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;


public class MyBuyBoardInform extends Activity {

    TextView info_title;
    TextView info_book;
    TextView info_author;
    TextView info_publisher;
    TextView info_cost;
    TextView info_state;
    TextView info_other;

    Button btn_update;
    Button btn_delete;

    String encodedString = "";

    String my_ip = "";

    int num_finish;
    int num_delete;

    int num;

    String title;
    String book;
    String author;
    String publisher;
    String cost;
    String state;
    String other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_buy_board_inform);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        my_ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기

        info_title = (TextView)findViewById(R.id.info_title);
        info_book = (TextView)findViewById(R.id.info_book);
        info_author = (TextView)findViewById(R.id.info_author);
        info_publisher = (TextView)findViewById(R.id.info_publisher);
        info_cost = (TextView)findViewById(R.id.info_cost);
        info_state = (TextView)findViewById(R.id.info_state);
        info_other = (TextView)findViewById(R.id.info_other);

        btn_update = (Button)findViewById(R.id.btn_update);
        btn_delete = (Button)findViewById(R.id.btn_delete);

        Intent i = getIntent();
        title = i.getExtras().getString("title");
        book = i.getExtras().getString("book");
        author = i.getExtras().getString("author");
        publisher = i.getExtras().getString("publisher");
        cost = i.getExtras().getString("cost");
        state = i.getExtras().getString("state");
        other = i.getExtras().getString("other");
        num = i.getExtras().getInt("num");

        info_title.setText(title);
        info_book.setText(book);
        info_author.setText(author);
        info_publisher.setText(publisher);
        info_cost.setText(cost);
        info_state.setText(state);
        info_other.setText(other);

        buttonClickListener();
    }

    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Mybuy.class);
        startActivity(intent1);
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
                    Toast.makeText(getApplicationContext(), "수정하기", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.btn_delete:
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MyBuyBoardInform.this);
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
                    break;

            }
        }
    };

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
