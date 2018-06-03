package com.programing.anheimoxin.realcode;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.programing.anheimoxin.realcode.adapter.ChapterAdapter;
import com.programing.anheimoxin.realcode.db.entity.Chapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity {

    private List<Chapter> chapterList = new ArrayList<>();
    private ChapterAdapter adapter;
    private RecyclerView recyclerView;

    public static void actionStart(Context context, int bookId,String bookName,String imageUrl) {
        Intent intent = new Intent(context, ChapterActivity.class);
        intent.putExtra("BOOK_ID", bookId);
        intent.putExtra("BOOK_NAME",bookName);
        intent.putExtra("BOOK_IMAGE_URL",imageUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //绑定控件
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView bookImageView = (ImageView) findViewById(R.id.product_image_view);

        //设置标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }
        //设置标题栏文字为书本名称
        collapsingToolbarLayout.setTitle(getIntent().getStringExtra("BOOK_NAME"));
        //展示书本图片
        Glide.with(this).load(getIntent().getStringExtra("BOOK_IMAGE_URL")).into(bookImageView);

        //初始化Book列表
        initList();

        recyclerView = (RecyclerView) findViewById(R.id.chapterList);
        //GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChapterAdapter(chapterList);
        recyclerView.setAdapter(adapter);
    }

    private void initList() {
        chapterList.clear();
        int id = getIntent().getIntExtra("BOOK_ID", 0);
        if (id != 0) {
            String bookId = String.valueOf(id);
            List<Chapter> chapters = DataSupport.where("bookId=?", bookId).find(Chapter.class);
            chapterList.addAll(chapters);
        }
    }
}
