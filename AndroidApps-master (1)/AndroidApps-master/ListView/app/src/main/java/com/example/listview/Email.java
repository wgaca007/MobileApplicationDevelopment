package com.example.listview;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

public class Email implements Parcelable {
    int id;
    String name;

    public Email(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Email(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Email> CREATOR = new Creator<Email>() {
        @Override
        public Email createFromParcel(Parcel in) {
            return new Email(in);
        }

        @Override
        public Email[] newArray(int size) {
            return new Email[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
