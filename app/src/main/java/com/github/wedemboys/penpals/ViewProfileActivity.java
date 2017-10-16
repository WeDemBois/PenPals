package com.github.wedemboys.penpals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import static com.github.wedemboys.penpals.R.id.editProfile;

public class ViewProfileActivity extends Activity {

    public static Gson gson;
    private String username;
    private String currentUsername;
    private Button editButton;
    public static String[] display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        currentUsername = sharedPreferences.getString("username", "");

        this.username = getIntent().getStringExtra(Constants.USERNAME_INTENT_EXTRA_KEY);
        this.editButton = (Button) findViewById(editProfile);

        editButton.setVisibility((username.equals(currentUsername) ? View.VISIBLE : View.GONE));

        gson = new Gson();
        display = new String[4];

        getUser();
        fillText();
    }

    private void fillText() {
        ((TextView) findViewById(R.id.viewProfileUsername)).setText(display[0]);
        ((TextView) findViewById(R.id.viewProfileName)).setText(display[1]);
        ((TextView) findViewById(R.id.viewProfileCountry)).setText(display[2]);
        ((TextView) findViewById(R.id.viewProfileLanguage)).setText(display[3]);
    }

    private void getUser() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://cjdesktop.rh.rit.edu/penpals?action=retrieveinfo&username=" + username;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        User user = ViewProfileActivity.gson.fromJson(response, User.class);
                        ViewProfileActivity.display[0] = user.getUsername();
                        ViewProfileActivity.display[1] = user.getFirstname() + " " + user.getLastname();
                        ViewProfileActivity.display[2] = user.getCountry();
                        ViewProfileActivity.display[3] = user.getLang();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void goToEditProfile(View view) {
        Intent newActivity = new Intent(this, EditProfileActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
        finish();
    }

    public void goToMainPageActivity() {
        Intent newActivity = new Intent(this, MainPageActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
        finish();
    }

    public void goToInterestActivity(View view) {
        Intent newActivity = new Intent(this, InterestActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
        finish();
    }

}
