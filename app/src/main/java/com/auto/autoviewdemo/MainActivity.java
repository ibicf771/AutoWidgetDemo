package com.auto.autoviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    TextureView textureView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("test", "MainActivity onCreate");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        Log.d("test", "MainActivity onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("test", "MainActivity onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d("test", "MainActivity onDestroy");
        super.onDestroy();
    }
}
