package com.github.wedemboys.penpals;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(android.R.style.Theme_NoTitleBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent newActivity = new Intent(this, RegistrationActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
        finish();
    }

}
