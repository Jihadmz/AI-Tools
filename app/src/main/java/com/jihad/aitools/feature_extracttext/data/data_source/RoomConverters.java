package com.jihad.aitools.feature_extracttext.data.data_source;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class RoomConverters{

    @TypeConverter
    public byte[] fromBitmapToByteArray(Bitmap bm){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
        return outputStream.toByteArray();
    }

    @TypeConverter
    public Bitmap toBitmapFromByteArray(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
