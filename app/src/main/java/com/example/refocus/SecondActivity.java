package com.example.refocus;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
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

import static java.security.AccessController.getContext;
import static java.sql.Types.NULL;

import android.app.Notification;
import android.app.NotificationManager;

public class SecondActivity extends AppCompatActivity implements OnItemSelectedListener {

    String selectedValue = "";
    String hoursSelectedValue;
    String minsSelectedValue;

    App app = new App();

    private Button buttonname;

    Bundle bundle;

    String package_name;

    String usage_hours_string, usage_minutes_string;

    int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1;

    ArrayList<App> apps;

    String mins_string, hours_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        buttonname = (Button) findViewById(R.id.submit_button);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app = (App) getIntent().getExtras().getParcelable("app");
        apps = getIntent().getExtras().getParcelableArrayList("apps");

        bundle = getIntent().getExtras();

        final int pos = bundle.getInt("position");


        if ((bundle.getString("title")!= null) && (bundle.getString("category")!= null))
        {
            String title = bundle.getString ("title");
            package_name = bundle.getString("pname");
            String cat = bundle.getString ("category");

            TextView textView = (TextView) findViewById(R.id.app_title);
            textView.setText(title);

            TextView textView2 = (TextView) findViewById(R.id.app_category2);
            textView2.setText(cat);

            hours_string = bundle.getString("hours");
            TextView hoursTextView = (TextView) findViewById(R.id.hoursTextView);
            hoursTextView.setText(hours_string);

            mins_string = bundle.getString("minutes");
            TextView minsTextView = (TextView) findViewById(R.id.minsTextView);
            minsTextView.setText(mins_string);

            usage_hours_string = bundle.getString("uhours");
            TextView usageHoursTextView = (TextView) findViewById(R.id.usage_hrs);
            usageHoursTextView.setText(usage_hours_string);

            usage_minutes_string = bundle.getString("uminutes");
            TextView usageMinutesTextView = (TextView) findViewById(R.id.usage_minutes);
            usageMinutesTextView.setText(usage_minutes_string);
        }

        String usage_hrs = usage_hours_string;
        int usage = Integer.parseInt(usage_hrs);
        usage = usage * 60;
        String usage_mins = usage_minutes_string;
        int usage_min = Integer.parseInt(usage_mins);
        usage = usage + usage_min;

        String hrs = hours_string;
        int limit = Integer.parseInt(hrs);
        limit = limit * 60;
        String mins = mins_string;
        int min_limit = Integer.parseInt(mins);
        limit = limit + min_limit;

        int diff = limit - usage;
        int diff_hrs = diff / 60;
        int diff_mins = diff % 60;
        String diff_hrs_string = Integer.toString(diff_hrs);
        String diff_mins_string = Integer.toString(diff_mins);

        String name = app.getName();


        if (usage > limit) {
            String result = "You have reached your usage limit";
            TextView result2 = (TextView) findViewById(R.id.result);
            result2.setText(result);
        }

        else {
            String result = "You have " + diff_hrs_string + " hours and " + diff_mins_string + " minutes of usage time left";
            TextView result2 = (TextView) findViewById(R.id.result2);
            result2.setText(result);
        }


        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        Spinner hours_spinner = (Spinner) findViewById(R.id.hours_spinner);
        Spinner mins_spinner = (Spinner) findViewById(R.id.minutes_spinner);

        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        hours_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.hours_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        hours_spinner.setAdapter(adapter2);

        mins_spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.minutes_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mins_spinner.setAdapter(adapter3);

        buttonname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // gets usage stats
                AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());

                if (mode == AppOpsManager.MODE_ALLOWED) {
                    UsageStatsManager usageStatsManager = (UsageStatsManager) view.getContext().getSystemService(Context.USAGE_STATS_SERVICE);

                    long end = System.currentTimeMillis();
                    long start = end - 1000*3600*3;
                    Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(start, end);

                    String pname = app.getPackageName();

                    if (stats.containsKey(pname)) {
                        long totalTimeUsageInMillis = stats.get(pname).getTotalTimeInForeground();

                        long usageTimeMinutes = (totalTimeUsageInMillis / 1000) / 60;

                        double hours = usageTimeMinutes / 60;
                        hours = Math.floor(hours);
                        int hrs = (int) hours;
                        usage_hours_string = Integer.toString(hrs);
                        int minutes = ((int )usageTimeMinutes) % 60;
                        usage_minutes_string = Integer.toString(minutes);
                        app.setUsageHours(usage_hours_string);
                        app.setUsageMinutes(usage_minutes_string);
                    }
                }

                else {
                    startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                }

                app.setUsageHours(usage_hours_string);
                app.setUsageMinutes(usage_minutes_string);

                TextView usageHoursTextView = (TextView) findViewById(R.id.usage_hrs);
                usageHoursTextView.setText(usage_hours_string);

                TextView usageMinutesTextView = (TextView) findViewById(R.id.usage_minutes);
                usageMinutesTextView.setText(usage_minutes_string);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",app);
                returnIntent.putExtra("position2",pos);
                returnIntent.putExtra("hrs",hoursSelectedValue);
                returnIntent.putExtra("mins",minsSelectedValue);
                returnIntent.putExtra("uhrs",usage_hours_string);
                returnIntent.putExtra("umins",usage_minutes_string);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        int id2 = parent.getId();

        switch (id2) {
            case R.id.spinner1:
                selectedValue = (String) parent.getItemAtPosition(pos);
                app.setCategory(selectedValue);
                app.setPackageName(package_name);

                break;
            case R.id.hours_spinner:
                hoursSelectedValue = (String) parent.getItemAtPosition(pos);
                app.setHours(hoursSelectedValue);
                break;
            case R.id.minutes_spinner:
                minsSelectedValue = (String) parent.getItemAtPosition(pos);
                app.setMinutes(minsSelectedValue);
                break;

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void createNotificationChannel() {
// Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel id", name,
                    importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviours after this
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
