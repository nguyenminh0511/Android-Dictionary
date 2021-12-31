package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView tvWord;
    private ImageButton btnBookmark, btnVolumn, btnBack;
    private WebView tvWordTranslate;
    private DBHelper mDBHelper;
    private int mDictype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        tvWord = (TextView) findViewById(R.id.word);
        tvWordTranslate = (WebView) findViewById(R.id.wordTranslate);
        btnBookmark = (ImageButton) findViewById(R.id.bookmarkBtn);
        btnVolumn = (ImageButton) findViewById(R.id.volumnBtn);
        btnBack = (ImageButton) findViewById(R.id.backBtn);
        mDBHelper = new DBHelper(this);

        Intent intent = getIntent();
        String value = intent.getStringExtra("word");
        mDictype = Integer.valueOf(intent.getStringExtra("dic_type"));
        tvWord.setText(value);

        String description = getWord(value, mDBHelper, mDictype);
        tvWordTranslate.loadDataWithBaseURL(null, description, "text/html",
                "utf-8", null);
    }

    String getWord(String value, DBHelper mDBHelper, int dicType) {
        Word bookmarkWord = mDBHelper.getWordFromBookmark(value);
        Word word;
        if (bookmarkWord != null) {
            word = bookmarkWord;
        } else {
            word = mDBHelper.getWord(value, dicType);
        }

        return word.value;
    }
}