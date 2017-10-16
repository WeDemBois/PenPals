package com.github.wedemboys.penpals;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.github.wedemboys.penpals.R.id.countrySpinner;
import static com.github.wedemboys.penpals.R.id.editCountrySpinner;
import static com.github.wedemboys.penpals.R.id.editLanguageSpinner;
import static com.github.wedemboys.penpals.R.id.languageSpinner;

public class EditProfileActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner[] spinners = new Spinner[2];
    private EditText[] textBoxes = new EditText[2];
    private MultiSpinner interestSpinner = new MultiSpinner(this);
    private String[] changes = new String[4];
    private Gson gson = new Gson();
    private Button applyChanges;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        spinners[0] = ((Spinner) findViewById(editCountrySpinner));
        spinners[1] = ((Spinner) findViewById(editLanguageSpinner));

        textBoxes[0] = ((EditText) findViewById(R.id.editText)); //firstname
        textBoxes[1] = ((EditText) findViewById(R.id.editText2)); //lastname

        interestSpinner.setItems(readInterests());

        for (int i = 0; i < spinners.length; i++) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this,
                    android.R.layout.simple_spinner_item, RegistrationActivity.options.get(i));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners[i].setAdapter(adapter);
            spinners[i].setOnItemSelectedListener(this);
        }
    }

    public void onClickApplyChanges(View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        fillChanges();
        user = new User("tester", "jd1984", "johndoe@test.com", changes[0], changes[1], changes[2], changes[3], "male", "");
        String json = gson.toJson(user);

        String url = "http://cjdesktop.rh.rit.edu/penpals?action=changeinfo&user=" + json;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
    //when APPLY CHANGES is clicked
    /*
        get values from editText boxes
        construct user object using the strings from the editText boxes
        String Json = gson.toJsonUsing(User)
        whole url shabang with "action=changeinfo&user=" + Json
     */

    // ?action=retrieveinfo&username=tester

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void fillChanges(){
        changes[0] = textBoxes[0].getText().toString();
        changes[1] = textBoxes[1].getText().toString();
        changes[2] = ((Spinner)(findViewById(R.id.editCountrySpinner))).getSelectedItem().toString();
        changes[3] = ((Spinner)(findViewById(R.id.editLanguageSpinner))).getSelectedItem().toString();
    }

    public String[] readInterests(){
        String file = "interests.txt";
        ArrayList<String> interests = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                interests.add(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("ERROR: unable to read file " + file);
            e.printStackTrace();
        }
        return (String[])interests.toArray();
    }

}
