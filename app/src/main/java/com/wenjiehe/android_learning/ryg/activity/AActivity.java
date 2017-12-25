package com.wenjiehe.android_learning.ryg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wenjiehe.android_learning.R;

public class AActivity extends BaseActivity {

    Button button_a;
    Button button_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        button_a = findViewById(R.id.button_a);
        button_ad = findViewById(R.id.button_ad);


        button_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AActivity.this,BActivity.class);
                startActivity(intent);
            }
        });

        button_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AActivity.this,DActivity.class);
                startActivity(intent);
            }
        });
    }
}
