package com.example.dictionary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dictionary.Language;
import com.example.dictionary.R;
import com.example.dictionary.translateAPI;

public class OnlineTranslate extends AppCompatActivity {

    EditText text;
    String fromLangCode = "en";
    String toLangCode = "vi";
    TextView translatedText;
    Button btnTranslate;
    Spinner fromLang, toLang;
    ArrayAdapter<String> fromAdapter, toAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_translate);

        text = findViewById(R.id.textInput);
        fromLang = findViewById(R.id.fromLanguage);
        toLang = findViewById(R.id.toLanguage);
        translatedText = findViewById(R.id.translated_text);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateAPI translate = new translateAPI();
                translate.setOnTranslationCompleteListener(new translateAPI.OnTranslationCompleteListener() {
                    @Override
                    public void onStartTranslation() {
                        // here you can perform initial work before translated the text like displaying progress bar
                    }

                    @Override
                    public void onCompleted(String text) {
                        // "text" variable will give you the translated text
                        translatedText.setText(text);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                translate.execute(text.getText().toString(),fromLangCode,toLangCode);
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

}