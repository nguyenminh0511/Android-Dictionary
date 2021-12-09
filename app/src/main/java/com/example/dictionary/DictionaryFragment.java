package com.example.dictionary;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class DictionaryFragment extends Fragment {

    private FragmentListener listener;
    private String value = "Hello everyone";
    ArrayAdapter<String> adapter;
    ListView dictList;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
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
//        Button myButton = (Button) view.findViewById(R.id.myBtn);
//        myButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onItemClick(value);
//                }
//            }
//        });

        dictList = view.findViewById(R.id.dictionaryList);
        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, getListOfWords());
        dictList.setAdapter(adapter);
        dictList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemClick(getListOfWords()[position]);
                }
            }
        });
    }

    //filter value for searching
    public void filterValue(String value) {
        adapter.getFilter().filter(value);

//        int size = adapter.getCount();
//        for (int i = 0; i < size; ++i) {
//            if (adapter.getItem(i).startsWith(value)) {
//                dictList.setSelection(i);
//                break;
//            }
//        }
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

    // reset Data when change translate mode
    public void resetDataSource(String[] source) {
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,source);
        dictList.setAdapter(adapter);
    }

    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }
}