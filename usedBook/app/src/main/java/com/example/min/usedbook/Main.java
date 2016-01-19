package com.example.min.usedbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main extends AppCompatActivity {

    String myname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("ip", "192.168.43.53");  //ip 설정하기 저장하기
        sharedPreferencesEditor.commit();
        myname = sharedPreferences.getString("name", "");

        Button MyInfoBtn = (Button) findViewById(R.id.btn_myinfo_id);
/*
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences",MODE_PRIVATE);   //쉐어드 객체 얻기
        final String myname = sharedPreferences.getString("name", "");                                      //키값name을 가져오기
        Toast.makeText(this, myname + "님 환영합니다.", Toast.LENGTH_SHORT).show();
*/
        MyInfoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Toast.makeText(Main.this, myname + "내 아이디", Toast.LENGTH_SHORT).show();

                if(!myname.equals("")) {     //로그인 됐으면
                    Intent newIntent = new Intent(getBaseContext(), MyInfo.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    startActivity(newIntent);
                    Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{   //로그인 안됐으면
                    Toast.makeText(Main.this, "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(getBaseContext(), Login.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    newIntent.putExtra("where", "login");
                    startActivity(newIntent);
                    Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        Button sellregBtn = (Button) findViewById(R.id.mainsellreg);
        sellregBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!myname.equals("")) {     //로그인 됐으면
                    Intent newIntent = new Intent(getBaseContext(), ChoiceSell.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    startActivity(newIntent);
                    Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{   //로그인 안됐으면
                    Toast.makeText(Main.this, "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(getBaseContext(), Login.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    newIntent.putExtra("where", "mysell");
                    startActivity(newIntent);

                    Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        Button buyregBtn = (Button) findViewById(R.id.mainbuyreg);
        buyregBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!myname.equals("")) {     //로그인 됐으면
                    Intent newIntent = new Intent(getBaseContext(), ChoiceBuy.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    startActivity(newIntent);
                    Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{   //로그인 안됐으면
                    Toast.makeText(Main.this, "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(getBaseContext(), Login.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                    // 메인 액티비티를 다시 띄웁니다.
                    newIntent.putExtra("where", "mybuy");
                    startActivity(newIntent);

                    Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        Button buyboard = (Button) findViewById(R.id.mainbuyboard);
        buyboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 객체를 만들고 메인 액티비티를 지정합니다.
                Intent newIntent = new Intent(getBaseContext(), BuyBoard.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                // 메인 액티비티를 다시 띄웁니다.
                startActivity(newIntent);
                Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void btnbook(View v){
        Intent intent = new Intent(this, SellBoardBook.class);
        startActivity(intent);
        Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void btnclothes(View v){
        Intent intent = new Intent(this, SellBoardClothes.class);
        startActivity(intent);
        Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void btnit(View v){
        Intent intent = new Intent(this, SellBoardElectronicEquipment.class);
        startActivity(intent);
        Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void btnetc(View v){
        Intent intent = new Intent(this, SellBoardEtc.class);
        startActivity(intent);
        Toast.makeText(Main.this,"피니시한다.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
