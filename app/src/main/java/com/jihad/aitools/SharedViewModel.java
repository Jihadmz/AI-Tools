package com.jihad.aitools;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isDownloadingModel;

    public SharedViewModel() {
        isDownloadingModel = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsDownloadingModel() {
        return isDownloadingModel;
    }

    public void setIsDownloadingModel(boolean value){
        isDownloadingModel.setValue(value);
    }
}
