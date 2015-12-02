package com.example.administrator.texttest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Lei Xiaoyue on 2015-12-01.
 */
public class MainActivity extends Activity{
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextView = new TextView(getApplicationContext());
        setContentView(mTextView);
    }
}
