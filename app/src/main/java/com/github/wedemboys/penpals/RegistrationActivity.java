package com.github.wedemboys.penpals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.constraint.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.wedemboys.penpals.R.id.*;

public class RegistrationActivity extends Activity implements OnItemSelectedListener {

    private ArrayList<Spinner> spinners = new ArrayList<Spinner>();
    protected static final ArrayList<String[]> options = new ArrayList<String[]>();
    private String[] response = new String[9];
    private EditText[] textBoxes = new EditText[6];

    private PopupWindow popup;
    private LayoutInflater layoutInflater;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        spinners.add((Spinner) findViewById(countrySpinner));
        spinners.add((Spinner) findViewById(languageSpinner));
        spinners.add((Spinner) findViewById(genderSpinner));

        textBoxes[0] = ((EditText) findViewById(R.id.editTextFirstName));
        textBoxes[1] = ((EditText) findViewById(R.id.editTextLastName));
        textBoxes[2] = ((EditText) findViewById(R.id.editTextUsername));
        textBoxes[3] = ((EditText) findViewById(R.id.editTextPassword));
        textBoxes[4] = ((EditText) findViewById(R.id.editTextRetypePassword));
        textBoxes[5] = ((EditText) findViewById(R.id.editTextEmail));

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

        //The below code assumes options and spinners ar ethe same size, make sure they are
        for (int i = 0; i < spinners.size(); i++) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegistrationActivity.this,
                    android.R.layout.simple_spinner_item, options.get(i));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners.get(i).setAdapter(adapter);
            spinners.get(i).setOnItemSelectedListener(this);
        }

        Button regButton = (Button) findViewById(R.id.registerButton);
        constraintLayout = (ConstraintLayout) findViewById(R.id.registerConstraint);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (parent.getId()) {
            case (R.id.countrySpinner):
                response[6] = parent.getItemAtPosition(pos).toString();
                break;
            case (R.id.languageSpinner):
                response[7] = parent.getItemAtPosition(pos).toString();
                break;
            case (R.id.genderSpinner):
                response[8] = parent.getItemAtPosition(pos).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private String checkResponse() {
        String strReturn = "";
        strReturn += (response[0].equals("")) ? "You need a first name\n\n" : "";
        strReturn += (response[1].equals("")) ? "You need a last name\n\n" : "";
        strReturn += (response[2].equals("")) ? "You need a username\n\n" : "";
        strReturn += (!response[3].equals(response[4])) ? "Your passwords need to match\n\n" :
                ((response[3].equals("")) ? "You need a password\n\n" : "");
        strReturn += checkValidEmail(response[5]);
        return strReturn;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void fillResponse() {
        for (int i = 0; i < textBoxes.length; i++){
            response[i] = textBoxes[i].getText().toString();
        }
        response[6] = ((Spinner)(findViewById(R.id.countrySpinner))).getSelectedItem().toString();
        response[7] = ((Spinner)(findViewById(R.id.languageSpinner))).getSelectedItem().toString();
        response[8] = ((Spinner)(findViewById(R.id.genderSpinner))).getSelectedItem().toString();
    }

    public void onClickRegister(View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://cjdesktop.rh.rit.edu/penpals?";
        fillResponse();
        String popupMessage = checkResponse();
        final String username = response[2];
        if (popupMessage.equals("")) {
            url += "action=register";
            url += ("&username=" + response[2]);
            url += ("&password=" + response[3]);
            url += ("&email=" + response[5]);
            url += ("&firstname=" + response[0]);
            url += ("&lastname=" + response[1]);
            url += ("&country=" + response[6]);
            url += ("&lang=" + response[7]);
            url += ("&gender=" + response[8]);
            url += ("&interests=");
//                    url += (response[]);
            //username, password, email, firstname, lastname, country, lang, gender, interests

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            System.out.println("Response is: " + response);
                            if (!response.equals("success")){
                                popup(response);
                            } else {
                                //go to the main activity
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
        } else {
            popup(popupMessage);
        }
    }

    private void popup(String message){
        layoutInflater = (LayoutInflater)
                getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popup, null);

        int popupWidth = 400;
        int popupHeight = 400;
        popup = new PopupWindow(container, popupWidth, popupHeight, true);
        ((TextView) popup.getContentView().findViewById(R.id.textViewPopup))
                .setText(message);
        int x = (getScreenWidth() - popupWidth) / 2;
        int y = (getScreenHeight() - popupHeight) / 2;
        popup.showAtLocation(constraintLayout, Gravity.NO_GRAVITY, x, y);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popup.dismiss();
                return true;
            }
        });
    }

    private String checkValidEmail(String email){
        if (email.length() == 0) return "You need an email";
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        return (pattern.matcher(email).matches()) ? "" : "You need a valid email";
    }

    private void goToMainPage(){
        Intent newActivity = new Intent(this, MainPageActivity.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newActivity);
        finish();
    }

}