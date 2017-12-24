package com.wenjiehe.android_learning.ryg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wenjiehe.android_learning.R;

public class CActivity extends AppCompatActivity {

    Button button_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        button_c =findViewById(R.id.button_c);

        button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CActivity.this,DActivity.class);
                startActivity(intent);
            }
        });
    }
}
