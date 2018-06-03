package com.programing.anheimoxin.realcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.PatternMatcher;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.programing.anheimoxin.realcode.db.entity.Book;
import com.programing.anheimoxin.realcode.db.entity.Chapter;
import com.programing.anheimoxin.realcode.db.entity.DBVersion;
import com.programing.anheimoxin.realcode.db.helper.DatabaseHelper;
import com.programing.anheimoxin.realcode.misc.Const;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

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

//用于展示开屏广告
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout container;

    private StringBuilder response;
    private final int REQUST_DONE = 1;

    /**
     * 用于判断是否可以跳过广告，直接进入MainActivity
     */
    private boolean canJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        container = (RelativeLayout) findViewById(R.id.container);
        //进行运行时权限处理
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            requestAds();
        }

        //新建线程,发送http请求，请求我的网站的db.json文件
        sendHttpRequest(Const.address);
    }

    /**
     * 请求开屏广告
     */
    private void requestAds() {
        String appId = "1105585573";
        String adId = "4010212448179536";

        new SplashAD(this, container, appId, adId, new SplashADListener() {
            @Override
            public void onADDismissed() {
                //广告显示完毕
                forward();
            }

            @Override
            public void onNoAD(AdError adError) {
                //广告加载失败
                Toast.makeText(SplashActivity.this,"failed",Toast.LENGTH_SHORT).show();
                forward();
            }

            @Override
            public void onADPresent() {
                //广告加载成功
            }

            @Override
            public void onADClicked() {
                //广告被点击
            }

            @Override
            public void onADTick(long l) {
                Toast.makeText(SplashActivity.this,"hello",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            forward();
        }
        canJump = true;
    }

    private void forward() {
        if (canJump) {
            //跳转到mainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            canJump = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本应用", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestAds();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

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
