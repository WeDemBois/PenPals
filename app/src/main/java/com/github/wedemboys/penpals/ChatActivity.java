package com.github.wedemboys.penpals;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ChatActivity extends Activity {

    String withUser;
    EditText chatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_chat, null);

//        withUser = getIntent().getStringExtra(Constants.USER_INTENT_KEY);
        chatText = (EditText) findViewById(R.id.chatViewChatText);
//
//        //set name in title bar
//        System.out.println(findViewById(R.id.chatUserName));
//        ((TextView) findViewById(R.id.chatUserName)).setText(withUser);
//
//        //load previous chats
//        File previousChats = new File(getFilesDir(), Constants.STORED_CHATS_FILE_PREFIX + withUser);
//        try {
//            Scanner scanner = new Scanner(previousChats);
//            Gson gson = new Gson();
//            while(scanner.hasNext()) {
//                String chatJson = scanner.nextLine();
//                Message chat = gson.fromJson(chatJson, Message.class);
//                if (chat.getSender().equals(withUser)) {
//                    //initialize chats on left side (received)
//                } else {
//                    //initialize chats on right side (sent)
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        setContentView(view);
    }

    public void sendChat(View view) {
        String content = chatText.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        Gson gson = new Gson();
        Message message = new Message(getIntent().getStringExtra(Constants.USER_INTENT_KEY), withUser, content);
        String messageJson = gson.toJson(message);
        System.out.println(messageJson);
        String url = "http://cjdesktop.rh.rit.edu/penpals?action=sendmessage&message=" + messageJson;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
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

    public void goToMainPage(View view) {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
