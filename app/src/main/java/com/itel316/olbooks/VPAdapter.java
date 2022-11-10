package com.itel316.olbooks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.OlbookUtils;
import com.itel316.olbooks.models.User;

import java.util.ArrayList;
import java.util.Date;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    Activity_Home rootActivity;
    View parentView;
    User curUser;
    DatabaseHelper dbHelper;

    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItemArrayList, Activity_Home rootActivity, View parentView, User curUser, DatabaseHelper dbHelper) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
        this.rootActivity = rootActivity;
        this.parentView = parentView;
        this.curUser = curUser;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item,parent,false);

        return new ViewHolder(view);
    }

    public void rerender(ViewHolder holder, ViewPagerItem viewPagerItem){
        holder.bookImage.setImageResource(R.drawable.logo);
        holder.bookTitle.setText(viewPagerItem.book.getTitle());
        holder.bookAuthor.setText("By "+viewPagerItem.book.getAuthor());
        holder.bookLikes.setText(String.format("%s likes ", OlbookUtils.shortenNumber(viewPagerItem.getBook().getLikes())));
        holder.bookSaved.setText(String.format("%s save ", OlbookUtils.shortenNumber(viewPagerItem.getBook().getSave())));
        Context context = rootActivity.getApplicationContext();
        int resId = context.getResources().getIdentifier(String.format("drawable/%s", viewPagerItem.getBook().getPhoto()), null, context.getPackageName());
        holder.bookImage.setImageResource(resId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);

        rerender(holder, viewPagerItem);

        holder.btnView.setOnClickListener(e -> {
            Fragment_BookInfo bookFrag = new Fragment_BookInfo();
            Bundle bund = new Bundle();
            bund.putSerializable("curBook", viewPagerItem.getBook());
            bund.putSerializable("curUser", curUser);
            bookFrag.setArguments(bund);
            rootActivity.switchFragment(bookFrag);
        });

        holder.bookLikes.setOnClickListener(e->{
            Snackbar snackbar;
            snackbar = Snackbar.make( parentView,"You liked this book!",Snackbar.LENGTH_SHORT);
            dbHelper.likeBook(viewPagerItem.getBook().getLikes() + 1, viewPagerItem.getBook().getIsbn_10());
            snackbar.show();
            viewPagerItem.getBook().fetchSelf(dbHelper);
            rerender(holder, viewPagerItem);
        });

        holder.bookSaved.setOnClickListener(e->{
            Snackbar snackbar;
            if(OlbookUtils.doesBookAdded(curUser, viewPagerItem.getBook())) {
                snackbar = Snackbar.make( parentView,"Removed from your saved books!",Snackbar.LENGTH_SHORT);
                dbHelper.removeFromBookList(viewPagerItem.getBook().getSave() - 1, curUser.getUserId(), viewPagerItem.getBook().getIsbn_10());
            }else{
                snackbar = Snackbar.make( parentView,"Added to your saved books.",Snackbar.LENGTH_SHORT);
                dbHelper.insertToBookList(viewPagerItem.getBook().getSave() + 1, OlbookUtils.toISODateString(new Date()), curUser.getUserId(), viewPagerItem.getBook().getIsbn_10(), viewPagerItem.getBook().getIsbn_13());
            }
            curUser.fetchSelf(dbHelper);
            viewPagerItem.getBook().fetchSelf(dbHelper);
            snackbar.show();
            rerender(holder, viewPagerItem);
        });
    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView bookImage;
        TextView bookTitle, bookAuthor, bookLikes, bookSaved;
        Button btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnView = itemView.findViewById(R.id.btn_view_details);
            bookImage = itemView.findViewById(R.id.book_image);
            bookTitle = itemView.findViewById(R.id.book_desc);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookLikes = itemView.findViewById(R.id.book_likes);
            bookSaved = itemView.findViewById(R.id.book_saves);
        }
    }

}