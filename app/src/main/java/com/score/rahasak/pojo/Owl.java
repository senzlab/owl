package com.score.rahasak.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by eranga on 11/7/17.
 */

public class Owl implements Parcelable {
    String username;
    String from;
    String to;
    String date;
    String desc;

    public Owl(String username, String from, String to, String date, String desc) {
        this.username = username;
        this.from = from;
        this.to = to;
        this.date = date;
        this.desc = desc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    protected Owl(Parcel in) {
        username = in.readString();
        from = in.readString();
        to = in.readString();
        date = in.readString();
        desc = in.readString();
    }

    public static final Creator<Owl> CREATOR = new Creator<Owl>() {
        @Override
        public Owl createFromParcel(Parcel in) {
            return new Owl(in);
        }

        @Override
        public Owl[] newArray(int size) {
            return new Owl[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(date);
        dest.writeString(desc);
    }
}
