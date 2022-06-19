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
//    private LiveData<List<ExtractTextEntity>> extractTextEntity;

    public ExtractTextHistoryViewModel(Application application) {
        super(application);
        repo = new ExtractTextRepoImpl(application);
//        extractTextEntity = repo.getAll();
    }

//    public LiveData<List<ExtractTextEntity>> getExtractTextEntityList() {
//        if (extractTextEntity == null) {
//            extractTextEntity = new MutableLiveData<>(new ArrayList<>());
//        }
//        return extractTextEntity;
//    }

    public void deleteEntity(ExtractTextEntity entity){
        repo.delete(entity);
    }

    public LiveData<List<ExtractTextEntity>> getAll(){
        return repo.getAll();
    }
}
