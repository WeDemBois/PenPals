package com.github.wedemboys.penpals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_main_page, null);
        final LinearLayout ll = (LinearLayout) view.findViewById(R.id.messagesLinearLayout);
        System.out.println(ll);

        RequestQueue queue = Volley.newRequestQueue(this);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        String url = "http://cjdesktop.rh.rit.edu/penpals?action=retrievemessage&username=" + username;

        final Gson gson = new Gson();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final Message message = gson.fromJson(response, Message.class);
                        if (message != null && message.getSender() != null && message.getSender().length() > 0) {
                            Button button = new Button(MainPageActivity.this.getBaseContext());
                            button.setText(message.getSender());
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainPageActivity.this, ChatActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra(Constants.USER_INTENT_KEY, message.getSender());
                                    intent.putExtra(Constants.MESSAGE_CONTENT, message.getContent());
                                    startActivity(intent);
                                }
                            });
                            ll.addView(button);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        setContentView(view);
    }

    @Override
    public void onBackPressed() {
    }

    public void goToViewProfile(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME_KEY, "");
        Intent newActivity = new Intent(this, ViewProfileActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newActivity.putExtra(Constants.USERNAME_INTENT_EXTRA_KEY, username);
        startActivity(newActivity);
    }

    public void goToNewChat(View view) {
        Intent newActivity = new Intent(this, NewChatActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newActivity.putExtra(Constants.USER_INTENT_KEY, getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.USERNAME_KEY, ""));
        startActivity(newActivity);
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USERNAME_KEY, "");
        editor.commit();
        Intent newActivity = new Intent(this, MainActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
    }

    public void goToChat(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
