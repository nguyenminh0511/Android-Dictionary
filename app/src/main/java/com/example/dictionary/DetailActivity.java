package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

        SearchWord searchWord = new SearchWord();
        searchWord.execute(value);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word = new Word(value, searchWord.getDescription());
                int status = (int) btnBookmark.getTag();
                if (status == 0) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_fill);
                    btnBookmark.setTag(1);
                    mDBHelper.addBookmark(word);
                } else if (status == 1) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border);
                    btnBookmark.setTag(0);
                    mDBHelper.removeBookmark(word);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    class SearchWord extends AsyncTask<String, Integer, String> {
        String description;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... value) {
            Word bookmarkWord = mDBHelper.getWordFromBookmark(value[0]);
            Word word;
            if (bookmarkWord != null) {
                word = bookmarkWord;
                publishProgress(R.drawable.ic_bookmark_fill);
                btnBookmark.setTag(1);
            } else {
                word = mDBHelper.getWord(value[0], mDictype);
                publishProgress(R.drawable.ic_bookmark_border);
                btnBookmark.setTag(0);
            }

            description = word.value;
            return description;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            btnBookmark.setImageResource(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            tvWordTranslate.loadDataWithBaseURL(null, s, "text/html",
                    "utf-8", null);
        }

        protected String getDescription() {
            return description;
        }
    }
}