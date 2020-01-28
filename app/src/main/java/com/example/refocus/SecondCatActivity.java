package com.example.refocus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import static java.sql.Types.NULL;

public class SecondCatActivity extends AppCompatActivity implements OnItemSelectedListener {

    String selectedValue = "";
    String hoursSelectedValue;
    String minsSelectedValue;

    App app = new App();

    private Button buttonname;

    Bundle bundle;

    int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1;

    String package_name;

    String cname, chours, cmins;

    Category cat = new Category();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_cat);

        buttonname = (Button) findViewById(R.id.submit_button2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = getIntent().getExtras();

        final int pos = bundle.getInt("position");

        cat = (Category) getIntent().getExtras().getParcelable("cat");


        if ((bundle.getString("cat_name")!= null))
        {
            cname = bundle.getString ("cat_name");

            TextView textView = (TextView) findViewById(R.id.cat_title);
            textView.setText(cname);

            chours = bundle.getString ("cat_hrs");
            TextView hoursTextView = (TextView) findViewById(R.id.hoursTextView);
            hoursTextView.setText(chours);

            cmins = bundle.getString("cat_mins");
            TextView minsTextView = (TextView) findViewById(R.id.minsTextView);
            minsTextView.setText(cmins);
        }

        Spinner hours_spinner = (Spinner) findViewById(R.id.hours_spinner);
        Spinner mins_spinner = (Spinner) findViewById(R.id.minutes_spinner);

        hours_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.hours_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        hours_spinner.setAdapter(adapter2);

        mins_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.minutes_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mins_spinner.setAdapter(adapter3);

        buttonname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("cat2",cat);
                returnIntent.putExtra("cat_name",cname);
                returnIntent.putExtra("position2",pos);
                returnIntent.putExtra("hrs",hoursSelectedValue);
                returnIntent.putExtra("mins",minsSelectedValue);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        int id2 = parent.getId();

        switch (id2) {
            case R.id.hours_spinner:
                hoursSelectedValue = (String) parent.getItemAtPosition(pos);
                cat.setHours(hoursSelectedValue);
                break;
            case R.id.minutes_spinner:
                minsSelectedValue = (String) parent.getItemAtPosition(pos);
                cat.setMinutes(minsSelectedValue);
                break;

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}