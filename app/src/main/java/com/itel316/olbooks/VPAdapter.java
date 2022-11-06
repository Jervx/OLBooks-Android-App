package com.itel316.olbooks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.itel316.olbooks.helpers.OlbookUtils;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    Activity_Home rootActivity;

    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItemArrayList, Activity_Home rootActivity) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
        this.rootActivity = rootActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);

        holder.bookImage.setImageResource(R.drawable.logo);
        holder.bookTitle.setText(viewPagerItem.book.getTitle());
        holder.bookAuthor.setText("By "+viewPagerItem.book.getAuthor());
        holder.bookLikes.setText(String.format("%s likes ", OlbookUtils.shortenNumber(viewPagerItem.getBook().getSave())));
        holder.bookSaved.setText(String.format("%s saved ", OlbookUtils.shortenNumber(viewPagerItem.getBook().getLikes())));
        holder.btnView.setOnClickListener(e -> {
            Fragment_BookInfo bookFrag = new Fragment_BookInfo();
            Bundle bund = new Bundle();
            bund.putSerializable("curBook", viewPagerItem.getBook());
            bookFrag.setArguments(bund);
            rootActivity.switchFragment(bookFrag);
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
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookLikes = itemView.findViewById(R.id.book_likes);
            bookSaved = itemView.findViewById(R.id.book_saves);
        }
    }

}