package com.wenjiehe.android_learning.ryg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wenjiehe.android_learning.R;

public class BActivity extends AppCompatActivity {

    Button button_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        button_b =findViewById(R.id.button_b);

        button_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BActivity.this,CActivity.class);
                startActivity(intent);
            }
        });


    }
}
