package com.github.wedemboys.penpals;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Scanner;

public class InterestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_interest, null);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.interestScrollView);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.interests));
        while (scanner.hasNext()) {
            String interest = scanner.nextLine();
            RelativeLayout interestLayout = new RelativeLayout(this);
            TextView interestText = new TextView(this);
            interestText.setText(interest);
            CheckBox checkBox = new CheckBox(this);
            interestLayout.addView(interestText);
            interestLayout.addView(checkBox);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) checkBox.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            linearLayout.addView(interestLayout);
        }
        scrollView.addView(linearLayout);
        setContentView(view);
    }

    public void submitInterests(View view) {

    }
}
