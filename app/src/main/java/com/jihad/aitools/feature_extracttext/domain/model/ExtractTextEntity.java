package com.jihad.aitools.feature_extracttext.domain.model;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ExtractText")
public class ExtractTextEntity {

    @PrimaryKey(autoGenerate = true) int id = 0;
    private String text;

    public ExtractTextEntity(String text){
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
