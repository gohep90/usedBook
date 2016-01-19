package com.example.min.usedbook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BuyBoardInform extends Activity {

    TextView info_title;
    TextView info_book;
    TextView info_author;
    TextView info_publisher;
    TextView info_cost;
    TextView info_state;
    TextView info_other;

    Button btn_call;
    Button btn_msg;

    String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_board_inform);

        info_title = (TextView)findViewById(R.id.info_title);
        info_book = (TextView)findViewById(R.id.info_book);
        info_author = (TextView)findViewById(R.id.info_author);
        info_publisher = (TextView)findViewById(R.id.info_publisher);
        info_cost = (TextView)findViewById(R.id.info_cost);
        info_state = (TextView)findViewById(R.id.info_state);
        info_other = (TextView)findViewById(R.id.info_other);

        btn_call = (Button)findViewById(R.id.btn_call);
        btn_msg = (Button)findViewById(R.id.btn_msg);

        Intent i = getIntent();
        String title = i.getExtras().getString("title");
        String book = i.getExtras().getString("book");
        String author = i.getExtras().getString("author");
        String publisher = i.getExtras().getString("publisher");
        String cost = i.getExtras().getString("cost");
        String state = i.getExtras().getString("state");
        String other = i.getExtras().getString("other");
        tel = 0 + toString().valueOf(i.getExtras().getInt("tel"));

        info_title.setText(title);
        info_book.setText(book);
        info_author.setText(author);
        info_publisher.setText(publisher);
        info_cost.setText(cost);
        info_state.setText(state);
        info_other.setText(other);

        buttonClickListener();
    }

    private void buttonClickListener() {
        btn_call.setOnClickListener(ClickListener);
        btn_msg.setOnClickListener(ClickListener);
    }

    View.OnClickListener ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.btn_call:
                    //전화걸기
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
                    startActivity(intent);
                    break;

                case R.id.btn_msg:
                    //문자하기
                    Intent intent2= new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tel)); //시스템 액티비티인 SMS문자보내기 Activity의 action값
                    startActivity(intent2);
                    break;
            }
        }
    };
}
