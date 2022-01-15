package com.example.dictionary.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.dictionary.Global;
import com.example.dictionary.Language;
import com.example.dictionary.R;
import com.example.dictionary.translateAPI;
import com.google.android.material.navigation.NavigationView;

public class OnlineTranslate extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    EditText text;
    String fromLangCode = "en";
    String toLangCode = "vi";
    TextView translatedText, connectionMessage;
    Button btnTranslate;
    Spinner fromLang, toLang;
    ArrayAdapter<String> fromAdapter, toAdapter;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    String dicType;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_main);

        Toolbar toolbar = findViewById(R.id.online_toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intentFromMain = getIntent();
        dicType = intentFromMain.getStringExtra("dic_type");

        progressBar = findViewById(R.id.progressBarOnlineTranslating);
        progressBar.setVisibility(View.GONE);

        text = findViewById(R.id.textInput);
        connectionMessage = findViewById(R.id.connectionMessage);
        connectionMessage.setVisibility(View.GONE);
        fromLang = findViewById(R.id.fromLanguage);
        toLang = findViewById(R.id.toLanguage);
        translatedText = findViewById(R.id.translated_text);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkAvailable()) {
                    connectionMessage.setVisibility(View.VISIBLE);
                } else {
                    connectionMessage.setVisibility(View.GONE);
                    translateAPI translate = new translateAPI();
                    translate.setOnTranslationCompleteListener(new translateAPI.OnTranslationCompleteListener() {
                        @Override
                        public void onStartTranslation() {
                            // here you can perform initial work before translated the text like displaying progress bar
                            progressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCompleted(String text) {
                            // "text" variable will give you the translated text
                            translatedText.setText(text);
                            progressBar.setVisibility(View.GONE);
                            connectionMessage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            connectionMessage.setVisibility(View.VISIBLE);
                            connectionMessage.setText("Error!!! Can't translate this paragraph!");
                        }
                    });
                    translate.execute(text.getText().toString(), fromLangCode, toLangCode);
                }
            }
        });

        Language.initLanguage();
        fromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Language.languageNames());
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Language.languageNames());
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromLang.setAdapter(fromAdapter);
        toLang.setAdapter(toAdapter);

        fromLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = fromAdapter.getItem(position);
                fromLangCode = Language.getCodeFromName(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = toAdapter.getItem(position);
                toLangCode = Language.getCodeFromName(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_back_home) {
            onBackPressed();
        } else if (id == R.id.nav_bookmark) {
            Intent intentToBookmark = new Intent(OnlineTranslate.this, BookmarkActivity.class);
            intentToBookmark.putExtra("dic_type", dicType);
            finish();
            startActivity(intentToBookmark);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}