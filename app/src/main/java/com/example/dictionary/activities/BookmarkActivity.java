package com.example.dictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.dictionary.BookmarkAdapter;
import com.example.dictionary.DBHelper;
import com.example.dictionary.ListItemListener;
import com.example.dictionary.R;
import com.google.android.material.navigation.NavigationView;

public class BookmarkActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MenuItem menuSetting;
    private DBHelper dbHelper;
    ListView bookmarkList;
    BookmarkAdapter bookmarkAdapter;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bookmark);

        dbHelper = new DBHelper(this);
        bookmarkList = (ListView) findViewById(R.id.bookmarkList);
        bookmarkAdapter = new BookmarkAdapter(BookmarkActivity.this, dbHelper.getAllWordFromBookmark());
        bookmarkList.setAdapter(bookmarkAdapter);

        Intent intentFromMain = getIntent();
        String dicType = intentFromMain.getStringExtra("dic_type");

        Toolbar toolbar = findViewById(R.id.bookmark_toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close){};
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        bookmarkAdapter.setOnItemCLick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                String value = String.valueOf(bookmarkAdapter.getItem(position));
                Intent intentToDetail = new Intent(BookmarkActivity.this, DetailActivity.class);
                intentToDetail.putExtra("word", value);
                intentToDetail.putExtra("dic_type", dicType);
                startActivity(intentToDetail);
            }
        });

        bookmarkAdapter.setOnItemDeleteClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                String value = String.valueOf(bookmarkAdapter.getItem(position));
                Toast.makeText(BookmarkActivity.this, value + " is removed",
                        Toast.LENGTH_SHORT).show();
                dbHelper.removeBookmark(value);
                bookmarkAdapter.removeItem(position);
                bookmarkAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        menuSetting = menu.findItem(R.id.action_clear);

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            dbHelper.clearBookmark();
            bookmarkAdapter.clear();
            bookmarkAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_back_home) {
            onBackPressed();
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}