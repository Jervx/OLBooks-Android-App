package com.itel316.olbooks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itel316.olbooks.models.Book;
import com.itel316.olbooks.models.User;

public class Fragment_BookInfo extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private User curUser;
    private Book curBook;

    public Fragment_BookInfo() {

    }

    public static Fragment_BookInfo newInstance(String param1, String param2) {
        Fragment_BookInfo fragment = new Fragment_BookInfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__book_info, container, false);

        curUser = (User) getArguments().getSerializable("curUser");
        curBook = (Book) getArguments().getSerializable("curBook");

        ((TextView) view.findViewById(R.id.book_title)).setText(curBook.getTitle());
        curUser = (User) getArguments().getSerializable("curUser");

        return view;
    }


}