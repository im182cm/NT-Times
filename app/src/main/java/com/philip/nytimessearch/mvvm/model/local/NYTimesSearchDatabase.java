package com.philip.nytimessearch.mvvm.model.local;

import com.philip.nytimessearch.mvvm.model.local.dao.DocDao;
import com.philip.nytimessearch.mvvm.model.local.entity.DocEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DocEntity.class}, version = 1, exportSchema = false)
public abstract class NYTimesSearchDatabase extends RoomDatabase {
    abstract public DocDao docDao();

}
