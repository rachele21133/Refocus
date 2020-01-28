package com.example.refocus;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<App> {
    Context context;
    private ArrayList<App> apps;


    public MyAdapter(Context context, ArrayList<App> apps) {
        super(context, R.layout.list_item, apps);
        this.apps = apps;
        this.context = context;

    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtName;
        TextView txtCategory;
        public ImageView icon;
        TextView usageHours;
        TextView usageMins;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        App app = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.title);
            viewHolder.txtCategory = (TextView) convertView.findViewById(R.id.subtitle);
            viewHolder.usageHours = (TextView) convertView.findViewById(R.id.usage_hours);
            viewHolder.usageMins = (TextView) convertView.findViewById(R.id.usage_mins);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);

            result=convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.txtName.setText(app.getName());
        viewHolder.txtCategory.setText(app.getCategory());
        viewHolder.usageHours.setText(app.getUsageHours());
        viewHolder.usageMins.setText(app.getUsageMinutes());

        // set each app's icon
        if (position == 0) {
            viewHolder.icon.setImageResource(R.drawable.camera);
        }

        else if (position == 1) {
            viewHolder.icon.setImageResource(R.drawable.chrome);
        }

        else if (position == 2) {
            viewHolder.icon.setImageResource(R.drawable.google);
        }

        else if (position == 3) {
            viewHolder.icon.setImageResource(R.drawable.messages);
        }

        else if (position == 4) {
            viewHolder.icon.setImageResource(R.drawable.phone);
        }

        else if (position == 5) {
            viewHolder.icon.setImageResource(R.drawable.refocus);
        }

        else if (position == 6) {
            viewHolder.icon.setImageResource(R.drawable.smart_fitness);
        }

        else if (position == 7){
            viewHolder.icon.setImageResource(R.drawable.youtube);
        }

        else {
            viewHolder.icon.setImageResource((R.drawable.icon));
        }

        // Return the completed view to render on screen
        return result;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public App getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return apps.indexOf(getItem(position));
    }
}
