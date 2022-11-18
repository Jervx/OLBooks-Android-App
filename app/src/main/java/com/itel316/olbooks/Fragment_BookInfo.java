package com.itel316.olbooks;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.OlbookUtils;
import com.itel316.olbooks.models.Book;
import com.itel316.olbooks.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.blurry.Blurry;

public class Fragment_BookInfo extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DatabaseHelper dbHelper;
    private User curUser;
    private Book curBook;

    private ImageView cover_blur;
    private Button book_saves_btnText, btn_startRead;

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
        dbHelper = new DatabaseHelper(getContext());
        curUser = (User) getArguments().getSerializable("curUser");
        curUser.fetchSelf(dbHelper);
        curBook = (Book) getArguments().getSerializable("curBook");

        cover_blur = (ImageView) view.findViewById(R.id.cover_blur);

        int resId = getContext().getResources().getIdentifier(String.format("drawable/%s", curBook.getPhoto()), null, getContext().getPackageName());
        cover_blur.setImageResource(resId);

        btn_startRead = view.findViewById(R.id.btn_startRead);

        Bitmap bm=((BitmapDrawable)cover_blur.getDrawable()).getBitmap();
        Blurry.with(getContext()).from(bm).into(cover_blur);

        ((ImageView) view.findViewById(R.id.cover)).setImageResource(resId);

        book_saves_btnText = view.findViewById(R.id.book_saves_btnText);
        TextView book_likes = view.findViewById(R.id.book_likes);

        rerender(view);

        book_saves_btnText.setOnClickListener(e->{
            Snackbar snackbar;
            if(OlbookUtils.doesBookAdded(curUser, curBook)) {
                snackbar = Snackbar.make( view,"Removed from your saved books!",Snackbar.LENGTH_SHORT);
                dbHelper.removeFromBookList(curBook.getSave()-1 ,curUser.getUserId(), curBook.getIsbn_10());
                curBook.fetchSelf(dbHelper);
            }else{
                snackbar = Snackbar.make( view,"Added to your saved books.",Snackbar.LENGTH_SHORT);
                dbHelper.insertToBookList( curBook.getSave() + 1,OlbookUtils.toISODateString(new Date()), curUser.getUserId(), curBook.getIsbn_10(), curBook.getIsbn_13());
                curBook.fetchSelf(dbHelper);
            }
            curUser.fetchSelf(dbHelper);
            snackbar.show();
            rerender(view);
        });

        btn_startRead.setOnClickListener(ev -> {
            dbHelper.likeBook(curBook.getLikes() + 1, curBook.getIsbn_10());

            Intent read = new Intent(getContext(), pdfreader.class);
            read.putExtra("pdf", curBook.getPdfFile());
            read.putExtra("book", curBook);
            startActivity(read);
        });
        return view;
    }

    public void rerender(View view){
        ((TextView) view.findViewById(R.id.txtView_author_name)).setText("By " + curBook.getAuthor());
        ((TextView) view.findViewById(R.id.text_view_more_desc)).setText(String.format("Published : %s\nISBN-10 : %s\nISBN-13 : %s", new SimpleDateFormat("EEE, d MMM yyyy").format(OlbookUtils.fromIoDateStringToDate(curBook.getPubDate()))+"" , curBook.getIsbn_10(), curBook.getIsbn_13()));
        ((TextView) view.findViewById(R.id.book_title)).setText(curBook.getTitle());
        ((TextView) view.findViewById(R.id.book_likes)).setText(OlbookUtils.shortenNumber(curBook.getLikes()) + " ");
        ((TextView) view.findViewById(R.id.book_saves)).setText(OlbookUtils.shortenNumber(curBook.getSave()) + " ");

        ((TextView) view.findViewById(R.id.book_tit)).setText("\t\t" + curBook.getDescription().replaceAll("<~>", "\n\n\t"));

        if(OlbookUtils.doesBookAdded(curUser, curBook)) {
            book_saves_btnText.setText("Unsave");
        }else{
            book_saves_btnText.setText("Save");
        }
    }

}