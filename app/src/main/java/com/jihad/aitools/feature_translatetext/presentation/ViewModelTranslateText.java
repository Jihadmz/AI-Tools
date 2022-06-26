package com.jihad.aitools.feature_translatetext.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jihad.aitools.feature_translatetext.data.repository.RepoImplTranslateText;

public class ViewModelTranslateText extends ViewModel {

    private final MutableLiveData<String> textToTranslate;
    private final MutableLiveData<String> sourceLanguageId;
    private final MutableLiveData<String> chosenLanguageId;
    private final MutableLiveData<String> translatedText;

    private final RepoImplTranslateText repoImplTranslateText;

    public ViewModelTranslateText(){
        repoImplTranslateText = new RepoImplTranslateText();

        textToTranslate = new MutableLiveData<>();
        sourceLanguageId = new MutableLiveData<>("en");
        chosenLanguageId = new MutableLiveData<>(repoImplTranslateText.getChosenLanguage());
        translatedText = new MutableLiveData<>();
    }

    public LiveData<String> getTextToTranslate() {
        return textToTranslate;
    }

    public void setTextToTranslate(String textToTranslate) {
        this.textToTranslate.setValue(textToTranslate);
    }

    public LiveData<String> getSourceLanguageId() {
        return sourceLanguageId;
    }

    public void setSourceLanguageCode(String sourceLanguageId) {
        this.sourceLanguageId.setValue(sourceLanguageId);
    }

    public LiveData<String> getChosenLanguageId() {
        return chosenLanguageId;
    }

    public void setChosenLanguage(String chosenLanguageId) {
        repoImplTranslateText.setChosenLanguage(chosenLanguageId);
        this.chosenLanguageId.setValue(chosenLanguageId);
    }

    public LiveData<String> getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText.setValue(translatedText);
    }
}
