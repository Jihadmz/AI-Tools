package com.jihad.aitools.feature_extracttext.presentation.history;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jihad.aitools.feature_extracttext.data.repository.ExtractTextRepoImpl;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;

import java.util.ArrayList;
import java.util.List;

public class ExtractTextHistoryViewModel extends AndroidViewModel {

    private final ExtractTextRepoImpl repo;
    public LiveData<List<ExtractTextEntity>> list;

    public ExtractTextHistoryViewModel(Application application) {
        super(application);
        repo = new ExtractTextRepoImpl(application);
        list = repo.getAll();
    }


    public void deleteEntity(ExtractTextEntity entity){
        repo.delete(entity);
    }


    public void deleteAll(){
        if (list.getValue() != null)
            list.getValue().clear();
        repo.clear();
    }
}
