package com.programing.anheimoxin.realcode.db.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Maomao on 2018/1/10.
 */

public class Book extends DataSupport {
    private int bookId;
    private String imageUrl;
    private String name;
    private String startDate;
    private String endDate;

    public Book(){

    }
    public Book(int bookId, String imageUrl, String name, String startDate, String endDate) {
        this.bookId = bookId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
