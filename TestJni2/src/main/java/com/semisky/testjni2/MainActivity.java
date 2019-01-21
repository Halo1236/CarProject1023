package com.semisky.testjni2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    XJni xJni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xJni = new XJni();
        findViewById(R.id.btn_click_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = xJni.getStr("Welcome SZ !!!");
                title = title !=null ? title:"UNKNOW !!!";
                Toast.makeText(MainActivity.this,"惊喜："+title,Toast.LENGTH_LONG).show();
            }
        });
    }
}
