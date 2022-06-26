package com.jihad.aitools.feature_recognizedigitalink.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.vision.digitalink.DigitalInkRecognition;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer;
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions;
import com.google.mlkit.vision.digitalink.Ink;
import com.google.mlkit.vision.digitalink.RecognitionContext;
import com.google.mlkit.vision.digitalink.WritingArea;
import com.jihad.aitools.Core;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityRecognizeDigitalInkBinding;
import com.jihad.aitools.feature_recognizedigitalink.presentation.components.InkView;
import com.jihad.aitools.feature_translatetext.presentation.TranslateTextActivity;
import com.jihad.aitools.feature_translatetext.presentation.components.DialogDownloading;

public class RecognizeDigitalInkActivity extends AppCompatActivity {

    private ActivityRecognizeDigitalInkBinding binding;

    private Ink.Builder inkBuilder;
    private Ink.Stroke.Builder strokeBuilder;
    private DigitalInkRecognizer recognizer;
    private boolean isActive;

    @Override
    protected void onStart() {
        super.onStart();

        isActive = true;
    }

    @SuppressLint("ClickableViewAccessibility")
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

        float x = 0;
        float y = 0;
        binding.inInkView.inkView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                addNewTouchEvent(motionEvent, x, y);
                processInk(recognizer);
                return false;
            }
        });

        binding.ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Core.copyToClipboard(binding.etDetectedText.getText().toString());
                Core.showToast(getString(R.string.TextCopied), Toast.LENGTH_SHORT);
            }
        });

        binding.ivTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), TranslateTextActivity.class)
                        .putExtra("ExtractedText", binding.etDetectedText.getText().toString()));
            }
        });

        binding.ivClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  clearing the ink and the text detected
                InkView.path.reset();
                InkView.pathList.clear();
                binding.etDetectedText.setText("");
                inkBuilder = null;
                inkBuilder = Ink.builder();
            }
        });

        binding.ivErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clearing the ink only
                InkView.path.reset();
                InkView.pathList.clear();
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
                                getRecognizer();
                            })
                            .addOnFailureListener(
                                    e -> Log.e("Jihad", "Error while downloading a model: " + e));
                }else
                    getRecognizer();
            }
        });
    }

    private Paint initPaint(){
       Paint brush = new Paint();
        brush.setAntiAlias(true);
        brush.setColor(Color.parseColor("#7075DC"));
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeWidth(8f);

        return brush;
    }

    public void addNewTouchEvent(MotionEvent event, float x, float y) {
        x = event.getX();
        y = event.getY();

        // If your setup does not provide timing information, you can omit the
        // third paramater (t) in the calls to Ink.Point.create
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                strokeBuilder = Ink.Stroke.builder();
                strokeBuilder.addPoint(Ink.Point.create(x, y));
                break;
            case MotionEvent.ACTION_MOVE:
                strokeBuilder.addPoint(Ink.Point.create(x, y));
                break;
            case MotionEvent.ACTION_UP:
                strokeBuilder.addPoint(Ink.Point.create(x, y));
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
        String preContext = binding.etDetectedText.getText().toString();
        float width = ViewGroup.LayoutParams.MATCH_PARENT;
        float height = 400;

        RecognitionContext recognitionContext =
                RecognitionContext.builder()
                        .setPreContext(preContext)
                        .setWritingArea(new WritingArea(width, height))
                        .build();

        // This is what to send to the recognizer.
        Ink ink = inkBuilder.build();
        recognizer.recognize(ink, recognitionContext)
                .addOnSuccessListener(
                        // `result` contains the recognizer's answers as a RecognitionResult.
                        // Logs the text from the top candidate.
                        result -> {
                            if (isActive) { // if this activity is running only get the text processed
                                binding.etDetectedText.setText(result.getCandidates().get(0).getText());
                            }
                        }
                )
                .addOnFailureListener(
                        e -> Log.e("Jihad", "Error during recognition: " + e));
    }

    @Override
    protected void onStop() {
        super.onStop();

        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}