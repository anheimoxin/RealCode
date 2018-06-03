package com.programing.anheimoxin.realcode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.programing.anheimoxin.realcode.ChapterActivity;
import com.programing.anheimoxin.realcode.R;
import com.programing.anheimoxin.realcode.db.entity.Book;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Maomao on 2017/12/28.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private Context mContext;
    private List<Book> mBookList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //CardView cardView;
        CircleImageView ciBook;
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            //cardView = (CardView) itemView;
            ciBook = (CircleImageView) itemView.findViewById(R.id.book_image);
            tvName = (TextView) itemView.findViewById(R.id.book_name);
        }
    }

    public BookAdapter(List<Book> bookList) {
        mBookList = bookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.ciBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Book book = mBookList.get(position);
                ChapterActivity.actionStart(mContext, book.getBookId(), book.getName(), book.getImageUrl());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = mBookList.get(position);
        holder.tvName.setText(book.getName());
        //图片资源的url或者id
        Glide.with(mContext).load(book.getImageUrl()).into(holder.ciBook);
        //Glide.with(mContext).load(R.drawable.pstartgirl).into(holder.ciBook);
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }
}
