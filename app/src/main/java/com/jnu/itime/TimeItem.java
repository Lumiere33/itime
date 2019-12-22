package com.jnu.itime;

import android.graphics.Bitmap;

import java.io.Serializable;

public class TimeItem implements Serializable {
    private String name;
    private String date;
    private String period;
    private int year;
    private int month;
    private int day;
    private Bitmap picture;
    private String count;

    public TimeItem(String name, String period, int year, int month, int day, Bitmap picture) {
        this.name = name;
        this.period = period;
        this.year = year;
        this.month = month;
        this.day = day;
        this.picture = picture;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return year+"年"+month+"月"+day+"日";
    }


    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCount(){
        return count;
    }

    public void setCount(String count) {this.count=count;}
}
