package com.github.wedemboys.penpals;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (username.equals("")) {
            Intent newActivity = new Intent(this, RegistrationActivity.class);
            newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newActivity);
            finish();
        } else {
            Intent newActivity = new Intent(this, MainPageActivity.class);
            newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(newActivity);
            finish();
        }
    }
}


