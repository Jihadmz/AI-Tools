package com.jihad.aitools.feature_extracttext.presentation.extract_text;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.io.IOException;

public class ExtractTextViewModel extends ViewModel {

    private final MutableLiveData<String> _extractedText;
    public LiveData<String> extractedText;

    private final MutableLiveData<Bitmap> _chosenImage;
    public LiveData<Bitmap> chosenImage;

    public ExtractTextViewModel() {
        _extractedText = new MutableLiveData<>();
        _chosenImage = new MutableLiveData<>();

        extractedText = _extractedText;
        chosenImage = _chosenImage;
    }

    public void extractText(Uri uri, Context context) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        try {
            InputImage inputImage = InputImage.fromFilePath(context, uri);
            Task<Text> task = recognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            _extractedText.setValue(text.getText());
                            extractedText = _extractedText;
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

    public void setChosenImage(Uri uri, ContentResolver contentResolver){
        try{
            _chosenImage.setValue(ImageUtils.getInstance().zza(contentResolver, uri));
            chosenImage=  _chosenImage;
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
