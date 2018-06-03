package com.programing.anheimoxin.realcode.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.programing.anheimoxin.realcode.ContentActivity;
import com.programing.anheimoxin.realcode.R;
import com.programing.anheimoxin.realcode.db.entity.Chapter;

import java.util.List;
import java.util.Random;

/**
 * Created by Maomao on 2018/1/13.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    private Context mContext;
    private List<Chapter> mChapterList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView chapterNum;
        TextView chapterName;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            chapterNum=itemView.findViewById(R.id.chapterNum);
            chapterName = itemView.findViewById(R.id.chapterName);
        }
    }

    public ChapterAdapter(List<Chapter> chapterList) {
        mChapterList = chapterList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.chapter_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Chapter chapter = mChapterList.get(position);
                ContentActivity.actionStart(mContext, chapter.getContentUrl());
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chapter chapter = mChapterList.get(position);
        holder.chapterNum.setText(String.valueOf(chapter.getChapterId()));
        holder.chapterName.setText(chapter.getChapterName());
        //图片资源的url或者id
        //Glide.with(mContext).load(book.getImage()).into(holder.ivBook);
        //Glide.with(mContext).load(R.mipmap.ic_launcher).into(holder.ivBook);
    }

    @Override
    public int getItemCount() {
        return mChapterList.size();
    }

}
