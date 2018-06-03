package com.programing.anheimoxin.realcode.db.entity;

import org.litepal.crud.DataSupport;

/**
 * Created by Maomao on 2018/1/13.
 */

public class DBVersion extends DataSupport {
    private int id;
    private int nVersion;

    public DBVersion() {
    }

    public DBVersion(int id, int nVersion) {
        this.id = id;
        this.nVersion=nVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getnVersion() {
        return nVersion;
    }

    public void setnVersion(int nVersion) {
        this.nVersion = nVersion;
    }
}
