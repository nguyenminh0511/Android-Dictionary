package com.example.dictionary;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    MenuItem menuSetting;
    DictionaryFragment dictionaryFragment;
    BookmarkFragment bookmarkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close){};
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dictionaryFragment = new DictionaryFragment();
        bookmarkFragment = new BookmarkFragment();
        goToFragment(dictionaryFragment, true);

        dictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                goToFragment(DetailFragment.getNewInstance(value), false);
            }
        });

        bookmarkFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                goToFragment(DetailFragment.getNewInstance(value), false);
            }
        });

        EditText edit_search = findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dictionaryFragment.filterValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting = menu.findItem(R.id.action_settings);

        String id = Global.getState(this, "dic_type");

        if (id != null) {
            onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));
        } else {
            dictionaryFragment.resetDataSource(DB.getData(R.id.action_eng_vn));
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
        Global.saveState(this, "dic_type", String.valueOf(id));

        String[] source = DB.getData(id);

        if (id == R.id.action_eng_vn) {
            dictionaryFragment.resetDataSource(source);
            menuSetting.setIcon(getDrawable(R.drawable.english_vietnamese_1));
        } else if (id == R.id.action_vn_eng) {
            dictionaryFragment.resetDataSource(source);
            menuSetting.setIcon(getDrawable(R.drawable.vietnamese_english_1));
        } else if (id == R.id.action_eng_eng) {
            dictionaryFragment.resetDataSource(source);
            menuSetting.setIcon(getDrawable(R.drawable.english_english_1));
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StateWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_bookmark) {
            goToFragment(bookmarkFragment, false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void goToFragment(Fragment fragment, boolean isTop) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);

        fragmentTransaction.replace(R.id.fragment_container, fragment, null);

        if (!isTop) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}