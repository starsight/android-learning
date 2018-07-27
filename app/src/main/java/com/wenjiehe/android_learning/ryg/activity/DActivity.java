package com.wenjiehe.android_learning.ryg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wenjiehe.android_learning.BaseActivity;
import com.wenjiehe.android_learning.R;

public class DActivity extends BaseActivity {

    Button button_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        button_d =findViewById(R.id.button_d);

        button_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DActivity.this,AActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        Log.d("wenjie","D-pause");
        super.onPause();
    }
    @Override
    protected void onStop() {
        Log.d("wenjie","D-stop");
        super.onStop();
    }

}
