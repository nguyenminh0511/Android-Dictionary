package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BookmarkFragment extends Fragment {

    private FragmentListener listener;
    private DBHelper dbHelper;
    private BookmarkAdapter bookmarkAdapter;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    public static BookmarkFragment getNewInstance(DBHelper dbHelper) {
        BookmarkFragment fragment = new BookmarkFragment();
        fragment.dbHelper = dbHelper;
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
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        setHasOptionsMenu(true); // Change option menu when change fragment

        ListView bookmarkList = (ListView) view.findViewById(R.id.bookmarkList);
        bookmarkAdapter = new BookmarkAdapter(getActivity(), dbHelper.getAllWordFromBookmark());
        bookmarkList.setAdapter(bookmarkAdapter);

        bookmarkAdapter.setOnItemCLick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                if (listener != null) {
                    listener.onItemClick(String.valueOf(bookmarkAdapter.getItem(position)));
                }
            }
        });

        bookmarkAdapter.setOnItemDeleteClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                String value = String.valueOf(bookmarkAdapter.getItem(position));
                Toast.makeText(getContext(), value + " is removed", Toast.LENGTH_SHORT).show();
                dbHelper.removeBookmark(value);
                bookmarkAdapter.removeItem(position);
                bookmarkAdapter.notifyDataSetChanged();
            }
        });
    }

    String[] getListOfWords() {
        String[] source = new String[] {
                "a",
                "abadon",
                "ability",
                "able",
                "about",
                "above",
                "aboard",
                "absense",
                "absent",
                "absolute",
                "absolutely",
                "absorb",
                "abuse",
                "academic",
                "accent",
                "accept",
                "acceptable",
                "access",
                "accident",
                "accidental",
                "accidentally",
                "accommodation",
                "accompany"
        };
        return source;
    }

    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clear,menu);
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