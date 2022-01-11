package com.example.dictionary.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.dictionary.R;
import com.example.dictionary.translateAPI;

public class OnlineTranslate extends AppCompatActivity {

    EditText text,fromLangCode,toLangCode;
    TextView translatedText;
    Button btnTranslate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_translate);

        text = findViewById(R.id.textInput);
        fromLangCode = findViewById(R.id.from_lang);
        toLangCode = findViewById(R.id.to_lang);
        translatedText = findViewById(R.id.translated_text);
        btnTranslate = findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateAPI translate=new translateAPI();
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
                translate.execute(text.getText().toString(),fromLangCode.getText().toString(),toLangCode.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //    private void initUi() {
//        text=findViewById(R.id.textInput);
//        fromLangCode=findViewById(R.id.from_lang);
//        toLangCode=findViewById(R.id.to_lang);
//        translatedText=findViewById(R.id.translated_text);
//        btnTranslate=findViewById(R.id.btnTranslate);
//    }
}