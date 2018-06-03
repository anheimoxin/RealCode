package com.programing.anheimoxin.realcode.db.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Maomao on 2018/1/13.
 */

public class Chapter extends DataSupport{
    private int bookId;
    private int chapterId;
    private String chapterName;
    private String contentUrl;

    public Chapter() {
    }

    public Chapter(int bookId, int chapterId, String chapterName, String contentUrl) {
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.contentUrl = contentUrl;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
