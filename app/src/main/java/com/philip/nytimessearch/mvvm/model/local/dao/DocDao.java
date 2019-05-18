package com.philip.nytimessearch.mvvm.model.local.dao;


import com.philip.nytimessearch.mvvm.model.local.entity.DocEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DocDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCompanyTickers(List<DocEntity> docEntityList);

    @Query("SELECT * from doc")
    LiveData<List<DocEntity>> queryDocList();
}
