package com.example.refocus;

import android.content.pm.ApplicationInfo;
import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    String name;
    String hours;
    String minutes;

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHours() { return hours; }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinutes() { return minutes; }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Category(Parcel source) {
        name = source.readString();
        hours = source.readString();
        minutes = source.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(hours);
        dest.writeString(minutes);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>(){
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
