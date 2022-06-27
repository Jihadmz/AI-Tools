package com.jihad.aitools.shared.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isDownloadingModel;
    private final MutableLiveData<Boolean> isLoading;

    public SharedViewModel() {
        isDownloadingModel = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsDownloadingModel() {
        return isDownloadingModel;
    }

    public void setIsDownloadingModel(boolean value){
        isDownloadingModel.setValue(value);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean value){
        isLoading.setValue(value);
    }
}
