package com.jihad.aitools.feature_extracttext.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jihad.aitools.feature_extracttext.data.data_source.ExtractTextDB;
import com.jihad.aitools.feature_extracttext.data.data_source.ExtractTextDao;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;

import java.util.List;


public class ExtractTextRepoImpl implements ExtractTextDao{

    private final ExtractTextDao dao;

    public ExtractTextRepoImpl(Application application){
        dao = ExtractTextDB.getInstance(application).dao();
    }

    @Override
    public LiveData<List<ExtractTextEntity>> getAll() {
        return dao.getAll();
    }

    @Override
    public void add(ExtractTextEntity entity) {
        new AddExtractText(dao).execute(entity);
    }

    @Override
    public void delete(ExtractTextEntity entity) {
        new DeleteExtractText(dao).execute(entity);
    }


    /**
     * background insert text extraction model
     */
    private static class AddExtractText extends AsyncTask<ExtractTextEntity, Void, Void> {
        private final ExtractTextDao dao;

        private AddExtractText(ExtractTextDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ExtractTextEntity... extractTextEntities) {
            dao.add(extractTextEntities[0]);
            return null;
        }
    }

    /**
     * background delete text extraction model
     */
    private static class DeleteExtractText extends AsyncTask<ExtractTextEntity, Void ,Void>{
        private final ExtractTextDao dao;

        private DeleteExtractText(ExtractTextDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ExtractTextEntity... extractTextEntities) {
            dao.delete(extractTextEntities[0]);
            return null;
        }
    }
}
