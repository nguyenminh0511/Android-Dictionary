package com.example.dictionary.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dictionary.DBHelper;
import com.example.dictionary.Global;
import com.example.dictionary.R;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    MenuItem menuSetting;
    Toolbar toolbar;
    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    ListView dicList;
    ArrayList<String> mSource = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close){};
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dicList = findViewById(R.id.dictionaryList);
        dicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dicType = Global.getState(MainActivity.this, "dic_type");
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("word", mSource.get(position));
                intent.putExtra("dic_type", dicType);
                startActivity(intent);
            }
        });

        EditText edit_search = findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void resetDatabase(ArrayList<String> source) {
        mSource = source;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mSource);
        dicList.setAdapter(adapter);
    }

    public void filterValue(String value) {
//        adapter.getFilter().filter(value);

        int size = adapter.getCount();
        for (int i = 0; i < size; ++i) {
            if (adapter.getItem(i).startsWith(value)) {
                dicList.setSelection(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting = menu.findItem(R.id.action_settings);

        String id = Global.getState(this, "dic_type");

        if (id != null) {
            onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));
        } else {
            ChangeDictType changeDictType = new ChangeDictType();
            changeDictType.execute(R.id.action_eng_vn);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // first time run app with database

        if (id == R.id.action_eng_vn) {
            Global.saveState(this, "dic_type", String.valueOf(id));
            ChangeDictType changeDictType = new ChangeDictType();
            changeDictType.execute(id);
            menuSetting.setIcon(getDrawable(R.drawable.english_vietnamese_1));
            return true;
        } else if (id == R.id.action_vn_eng) {
            Global.saveState(this, "dic_type", String.valueOf(id));
            ChangeDictType changeDictType = new ChangeDictType();
            changeDictType.execute(id);
            menuSetting.setIcon(getDrawable(R.drawable.vietnamese_english_1));
            return true;
        } else if (id == R.id.action_eng_eng) {
            Toast.makeText(this, "Not support yet", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StateWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_bookmark) {
            String dicType = Global.getState(MainActivity.this, "dic_type");
            Intent intentToBookmark = new Intent(MainActivity.this, BookmarkActivity.class);
            intentToBookmark.putExtra("dic_type", dicType);
            startActivity(intentToBookmark);
        } else if (id == R.id.nav_online) {
            Intent intentToOnlineTranslate = new Intent(MainActivity.this, OnlineTranslate.class);
            String dicType = Global.getState(MainActivity.this, "dic_type");
            intentToOnlineTranslate.putExtra("dic_type", dicType);
            startActivity(intentToOnlineTranslate);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class ChangeDictType extends AsyncTask<Integer, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Integer... integers) {
            int id = integers[0];
            ArrayList<String> source = dbHelper.getWord(id);
            return source;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            resetDatabase(strings);
        }
    }

}