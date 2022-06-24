package com.jihad.aitools.feature_translatetext.data.data_source;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.jihad.aitools.feature_translatetext.CoreTranslateText;

public class SharedPreferencesTranslateText {

    public static SharedPreferences getInstance(){
        return CoreTranslateText.application.getSharedPreferences("TranslateText", Context.MODE_PRIVATE);
    }
}
