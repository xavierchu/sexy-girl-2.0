package org.meepo.sexygirl;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xavierchu on 13-11-8.
 */
public class Image implements Parcelable {

    private String pn;

    private String name;

    private String thumbUrl;

    private String url;

    private String date;

    private int width;

    private int height;

    private int thumbWidth;

    private int thumbHeight;

    private String abs;

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static Image toImage(JSONObject json) throws JSONException {
        Image img = new Image();
        img.setPn(json.getString("pn"));
        img.setAbs(json.getString("abs"));
        img.setHeight(json.getInt("image_height"));
        img.setWidth(json.getInt("image_width"));
        img.setName(json.getString("abs"));
        img.setThumbHeight(json.getInt("thumbnail_height"));
        img.setThumbWidth(json.getInt("thumbnail_width"));
        img.setThumbUrl(json.getString("thumbnail_url"));
        img.setUrl(json.getString("image_url"));
        img.setDate(json.getString("date"));
        return img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getName());
        parcel.writeString(this.getAbs());
        parcel.writeString(this.getUrl());
        parcel.writeInt(this.getHeight());
        parcel.writeInt(this.getWidth());
        parcel.writeString(this.getThumbUrl());
        parcel.writeInt(this.getThumbHeight());
        parcel.writeInt(this.getThumbWidth());
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel in) {
            Image img = new Image();
            img.setName(in.readString());
            img.setAbs(in.readString());
            img.setUrl(in.readString());
            img.setHeight(in.readInt());
            img.setWidth(in.readInt());
            img.setThumbUrl(in.readString());
            img.setThumbHeight(in.readInt());
            img.setThumbWidth(in.readInt());
            return img;
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
