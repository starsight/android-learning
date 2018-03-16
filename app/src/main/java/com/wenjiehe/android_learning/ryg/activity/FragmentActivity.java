package com.wenjiehe.android_learning.ryg.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wenjiehe.android_learning.R;
import com.wenjiehe.android_learning.fragment.AFragment;
import com.wenjiehe.android_learning.fragment.BFragment;

public class FragmentActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AFragment aFragment =new AFragment();

        ft.add(R.id.framelayout,aFragment);
        ft.commit();
    }
}
