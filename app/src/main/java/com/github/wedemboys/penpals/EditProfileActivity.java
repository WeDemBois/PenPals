package com.github.wedemboys.penpals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.github.wedemboys.penpals.R.id.countrySpinner;
import static com.github.wedemboys.penpals.R.id.editCountrySpinner;
import static com.github.wedemboys.penpals.R.id.editLanguageSpinner;
import static com.github.wedemboys.penpals.R.id.languageSpinner;

public class EditProfileActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner[] spinners = new Spinner[2];
    private EditText[] textBoxes = new EditText[2];
    private MultiSpinner interestSpinner;
    private String[] changes = new String[4];
    private Gson gson = new Gson();
    private Button applyChanges;
    public User user;
    public ArrayList<String[]> options = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        interestSpinner = (MultiSpinner) findViewById(R.id.interestSpinner);

        spinners[0] = ((Spinner) findViewById(editCountrySpinner));
        spinners[1] = ((Spinner) findViewById(editLanguageSpinner));

        textBoxes[0] = ((EditText) findViewById(R.id.editText)); //firstname
        textBoxes[1] = ((EditText) findViewById(R.id.editText2)); //lastname

        String[] array = readInterests();
        System.out.println(array);
        interestSpinner.setItems(readInterests());

        InputStream countriesInputStream = getResources().openRawResource(R.raw.countries);
        ReadCSVFile countries = new ReadCSVFile(countriesInputStream);
        List countryOptions = countries.read();
        String[] strArrCountries = new String[(countryOptions.size() / 2) + 1];
        strArrCountries[0] = ((String[]) (countryOptions.get(0)))[0].substring(2);
        for (int i = 2; i < countryOptions.size(); i += 2) {
            strArrCountries[i / 2] = ((String[]) (countryOptions.get(i)))[0];
        }
        options.add(strArrCountries);

        String[] languageOptions = {"English"};
        options.add(languageOptions);

        String[] genderOptions = {"Male", "Female", "Other"};
        options.add(genderOptions);

        for (int i = 0; i < spinners.length; i++) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this,
                    android.R.layout.simple_spinner_item, options.get(i));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners[i].setAdapter(adapter);
            spinners[i].setOnItemSelectedListener(this);
        }
    }

    public void onClickApplyChanges(View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        fillChanges();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        user = new User(username, "", "", changes[0], changes[1], changes[2], changes[3], "", "");
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
        goToMainPage();

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
        ArrayList<String> interests = new ArrayList<String>();
        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.interests));
        while (scanner.hasNext()) {
            interests.add(scanner.nextLine());
        }
        return interests.toArray(new String[interests.size()]);
    }

    public void goToMainPage() {
        Intent newActivity = new Intent(this, MainPageActivity.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
    }

}
