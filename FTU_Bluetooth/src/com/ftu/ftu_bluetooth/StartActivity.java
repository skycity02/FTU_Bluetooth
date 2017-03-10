package com.ftu.ftu_bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class StartActivity extends Activity {

    //延迟3秒 
    private static final short START_DELAY_MILLIS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_pic);
        
        // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity 
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goApp();
            }
        }, START_DELAY_MILLIS);
    }

    private void goApp() {
        Intent intent = new Intent(StartActivity.this, FTU_Bluetooth.class);
        StartActivity.this.startActivity(intent);
        StartActivity.this.finish();
    }
}

