package com.example.refocus;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String TAG = MainActivity.class.getSimpleName();
    private PackageManager pm;

    ArrayList<App> apps = new ArrayList<App>();

    App app2 = new App();

    ListView listView;

    int pos;

    String hours_string2, mins_string2, usage_hours_string2, usage_mins_string2;

    int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String json = null;
        try {
            InputStream is = this.getAssets().open("data.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        Iterator<ApplicationInfo> iterator = packages.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            ApplicationInfo packageInfo = iterator.next();

            String test_name = (String) pm.getApplicationLabel(packageInfo);

            ArrayList<String> app_array = new ArrayList<String>();
            app_array.add("Refocus");
            app_array.add("Chrome");
            app_array.add("Phone");
            app_array.add("Smart Fitness");
            app_array.add("Camera");
            app_array.add("Messages");
            app_array.add("Google");
            app_array.add("YouTube");

            if (app_array.contains(test_name)) {
                String app_name = (String) pm.getApplicationLabel(packageInfo);
                String package_name = (String) packageInfo.packageName;
                Drawable icon = pm.getApplicationIcon(packageInfo);
                String category = "none";
                App app = new App();
                app.setName(app_name);
                app.setPackageName(package_name);
                app.setCategory(category);
                app.setImage(icon);
                app.setHours("0");
                app.setMinutes("0");
                apps.add(i, app);
            }
        }

        Collections.sort(apps);

        View view = new View(this);

        int k = 0;
        while (k < apps.size()) {
            // testing stat usage manager here
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());

            if (mode == AppOpsManager.MODE_ALLOWED) {
                UsageStatsManager usageStatsManager = (UsageStatsManager) view.getContext().getSystemService(Context.USAGE_STATS_SERVICE);

                long end = System.currentTimeMillis();
                long start = end - 1000*3600*3;
                Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(start, end);

                String pname = apps.get(k).getPackageName();

                if (stats.containsKey(pname)) {
                    long totalTimeUsageInMillis = stats.get(pname).getTotalTimeInForeground();

                    long usageTimeMinutes = (totalTimeUsageInMillis / 1000) / 60;

                    double hours = usageTimeMinutes / 60;
                    hours = Math.floor(hours);
                    int hrs = (int) hours;
                    String hours_string = Integer.toString(hrs);
                    int minutes = ((int )usageTimeMinutes) % 60;
                    String minutes_string = Integer.toString(minutes);
                    apps.get(k).setUsageHours(hours_string);
                    apps.get(k).setUsageMinutes(minutes_string);
                }
            }

            else {
                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }

            k++;
        }

        listView = (ListView) findViewById(R.id.list);
        MyAdapter adapter = new MyAdapter(this, apps);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(view.getContext(), SecondActivity.class);
                //myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                app2 = apps.get(position);
                String title = app2.getName();
                String category = app2.getCategory();
                String hours = app2.getHours();
                String usage_hours = app2.getUsageHours();
                String mins = app2.getMinutes();
                String usage_mins = app2.getUsageMinutes();
                String pname = app2.getPackageName();
                myIntent.putExtra("title", title);
                myIntent.putExtra("pname", pname);
                myIntent.putExtra("category", category);
                myIntent.putExtra("position", position);
                myIntent.putExtra("hours", hours);
                myIntent.putExtra("minutes", mins);
                myIntent.putExtra("uhours", usage_hours);
                myIntent.putExtra("uminutes", usage_mins);
                myIntent.putExtra("app", app2);
                myIntent.putExtra("apps", apps);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            //startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {

                pos = data.getIntExtra("position2", 0);

                hours_string2 = data.getExtras().getString("hrs");

                mins_string2 = data.getExtras().getString("mins");

                usage_hours_string2 = data.getExtras().getString("uhrs");

                usage_mins_string2 = data.getExtras().getString("umins");

                app2 = (App) data.getExtras().getParcelable("result");

                app2.setHours(hours_string2);
                app2.setMinutes(mins_string2);
                app2.setUsageHours(usage_hours_string2);
                app2.setUsageMinutes(usage_mins_string2);

                apps.set(pos, app2);

                View view = new View(this);

                int k = 0;
                while (k < apps.size()) {
                    // testing stat usage manager here
                    AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                    int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());

                    if (mode == AppOpsManager.MODE_ALLOWED) {
                        UsageStatsManager usageStatsManager = (UsageStatsManager) view.getContext().getSystemService(Context.USAGE_STATS_SERVICE);

                        long end = System.currentTimeMillis();
                        long start = end - 1000*3600*3;
                        Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(start, end);

                        String pname = apps.get(k).getPackageName();

                        if (stats.containsKey(pname)) {
                            long totalTimeUsageInMillis = stats.get(pname).getTotalTimeInForeground();

                            long usageTimeMinutes = (totalTimeUsageInMillis / 1000) / 60;

                            double hours = usageTimeMinutes / 60;
                            hours = Math.floor(hours);
                            int hrs = (int) hours;
                            String hours_string = Integer.toString(hrs);
                            int minutes = ((int )usageTimeMinutes) % 60;
                            String minutes_string = Integer.toString(minutes);
                            apps.get(k).setUsageHours(hours_string);
                            apps.get(k).setUsageMinutes(minutes_string);
                        }
                    }

                    else {
                        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                    }
                    k++;
                }

                app2 = apps.get(pos);

                app2.setHours(app2.getHours());
                app2.setMinutes(app2.getMinutes());
                app2.setUsageHours(app2.getUsageHours());
                app2.setUsageMinutes(app2.getUsageMinutes());

                apps.set(pos, app2);

                listView = (ListView) findViewById(R.id.list);

                MyAdapter adapter2 = new MyAdapter(this, apps);
                listView.setAdapter(adapter2);

                listView.smoothScrollToPosition(pos);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent myIntent = new Intent(view.getContext(), SecondActivity.class);
                        app2 = apps.get(position);
                        String title = app2.getName();
                        String category = app2.getCategory();
                        String hours = app2.getHours();
                        String mins = app2.getMinutes();
                        String usage_hours = app2.getUsageHours();
                        String usage_mins = app2.getUsageMinutes();
                        myIntent.putExtra("title", title);
                        myIntent.putExtra("category", category);
                        myIntent.putExtra("position", position);
                        myIntent.putExtra("hours", hours);
                        myIntent.putExtra("minutes", mins);
                        myIntent.putExtra("uhours", usage_hours);
                        myIntent.putExtra("uminutes", usage_mins);
                        myIntent.putExtra("app", app2);
                        startActivityForResult(myIntent, 0);
                    }
                });
            }
        }
    }
}
