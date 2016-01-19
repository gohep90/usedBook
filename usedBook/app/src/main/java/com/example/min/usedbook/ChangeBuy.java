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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

public class ChangeBuy extends Activity {

    Button c_ebuy;
    Button c_cbuy;

    TextView text_name;
    TextView text_num;

    EditText edt_btitle;
    EditText edt_bbook;
    EditText edt_bauthor;
    EditText edt_bpublisher;
    EditText edt_bcost;
    EditText edt_bstate;
    EditText edt_bother;

    String encodedString = "";

    String my_ip="";
    String my_name="";
    String my_tel="";

    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_buy);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        my_ip = sharedPreferences.getString("ip", "" ); //데이터 가져오기
        my_name = sharedPreferences.getString("name", "" );
        my_tel = sharedPreferences.getString("tel", "" );

        c_ebuy = (Button)findViewById(R.id.c_ebuy);
        c_cbuy = (Button)findViewById(R.id.c_cbuy);

        text_name = (TextView)findViewById(R.id.text_name);
        text_num = (TextView)findViewById(R.id.text_num);

        edt_btitle = (EditText)findViewById(R.id.c_btitle);
        edt_bbook = (EditText)findViewById(R.id.c_bbook);
        edt_bauthor = (EditText)findViewById(R.id.c_bauthor);
        edt_bpublisher = (EditText)findViewById(R.id.c_bpublisher);
        edt_bcost = (EditText)findViewById(R.id.c_bcost);
        edt_bstate = (EditText)findViewById(R.id.c_bstate);
        edt_bother = (EditText)findViewById(R.id.c_bother);

        Intent i = getIntent();
        num = i.getExtras().getInt("num");
        String title = i.getExtras().getString("title");
        String book = i.getExtras().getString("book");
        String author = i.getExtras().getString("author");
        String publisher = i.getExtras().getString("publisher");
        String cost = i.getExtras().getString("cost");
        String state = i.getExtras().getString("state");
        String other = i.getExtras().getString("other");

        text_name.setText(my_name);
        text_num.setText(my_tel);

        edt_btitle.setText(title);
        edt_bbook.setText(book);
        edt_bauthor.setText(author);
        edt_bpublisher.setText(publisher);
        edt_bcost.setText(cost);
        edt_bstate.setText(state);
        edt_bother.setText(other);

        buttonClickListener();
    }

    private void buttonClickListener() {
        c_ebuy.setOnClickListener(ClickListener);
        c_cbuy.setOnClickListener(ClickListener);
    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.c_ebuy:
                    if(edt_btitle.getText().toString() == null || edt_btitle.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "글제목을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(edt_bbook.getText().toString() == null || edt_bbook.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "도서명을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(edt_bauthor.getText().toString() == null || edt_bauthor.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "저자를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(edt_bpublisher.getText().toString() == null || edt_bpublisher.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "출판사를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(edt_bcost.getText().toString() == null || edt_bcost.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "가격을 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(edt_bstate.getText().toString() == null || edt_bstate.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "상태를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else if(edt_bother.getText().toString() == null || edt_bother.getText().toString().length() == 0)
                        Toast.makeText(getApplicationContext(), "기타를 입력하세요.", Toast.LENGTH_SHORT).show();
                    else {
                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ChangeBuy.this);
                        alert_confirm.setMessage("수정하시겠습니까?").setCancelable(true).setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 'yes'
                                        SendUpdateBuy();
                                        Toast.makeText(getApplicationContext(), "삽니다 물품이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), Mybuy.class);
                                        startActivity(i);
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
                    break;

                case R.id.c_cbuy:
                    Intent i = new Intent(getApplicationContext(), Mybuy.class);
                    startActivity(i);
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

    private void SendUpdateBuy() {
        HttpURLConnection urlConnection = null;
        URL url = null;
        DataOutputStream out = null;
        BufferedInputStream buf = null;

        Properties prop = new Properties();

        prop.setProperty("num", toString().valueOf(num));
        prop.setProperty("edt_btitle", edt_btitle.getText().toString());
        prop.setProperty("edt_bbook", edt_bbook.getText().toString());
        prop.setProperty("edt_bauthor", edt_bauthor.getText().toString());
        prop.setProperty("edt_bpublisher", edt_bpublisher.getText().toString());
        prop.setProperty("edt_bcost", edt_bcost.getText().toString());
        prop.setProperty("edt_bstate", edt_bstate.getText().toString());
        prop.setProperty("edt_bother", edt_bother.getText().toString());

        encodedString = encodeString(prop);

        try{
            url = new URL("http://" + my_ip + ":8080/usedBook/updateBuy.jsp");
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