package com.github.wedemboys.penpals;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class NewChatActivity extends Activity {

    EditText recipients;
    EditText chatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        recipients = (EditText) findViewById(R.id.newChatRecipients);
        chatText = (EditText) findViewById(R.id.newChatMessageContent);
    }

    public void createChat(View view) {
        String to = recipients.getText().toString();
        String content = chatText.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        Gson gson = new Gson();
        Message message = new Message(getIntent().getStringExtra(Constants.USER_INTENT_KEY), to, content);
        String messageJson = gson.toJson(message);
        System.out.println(messageJson);
        String url = "http://cjdesktop.rh.rit.edu/penpals?action=sendmessage&message=" + messageJson;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("Response is: " + response);
                        if (!response.equals("success")){
                        } else {
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
}
