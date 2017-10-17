package com.github.wedemboys.penpals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://cjdesktop.rh.rit.edu/penpals?";

        final String username = ((EditText) findViewById(R.id.loginUsername)).getText().toString();
        System.out.println(username);

        url += "action=login";
        url += "&username=" + username;
        url += "&password=" + ((EditText) findViewById(R.id.loginPassword)).getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Response is: " + response);
                        if (!response.equals("success")) {
                            ((TextView) findViewById(R.id.loginErrorMessage)).setText(getResources().getString(R.string.loginError));
                        } else {
                            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.USERNAME_KEY, username);
                            editor.commit();
                            goToMainPage();
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
    }

    public void goToMainPage() {
        Intent newActivity = new Intent(this, MainPageActivity.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
