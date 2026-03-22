package com.example.fbla;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {

    public String title, description, date, time, location, color, dotColor;
    public long dateMillis;

    public Event(String title, String description, String date, String time,
                 String location, String color, String dotColor, long dateMillis) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.color = color;
        this.dotColor = dotColor;
        this.dateMillis = dateMillis;
    }

    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        color = in.readString();
        dotColor = in.readString();
        dateMillis = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(color);
        dest.writeString(dotColor);
        dest.writeLong(dateMillis);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}