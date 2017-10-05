package com.github.wedemboys.penpals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    public void goToViewProfile(View view){
        Intent newActivity = new Intent(this, ViewProfileActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
    }

    public void goToNewChat(View view) {
        Intent newActivity = new Intent(this, NewChatActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newActivity.putExtra(Constants.USER_INTENT_KEY, getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.USERNAME_KEY, ""));
        startActivity(newActivity);
    }
}
