package com.philip.nytimessearch.mvvm.model.local.entity;

import com.google.gson.annotations.SerializedName;
import com.philip.nytimessearch.mvvm.model.local.MultiMediaTypeConverter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "doc")
public class DocEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "web_url")
    @SerializedName("web_url")
    public String webUrl;

    @Embedded
    @SerializedName("headline")
    public HeadLineEntity headline;

    @TypeConverters(MultiMediaTypeConverter.class)
    @SerializedName("multimedia")
    public List<MultiMediaEntity> multimedia;

    @Ignore
    private MultiMediaEntity thumbnail = null;
    @Ignore
    private boolean hasThumbnailSearched = false;

    public String getWebUrl() {
        return webUrl;
    }

    public HeadLineEntity getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        // To avoid duplicate searching work
        if (hasThumbnailSearched)
            return getThumbnailUrl();

        hasThumbnailSearched = true;
        if (multimedia == null || multimedia.size() == 0)
            return null;

        // Only search for thumbnail even there could be some images.
        for (MultiMediaEntity item : multimedia) {
            if (item.getSubType().equals("thumbnail")) {
                thumbnail = item;
                break;
            }
        }
        return getThumbnailUrl();
    }

    private String getThumbnailUrl() {
        return thumbnail != null ? thumbnail.getUrl() : null;
    }
}