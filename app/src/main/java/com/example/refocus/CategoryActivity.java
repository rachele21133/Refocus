package com.example.refocus;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Category> cat_array = new ArrayList<Category>();
    Category category = new Category();
    Category cat2 = new Category();

    int pos;

    String hours_string2, mins_string2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        int i = 0;

        category.setName("Art and Design");
        cat_array.add(category);
        Category category1 = new Category();
        category1.setName("Beauty");
        cat_array.add(category1);
        Category category2 = new Category();
        category2.setName("Books and Reference");
        cat_array.add(category2);
        Category category3 = new Category();
        category3.setName("Comics");
        cat_array.add(category3);
        Category category4 = new Category();
        category4.setName("Communication");
        cat_array.add(category4);
        Category category5 = new Category();
        category5.setName("Dating");
        cat_array.add(category5);
        Category category6 = new Category();
        category6.setName("Education");
        cat_array.add(category6);
        Category category7 = new Category();
        category7.setName("Entertainment");
        cat_array.add(category7);
        Category category8 = new Category();
        category8.setName("Games");
        cat_array.add(category8);
        Category category9 = new Category();
        category9.setName("Health and Fitness");
        cat_array.add(category9);
        Category category10 = new Category();
        category10.setName("Music and Audio");
        cat_array.add(category10);
        Category category11 = new Category();
        category11.setName("News and Magazine");
        cat_array.add(category11);
        Category category12 = new Category();
        category12.setName("Photography");
        cat_array.add(category12);
        Category category13 = new Category();
        category13.setName("Shopping");
        cat_array.add(category13);
        Category category14 = new Category();
        category14.setName("Social");
        cat_array.add(category14);
        Category category15 = new Category();
        category15.setName("Sports");
        cat_array.add(category15);
        Category category16 = new Category();
        category16.setName("Video Players");
        cat_array.add(category16);

        listView = (ListView) findViewById(R.id.cat_list);
        CatAdapter cat_adapter = new CatAdapter(this, cat_array);
        listView.setAdapter(cat_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent myIntent = new Intent(view.getContext(), SecondCatActivity.class);
                cat2 = cat_array.get(position);
                String cat_name = cat2.getName();
                String cat_hours = cat2.getHours();
                String cat_mins = cat2.getMinutes();
                myIntent.putExtra("cat_name", cat_name);
                myIntent.putExtra("cat_hrs", cat_hours);
                myIntent.putExtra("cat_mins", cat_mins);
                myIntent.putExtra("cat", cat2);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {

                //cat2 = (Category) getIntent().getExtras().getParcelable("cat2");

                pos = data.getIntExtra("position2", 0);

                cat2 = cat_array.get(pos);

                hours_string2 = data.getExtras().getString("hrs");

                mins_string2 = data.getExtras().getString("mins");

                cat2.setHours(hours_string2);
                cat2.setMinutes(mins_string2);

                cat_array.set(pos, cat2);

                listView = (ListView) findViewById(R.id.cat_list);
                CatAdapter cat_adapter = new CatAdapter(this, cat_array);
                listView.setAdapter(cat_adapter);

                listView.smoothScrollToPosition(pos);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent myIntent = new Intent(view.getContext(), SecondCatActivity.class);
                        cat2 = cat_array.get(position);
                        String cat_name = cat2.getName();
                        String cat_hours = cat2.getHours();
                        String cat_mins = cat2.getMinutes();
                        myIntent.putExtra("cat_name", cat_name);
                        myIntent.putExtra("cat_hrs", cat_hours);
                        myIntent.putExtra("cat_mins", cat_mins);
                        myIntent.putExtra("cat", cat2);
                        startActivityForResult(myIntent, 0);
                    }
                });
            }
        }
    }
}
