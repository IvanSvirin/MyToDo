package com.example.MyToDo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ivan on 19.07.2015.
 */
public class Doing implements Parcelable, Serializable {
    private String toDo;
    private String date;
    private boolean isDone;

    public static final Creator<Doing> CREATOR = new Creator<Doing>() {
        @Override
        public Doing createFromParcel(Parcel parcel) {
            return new Doing(parcel);
        }

        @Override
        public Doing[] newArray(int i) {
            return new Doing[i];
        }
    };

    public Doing() {
    }

    public Doing(Parcel parcel) {
        toDo = parcel.readString();
        date = parcel.readString();
    }

    public String getToDo() {
        return toDo;
    }

    public void setToDo(String toDo) {
        this.toDo = toDo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(toDo);
        parcel.writeString(date);
    }
}
