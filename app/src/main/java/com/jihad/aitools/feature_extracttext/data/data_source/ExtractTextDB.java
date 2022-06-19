package com.jihad.aitools.feature_extracttext.data.data_source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;

@Database(entities = {ExtractTextEntity.class}, version = 1)
@TypeConverters(RoomConverters.class)
public abstract class ExtractTextDB extends RoomDatabase {

    public abstract ExtractTextDao dao();

    private static ExtractTextDB INSTANCE = null;

   public static ExtractTextDB getInstance(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context,
                    ExtractTextDB.class,
                    "ExtractTextDB"
            ).build();
        }
            return INSTANCE;
    }
}
