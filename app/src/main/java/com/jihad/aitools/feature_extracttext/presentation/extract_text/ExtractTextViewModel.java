package com.jihad.aitools.feature_extracttext.presentation.extract_text;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.ImageUtils;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.jihad.aitools.R;
import com.jihad.aitools.feature_extracttext.data.repository.ExtractTextRepoImpl;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;

import java.io.IOException;

public class ExtractTextViewModel extends AndroidViewModel {

    private final MutableLiveData<String> _extractedText;
    public LiveData<String> extractedText;

    private final MutableLiveData<Bitmap> _chosenImage;
    public LiveData<Bitmap> chosenImage;

    private final ExtractTextRepoImpl repo;
    private final TextRecognizer recognizer;

    public ExtractTextViewModel(Application application) {
        super(application);
        _extractedText = new MutableLiveData<>();
        _chosenImage = new MutableLiveData<>();
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        repo = new ExtractTextRepoImpl(application);
        extractedText = _extractedText;
        chosenImage = _chosenImage;
    }

    public void setExtractedText(String text){
        _extractedText.setValue(text);
        extractedText = _extractedText;
    }

    public void extractText(Uri uri, Context context) {
        try {
            recognizer.process(InputImage.fromFilePath(context, uri))
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                          setExtractedText(text.getText());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException exception) {
            Toast.makeText(context, context.getString(R.string.CouldntExtractText), Toast.LENGTH_SHORT).show();
        }
    }

    public void setChosenImage(Bitmap bm, ContentResolver contentResolver){
            _chosenImage.setValue(bm);
            chosenImage=  _chosenImage;
    }

    public void addEntity(String text, Bitmap image) {
        repo.add(new ExtractTextEntity(text, image));
    }

}
