package com.programing.anheimoxin.realcode.db.helper;

import com.programing.anheimoxin.realcode.R;
import com.programing.anheimoxin.realcode.db.entity.Book;
import com.programing.anheimoxin.realcode.db.entity.Chapter;
import com.programing.anheimoxin.realcode.db.entity.DBVersion;

import org.litepal.crud.DataSupport;

/**
 * Created by Maomao on 2018/1/13.
 */

public class DatabaseHelper {

    public static void clearDatabase(){
        DataSupport.deleteAll(Book.class);
        DataSupport.deleteAll(Chapter.class);
        DataSupport.deleteAll(DBVersion.class);
    }
}
