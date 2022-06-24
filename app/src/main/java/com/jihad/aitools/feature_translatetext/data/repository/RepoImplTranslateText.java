package com.jihad.aitools.feature_translatetext.data.repository;

import android.content.SharedPreferences;

import com.jihad.aitools.feature_translatetext.data.data_source.SharedPreferencesTranslateText;

public class RepoImplTranslateText {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public RepoImplTranslateText(){
        sharedPreferences = SharedPreferencesTranslateText.getInstance();
        editor = sharedPreferences.edit();
    }

    public void setChosenLanguage(String language){
        editor.putString("ChosenLanguage", language);
        editor.apply();
    }

    public String getChosenLanguage(){
        return sharedPreferences.getString("ChosenLanguage", "ENGLISH");
    }
}
