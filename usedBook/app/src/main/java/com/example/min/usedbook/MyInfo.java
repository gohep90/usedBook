package com.example.min.usedbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        Button HomeBtn = (Button) findViewById(R.id.btn_home_id);
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 객체를 만들고 메인 액티비티를 지정합니다.
                Intent newIntent = new Intent(getBaseContext(), Main.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                // 메인 액티비티를 다시 띄웁니다.
                startActivity(newIntent);
                finish();
            }
        });

        Button sellregBtn = (Button) findViewById(R.id.myinfosellregistration);
        sellregBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 객체를 만들고 메인 액티비티를 지정합니다.
                Intent newIntent = new Intent(getBaseContext(), ChoiceSell.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                // 메인 액티비티를 다시 띄웁니다.
                startActivity(newIntent);

            }
        });

        Button buyregBtn = (Button) findViewById(R.id.myinfobuyreg);
        buyregBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 객체를 만들고 메인 액티비티를 지정합니다.
                Intent newIntent = new Intent(getBaseContext(), ChoiceBuy.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                // 메인 액티비티를 다시 띄웁니다.
                startActivity(newIntent);

            }
        });

        Button buyboard = (Button) findViewById(R.id.myinfobuyboard);
        buyboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 객체를 만들고 메인 액티비티를 지정합니다.
                Intent newIntent = new Intent(getBaseContext(), BuyBoard.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                // 메인 액티비티를 다시 띄웁니다.
                startActivity(newIntent);
                finish();
            }
        });

    }
    public void btn_myselllist(View v){
        Intent intent = new Intent(this, Mysell.class);
        startActivity(intent);
    }

    public void btn_mybuylist(View v){
        Intent intent = new Intent(this, Mybuy.class);
        startActivity(intent);
    }

    public void btn_editprofile(View v){
        Intent intent = new Intent(this, Insert.class);
        startActivity(intent);
    }
    public void btn_logout(View v){
        //Intent intent = new Intent(this, Login.class);
        //startActivity(intent);
        /*
        information who = (information) getApplication();   //로그인 상태 가져오기
        who.setLoginstate("yet");
        Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();

        information id = (information) getApplication();   //아이디 가져오기
        id.setmyid("no");                 //아이디 셋팅
*/
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);   //쉐어드 객체 얻기
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();                        //쉐어드 쓰기
        sharedPreferencesEditor.putString("name", "");                                  //키값name에 쓰기
        sharedPreferencesEditor.putString("name", "");
        sharedPreferencesEditor.putString("tel", "");
        sharedPreferencesEditor.putString("birth", "");
        sharedPreferencesEditor.commit();                                                                   //제출

        Toast.makeText(MyInfo.this, "로그아웃 됐습니다.", Toast.LENGTH_SHORT).show();        //출력

        Intent newIntent = new Intent(getBaseContext(), Main.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
        // 메인 액티비티를 다시 띄웁니다.
        startActivity(newIntent);
        finish();
    }

    public void onBackPressed(){
        Intent intent1 = new Intent(getApplicationContext(), Main.class);
        startActivity(intent1);
        finish();
    }

}
