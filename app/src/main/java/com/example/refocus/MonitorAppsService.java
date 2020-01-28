package com.example.refocus;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import static android.support.v4.app.ActivityCompat.startActivityForResult;


public class MonitorAppsService extends IntentService {

    int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1;

    public MonitorAppsService() {
        super("MonitorAppsService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        View view = new View(this);
        String usage_hours_string, usage_minutes_string = "";

        // Gets data from the incoming Intent
        ArrayList<App> app_array = workIntent.getParcelableArrayListExtra("apps");

        int pos = workIntent.getIntExtra("pos", 0);

        while (true) {
            int c = 0;

            while (c < app_array.size()) {
                // gets usage stats
                AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
                int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());

                if (mode == AppOpsManager.MODE_ALLOWED) {
                    UsageStatsManager usageStatsManager = (UsageStatsManager) view.getContext().getSystemService(Context.USAGE_STATS_SERVICE);

                    long end = System.currentTimeMillis();
                    long start = end - 1000*3600*3;
                    Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(start, end);

                    String pname = app_array.get(c).getPackageName();

                    if (stats.containsKey(pname)) {
                        long totalTimeUsageInMillis = stats.get(pname).getTotalTimeInForeground();

                        long usageTimeMinutes = (totalTimeUsageInMillis / 1000) / 60;

                        double hours = usageTimeMinutes / 60;
                        hours = Math.floor(hours);
                        int hrs = (int) hours;
                        usage_hours_string = Integer.toString(hrs);
                        int minutes = ((int )usageTimeMinutes) % 60;
                        usage_minutes_string = Integer.toString(minutes);
                        app_array.get(c).setUsageHours(usage_hours_string);
                        app_array.get(c).setUsageMinutes(usage_minutes_string);
                    }
                }

                else {
                    //startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                }

                c++;
            }
            Intent broadcastIntent = new Intent();
            //broadcastIntent.setAction(MainActivity.mBroadcastArrayListAction);
            broadcastIntent.putExtra("apps", app_array);
            broadcastIntent.putExtra("pos", pos);
            sendBroadcast(broadcastIntent);
        }
    }
}

