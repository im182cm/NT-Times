package com.philip.nytimessearch.mvvm.model.local.entity;

import com.google.gson.annotations.SerializedName;

import androidx.room.ColumnInfo;

public class HeadLineEntity {
    @ColumnInfo(name = "main")
    @SerializedName("main")
    public String main;

    public String getMain() {
        return main;
    }
}
