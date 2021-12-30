package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment {

    private String value = "";
    private TextView tvWord;
    private ImageButton btnBookmark, btnVolumn, btnBack;
    private WebView tvWordTranslate;
    private DBHelper mDBHelper;
    private int mDictype;
    private FragmentListener listener;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment getNewInstance(String value, DBHelper dbHelper, int dicType) {
        DetailFragment fragment = new DetailFragment();
        fragment.value = value;
        fragment.mDBHelper = dbHelper;
        fragment.mDictype = dicType;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true); // Change option menu when change fragment

        tvWord = (TextView) view.findViewById(R.id.word);
        tvWordTranslate = (WebView) view.findViewById(R.id.wordTranslate);
        btnBookmark = (ImageButton) view.findViewById(R.id.bookmarkBtn);
        btnVolumn = (ImageButton) view.findViewById(R.id.volumnBtn);
        btnBack = (ImageButton) view.findViewById(R.id.backBtn);

//        final Word word = mDBHelper.getWord(value, mDictype);
//        tvWord.setText(word.key);
//        tvWordTranslate.loadDataWithBaseURL(null, word.value, "text/html",
//                "utf-8", null);

        Word bookmarkWord = mDBHelper.getWordFromBookmark(value);
        Word word;
        if (bookmarkWord != null) {
            word = bookmarkWord;
        } else {
            word = mDBHelper.getWord(value, mDictype);
        }

        tvWord.setText(word.key);
        tvWordTranslate.loadDataWithBaseURL(null, word.value, "text/html",
                "utf-8", null);

        int isMark = bookmarkWord == null ? 0 : 1;

        btnBookmark.setTag(isMark);

        int icon = bookmarkWord == null ? R.drawable.ic_bookmark_border : R.drawable.ic_bookmark_fill;
        btnBookmark.setImageResource(icon);

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) btnBookmark.getTag();
                if (i == 0) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_fill);
                    btnBookmark.setTag(1);
                    mDBHelper.addBookmark(word);
                } else if (i == 1) {
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border);
                    btnBookmark.setTag(0);
                    mDBHelper.removeBookmark(word);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    System.out.println("Press back");
                    listener.onItemClick(null);
                }
            }
        });
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }
}