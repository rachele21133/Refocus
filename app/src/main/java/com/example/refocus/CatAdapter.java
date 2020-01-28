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

public class CatAdapter extends ArrayAdapter<Category> {
    Context context;
    private ArrayList<Category> categories;


    public CatAdapter(Context context, ArrayList<Category> cats) {
        super(context, R.layout.cat_list_item, cats);
        this.categories = cats;
        this.context = context;

    }

    /*private view holder class*/
    private class ViewHolder {
        TextView catName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Category cat = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cat_list_item, parent, false);
            viewHolder.catName = (TextView) convertView.findViewById(R.id.category);

            result=convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.catName.setText(cat.getName());

        // Return the completed view to render on screen
        return result;
    }

    @Override
    public int getCount() { return categories.size(); }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.indexOf(getItem(position));
    }
}
