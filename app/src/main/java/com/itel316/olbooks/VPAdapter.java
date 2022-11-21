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


import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.OlbookUtils;
import com.itel316.olbooks.models.User;

import java.util.ArrayList;

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
        holder.bookTitle.setText(OlbookUtils.shortener(viewPagerItem.book.getTitle()));
        holder.bookAuthor.setText("By "+OlbookUtils.shorterAuthors(viewPagerItem.book.getAuthor(), false));
        holder.bookLikes.setText(String.format("%s ", OlbookUtils.shortenNumber(viewPagerItem.getBook().getLikes())));
        holder.bookSaved.setText(String.format("%s ", OlbookUtils.shortenNumber(viewPagerItem.getBook().getSave())));
        Context context = rootActivity.getApplicationContext();
        int resId = context.getResources().getIdentifier(String.format("drawable/%s", viewPagerItem.getBook().getPhoto()), null, context.getPackageName());
        holder.bookImage.setImageResource(resId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);

        rerender(holder, viewPagerItem);

        holder.bookImage.setOnClickListener(e -> {
            Fragment_BookInfo bookFrag = new Fragment_BookInfo();
            Bundle bund = new Bundle();
            bund.putSerializable("curBook", viewPagerItem.getBook());
            bund.putSerializable("curUser", curUser);
            bookFrag.setArguments(bund);
            rootActivity.switchFragment(bookFrag);
        });

        holder.bookLikes.setTooltipText(String.format("%s People Read This Book", OlbookUtils.shortenNumber(viewPagerItem.getBook().getLikes())));
        holder.bookSaved.setTooltipText(String.format("%s Users Saved This Book", OlbookUtils.shortenNumber(viewPagerItem.getBook().getSave())));

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
            btnView = itemView.findViewById(R.id.readbtn);
            bookImage = itemView.findViewById(R.id.book_img);
            bookTitle = itemView.findViewById(R.id.book_tit);
            bookAuthor = itemView.findViewById(R.id.book_auth);
            bookLikes = itemView.findViewById(R.id.book_views);
            bookSaved = itemView.findViewById(R.id.book_saved);
        }
    }

}