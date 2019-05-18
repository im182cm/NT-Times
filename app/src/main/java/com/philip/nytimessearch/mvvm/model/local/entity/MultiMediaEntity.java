package com.philip.nytimessearch.mvvm.model.local.entity;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class MultiMediaEntity {
    @Ignore
    private final String imageUrlPrefix = "https://www.nytimes.com/";

    @ColumnInfo(name = "sub_type")
    @SerializedName("subtype")
    public String subType;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    public String url;

    public String getSubType() {
        return subType;
    }

    public String getUrl() {
        return imageUrlPrefix + url;
    }
}
