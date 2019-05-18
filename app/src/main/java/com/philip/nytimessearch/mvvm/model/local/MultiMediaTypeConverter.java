package com.philip.nytimessearch.mvvm.model.local;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.philip.nytimessearch.mvvm.model.local.entity.MultiMediaEntity;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * Converter for MultiMediaEntity to save and query to Room
 */
public class MultiMediaTypeConverter {
    @TypeConverter
    public static List<MultiMediaEntity> stringToMultiMedia(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<MultiMediaEntity>>() {}.getType();
        List<MultiMediaEntity> measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String MultiMediaToString(List<MultiMediaEntity> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<MultiMediaEntity>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
