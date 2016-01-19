package com.example.min.usedbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SellBoardElectronicEquipment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_board_electronic_equipment);
        Button HomeBtn = (Button) findViewById(R.id.it_btn_home_id);

        // 버튼을 눌렀을 때 메인 액티비티를 다시 한 번 띄워줍니다.
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 객체를 만들고 메인 액티비티를 지정합니다.
                Intent newIntent = new Intent(getBaseContext(), Main.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                // 메인 액티비티를 다시 띄웁니다.
                startActivity(newIntent);
            }
        });

        Button MyInfoBtn = (Button) findViewById(R.id.it_myinfo_id);

        // 버튼을 눌렀을 때 메인 액티비티를 다시 한 번 띄워줍니다.
        MyInfoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 객체를 만들고 메인 액티비티를 지정합니다.
                Intent newIntent = new Intent(getBaseContext(), MyInfo.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//넘기는 메인 flag에 설정을 넣은거야, 원래있던게 사라지고 다시 로드되는건가?
                // 메인 액티비티를 다시 띄웁니다.
                startActivity(newIntent);
            }
        });
    }
    public void contentsclick(View v){
        Intent intent = new Intent(this, SellBoardInform.class);
        startActivity(intent);
    }
}
