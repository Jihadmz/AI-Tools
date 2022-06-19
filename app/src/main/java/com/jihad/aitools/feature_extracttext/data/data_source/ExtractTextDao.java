package com.jihad.aitools.feature_extracttext.data.data_source;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;

import java.util.List;

@Dao
public interface ExtractTextDao {

    @Query("select * from extracttext")
    LiveData<List<ExtractTextEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(ExtractTextEntity entity);

    @Delete
    void delete(ExtractTextEntity entity);
}
