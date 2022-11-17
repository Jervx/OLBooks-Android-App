package com.itel316.olbooks;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.models.Book;
import com.itel316.olbooks.models.BookList;
import com.itel316.olbooks.models.User;

import java.util.ArrayList;

public class Fragment_Bookmarks extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DatabaseHelper dbHelper;

    private String mParam1;
    private String mParam2;
    private User curUser;

    private BookList books;
    private LinearLayout booklist;
    private ImageView nofound;

    public Fragment_Bookmarks() {
    }

    public static Fragment_Bookmarks newInstance(String param1, String param2) {
        Fragment_Bookmarks fragment = new Fragment_Bookmarks();
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

        View view = inflater.inflate(R.layout.fragment__bookmarks, container, false);

        dbHelper = new DatabaseHelper(getContext());
        curUser = (User) getArguments().getSerializable("curUser");

        nofound = view.findViewById(R.id.nofound);
        booklist = view.findViewById(R.id.booklist);

        renderBooks();

        return view;
    }

    public void renderBooks(){
        books = curUser.getBookList(dbHelper);
        Book[] saved = books.getBooks();

        if(saved.length == 0) nofound.setVisibility(View.VISIBLE);
        else nofound.setVisibility(View.GONE);

        ArrayList <View> AddedBoooks = new ArrayList<>();

        for(Book bk : saved) {
            View v = View.inflate(getContext(), R.layout.bookmarkbook_item, null);
            ImageView book_img = v.findViewById(R.id.book_img);

            int resId = getContext().getResources().getIdentifier(String.format("drawable/%s", bk.getPhoto()), null, getContext().getPackageName());
            book_img.setImageResource(resId);

            Button read = v.findViewById(R.id.readbtn);
            ImageButton remove = v.findViewById(R.id.remove_btn);

            ((TextView) v.findViewById(R.id.book_tit)).setText(bk.getTitle());
            ((TextView) v.findViewById(R.id.book_auth)).setText(bk.getAuthor());

            read.setOnClickListener(e -> {
                dbHelper.likeBook(bk.getLikes() + 1, bk.getIsbn_10());
                Intent reader = new Intent(getContext(), pdfreader.class);
                reader.putExtra("pdf", bk.getPdfFile());
                reader.putExtra("book", bk);
                startActivity(reader);
            });

            remove.setOnClickListener(e -> {
                Toast.makeText( getContext(),"Removed From Saved Books", Toast.LENGTH_SHORT).show();
                dbHelper.removeFromBookList(bk.getSave() - 1, curUser.getUserId(), bk.getIsbn_10());
                curUser.fetchSelf(dbHelper);
                for(View AddedBks : AddedBoooks) booklist.removeView(AddedBks);
                renderBooks();
            });


            AddedBoooks.add(v);
            booklist.addView(v);
        }
    }
}