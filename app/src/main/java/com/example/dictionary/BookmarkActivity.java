package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class BookmarkActivity extends AppCompatActivity {

    MenuItem menuSetting;
    private DBHelper dbHelper;
    ListView bookmarkList;
    BookmarkAdapter bookmarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bookmark);

        dbHelper = new DBHelper(this);
        bookmarkList = (ListView) findViewById(R.id.bookmarkList);
        bookmarkAdapter = new BookmarkAdapter(this, dbHelper.getAllWordFromBookmark());
        bookmarkList.setAdapter(bookmarkAdapter);
        Intent intentFromMain = getIntent();
        String dicType = intentFromMain.getStringExtra("dic_type");

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
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            dbHelper.clearBookmark();
            bookmarkAdapter.clear();
            bookmarkAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}