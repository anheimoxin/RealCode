package com.programing.anheimoxin.realcode;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

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

public class AnimationActivity extends AppCompatActivity {

    private ImageView welcomeImg = null;
    private StringBuilder response;
    private final int REQUST_DONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        welcomeImg = this.findViewById(R.id.welcome_img);
        AlphaAnimation anima = new AlphaAnimation(0.5f, 1.0f);

        //setup animation time long
        anima.setDuration(3000);
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());

        //新建线程,发送http请求，请求我的网站的db.json文件
        sendHttpRequest(Const.address);
    }

    private class AnimationImpl implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.drawable.welcomeimg);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            skip();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        private void skip() {
            startActivity(new Intent(AnimationActivity.this, MainActivity.class));
            finish();
        }
    }

    ////*********************************************************

    private void loadDatabase() {
        //*********解析json**************
        //开始解析json数据
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            //数据库版本
            int dbVersion = jsonObject.getInt("dbVersion");

            //初始化数据库
            LitePal.getDatabase();
            //获取当前数据版本
            DBVersion db = DataSupport.find(DBVersion.class, 1);

            //如果数据版本不同或者不存在，则删除原来的数据库，更新数据库或者创建数据库
            if (db == null || db.getnVersion() != dbVersion) {

                DatabaseHelper.clearDatabase();

                db = new DBVersion(1, dbVersion);
                db.save();

                //书籍数组
                JSONArray booksJson = jsonObject.getJSONArray("book");
                //章节数组
                JSONArray chaptersJson = jsonObject.getJSONArray("chapter");

                //将书籍添加进Book
                for (int i = 0; i < booksJson.length(); i++) {
                    JSONObject bookJson = booksJson.getJSONObject(i);

                    Book book = new Book();

                    book.setBookId(bookJson.getInt("bookId"));
                    book.setName(bookJson.getString("bookName"));
                    book.setImageUrl(bookJson.getString("imageUrl"));
                    book.setStartDate(bookJson.getString("startDate"));
                    book.setEndDate(bookJson.getString("endDate"));

                    book.save();
                }

                //将章节信息添加进Chapter
                for (int i = 0; i < chaptersJson.length(); i++) {
                    JSONObject chapterJson = chaptersJson.getJSONObject(i);

                    Chapter chapter = new Chapter();

                    chapter.setBookId(chapterJson.getInt("bookId"));
                    chapter.setChapterId(chapterJson.getInt("chapterId"));
                    chapter.setChapterName(chapterJson.getString("chapterName"));
                    chapter.setContentUrl(chapterJson.getString("contentUrl"));

                    chapter.save();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //**********json解析完毕***************
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUST_DONE:
                    loadDatabase();
                    break;
            }
        }
    };

    // address为数据请求地址
    // 像联网进行数据请求这种耗时操作，最好都是放到子线程中进行，以避免阻塞主线程
    public void sendHttpRequest(final String address) {
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    // 请求方式
                    connection.setRequestMethod("GET");
                    // 连接超时
                    connection.setConnectTimeout(8000);
                    // 读取超时
                    connection.setReadTimeout(8000);
                    connection.connect();
                    // 获取输入流
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = REQUST_DONE;
                    myHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
