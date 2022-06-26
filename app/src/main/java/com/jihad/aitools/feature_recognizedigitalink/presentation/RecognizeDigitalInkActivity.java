package com.jihad.aitools.feature_recognizedigitalink.presentation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;
import com.google.mlkit.vision.digitalink.Ink;
import com.jihad.aitools.Core;
import com.jihad.aitools.MainActivity;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityRecognizeDigitalInkBinding;
import com.jihad.aitools.feature_recognizedigitalink.presentation.components.InkView;
import com.jihad.aitools.feature_translatetext.presentation.components.DialogDownloading;

public class RecognizeDigitalInkActivity extends AppCompatActivity {

    private ActivityRecognizeDigitalInkBinding binding;

    private Ink.Builder inkBuilder;
    private Ink.Stroke.Builder strokeBuilder;
    private DigitalInkRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecognizeDigitalInkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.DigitalInk));
        actionBar.setDisplayHomeAsUpEnabled(true);

        //  initializing fields
        inkBuilder = Ink.builder();

        binding.inInkView.inkView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                addNewTouchEvent(motionEvent);
                processInk(recognizer);
                return false;
            }
        });

        //  observing when the download dialog should appear
        DialogDownloading dialogDownloading = new DialogDownloading(this);
        Core.sharedViewModel.getIsDownloadingModel().observe((LifecycleOwner) RecognizeDigitalInkActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    dialogDownloading.create();
                    dialogDownloading.show();
                } else {
                    dialogDownloading.dismiss();
                }
            }
        });

        //  creating and downloading the model if needed
        DigitalInkRecognitionModelIdentifier modelIdentifier;
        modelIdentifier =
                DigitalInkRecognitionModelIdentifier.EN_US;
        DigitalInkRecognitionModel model = DigitalInkRecognitionModel.builder(modelIdentifier).build();
        RemoteModelManager remoteModelManager = RemoteModelManager.getInstance();
        remoteModelManager.isModelDownloaded(model).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

                if (!aBoolean){
                    Core.sharedViewModel.setIsDownloadingModel(true);

                    remoteModelManager
                            .download(model, new DownloadConditions.Builder().build())
                            .addOnSuccessListener(aVoid -> {
                                Core.sharedViewModel.setIsDownloadingModel(false);
                                Log.i("Jihad", "Model downloaded");
                                getRecognizer();
                            })
                            .addOnFailureListener(
                                    e -> Log.e("Jihad", "Error while downloading a model: " + e));
                }else
                    getRecognizer();
            }
        });
    }

    public void addNewTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        long t = System.currentTimeMillis();

        // If your setup does not provide timing information, you can omit the
        // third paramater (t) in the calls to Ink.Point.create
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                strokeBuilder = Ink.Stroke.builder();
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_MOVE:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                break;
            case MotionEvent.ACTION_UP:
                strokeBuilder.addPoint(Ink.Point.create(x, y, t));
                inkBuilder.addStroke(strokeBuilder.build());
                strokeBuilder = null;
                break;
        }
    }

    private void getRecognizer(){
        // Specify the recognition model for a language
        DigitalInkRecognitionModelIdentifier modelIdentifier;
        modelIdentifier =
                DigitalInkRecognitionModelIdentifier.EN_US;

        DigitalInkRecognitionModel model =
                DigitalInkRecognitionModel.builder(modelIdentifier).build();

// Get a recognizer for the language
        recognizer =
                DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(model).build());

    }

    private void processInk(DigitalInkRecognizer recognizer){
        // This is what to send to the recognizer.
        Ink ink = inkBuilder.build();
        Log.d("Jihad", "processInk: "+ink.getStrokes());
        recognizer.recognize(ink)
                .addOnSuccessListener(
                        // `result` contains the recognizer's answers as a RecognitionResult.
                        // Logs the text from the top candidate.
                        result -> Log.i("Jihad", result.getCandidates().get(0).getText()))
                .addOnFailureListener(
                        e -> Log.e("Jihad", "Error during recognition: " + e));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}