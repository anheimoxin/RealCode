package com.programing.anheimoxin.realcode;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.programing.anheimoxin.realcode.adapter.BookAdapter;
import com.programing.anheimoxin.realcode.db.entity.Book;
import com.programing.anheimoxin.realcode.db.entity.Chapter;
import com.programing.anheimoxin.realcode.db.entity.DBVersion;
import com.programing.anheimoxin.realcode.db.helper.DatabaseHelper;
import com.programing.anheimoxin.realcode.misc.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionsMenu fam;

    private List<Book> bookList = new ArrayList<>();
    private BookAdapter adapter;

    //双击退出的时间间隔
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化Book列表
        initList();

        //setup main layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        //StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BookAdapter(bookList);
        recyclerView.setAdapter(adapter);

        //绑定悬浮按钮菜单
        fam = (FloatingActionsMenu) findViewById(R.id.fam);

        //绑定悬浮按钮
        com.getbase.floatingactionbutton.FloatingActionButton fab_reset = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_action_reset);
        fab_reset.setOnClickListener(this);
        com.getbase.floatingactionbutton.FloatingActionButton fab_About = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_action_about);
        fab_About.setOnClickListener(this);
    }

    private void initList() {
        bookList.clear();
        List<Book> books = DataSupport.findAll(Book.class);
        bookList.addAll(books);
    }

    //对返回键进行监听,实现双击返回
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            //MyConfig.clearSharePre(this, "users");
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_action_reset:
                DatabaseHelper.clearDatabase();
                finish();
//                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

                break;
            case R.id.fab_action_about:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("关于")
                        .setMessage("当前版本"+Const.version)
                        .setPositiveButton("确定", null)
                        .show();
                fam.collapse();
                break;
            default:
                fam.collapse();
                break;
        }
    }


}
