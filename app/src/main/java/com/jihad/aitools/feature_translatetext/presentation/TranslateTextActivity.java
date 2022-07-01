package com.jihad.aitools.feature_translatetext.presentation;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.jihad.aitools.Core;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityTranslateTextBinding;
import com.jihad.aitools.feature_translatetext.CoreTranslateText;
import com.jihad.aitools.feature_translatetext.presentation.components.DialogLanguageChooser;
import com.jihad.aitools.shared.components.DialogDownloading;

import java.util.Locale;

public class TranslateTextActivity extends AppCompatActivity {

    private ActivityTranslateTextBinding binding;
    private TextToSpeech textToSpeech;
    private TextToSpeech textToSpeech1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranslateTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CoreTranslateText.viewModel = new ViewModelProvider(this).get(ViewModelTranslateText.class);
        CoreTranslateText.modelManager = RemoteModelManager.getInstance();

        textToSpeech = new TextToSpeech(TranslateTextActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
        textToSpeech1 = new TextToSpeech(TranslateTextActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                gettingLocalLanguageFromLanguage(binding.tvChosenId.getText().toString());
            }
        });

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.TranslateText));
        actionBar.setDisplayHomeAsUpEnabled(true);


        //  observing when the download dialog show appear
        DialogDownloading dialogDownloading = new DialogDownloading(this);
        Core.sharedViewModel.getIsDownloadingModel().observe((LifecycleOwner) TranslateTextActivity.this, new Observer<Boolean>() {
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

        // observing text to translate
        CoreTranslateText.viewModel.getTextToTranslate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.etTextTranslate.setText(s);
            }
        });

        // observing translated text
        CoreTranslateText.viewModel.getTranslatedText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvTranslatedText.setText(s);
            }
        });

        //  Observing chosen language
        CoreTranslateText.viewModel.getChosenLanguageId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvChosenId.setText(s);

                // this will set the language of the textToSpeech1 relative to the chosen language
                gettingLocalLanguageFromLanguage(s);
            }
        });

        CoreTranslateText.viewModel.getSourceLanguageId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvSourceId.setText(gettingSourceLanguage(s));

                //  setting the language of the textToSpeech relative to the source language
                switch (s){
                    case "fr": textToSpeech.setLanguage(Locale.FRENCH); break;
                    case "en": textToSpeech.setLanguage(Locale.ENGLISH); break;
                    case "de": textToSpeech.setLanguage(Locale.GERMAN); break;
                    case "ar": {
                        textToSpeech.setLanguage(new Locale("ar"));
                        break;
                    }
                }
            }
        });

        //  Clicking on the chosen text -> chooser dialog opens
        binding.tvChosenId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogLanguageChooser dialogLanguageChooser = new DialogLanguageChooser(view.getContext(), binding.etTextTranslate.getText().toString());
                dialogLanguageChooser.create();
                dialogLanguageChooser.show();
            }
        });

        //  on text changes listener
        LanguageIdentifier languageIdentifier =
                LanguageIdentification.getClient();
        binding.etTextTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                languageIdentifier.identifyLanguage(editable.toString())
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(@Nullable String languageCode) {
                                        if (!languageCode.equals("und")) {
                                            CoreTranslateText.viewModel.setSourceLanguageCode(languageCode);
                                            CoreTranslateText.translating(languageCode, editable.toString(), binding.tvChosenId.getText().toString());
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Model couldn’t be loaded or other internal error.
                                        // ...
                                    }
                                });
            }
        });

        //  paste icon
        binding.ivPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Core.pasteFromClipboardInToEditText(binding.etTextTranslate);
            }
        });

        // clear icon
        binding.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etTextTranslate.getText().clear();
                Core.requestFocusAndShowKeyboard(binding.etTextTranslate);
            }
        });

        // copy icon
        binding.ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Core.copyToClipboard(binding.tvTranslatedText.getText().toString());
            }
        });

        // speak text to translate icon
        binding.ivSpeak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        //  speak text translated icon
        binding.ivSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak1();
            }
        });

        //  on card view listener -> edit text request focus
        binding.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Core.requestFocusAndShowKeyboard(binding.etTextTranslate);
            }
        });

        // if user is getting here from the text extraction activity -> user wants to translate extract tex
        if (getIntent().hasExtra("ExtractedText")) { // user coming here with the text being copied to translate it
            String text = getIntent().getStringExtra("ExtractedText");
            binding.etTextTranslate.setText(text);
        }

        //  Downloading english model if it is not already downloaded
        downloadEnglishLanguageModel();



    }

    private void gettingLocalLanguageFromLanguage(String language){
        switch (language){
            case "ENGLISH": textToSpeech1.setLanguage(Locale.ENGLISH); break;
            case "GERMANY": textToSpeech1.setLanguage(Locale.GERMAN); break;
            case "FRENCH": textToSpeech1.setLanguage(Locale.FRENCH); break;
            case "ARABIC":{
                textToSpeech1.setLanguage(new Locale("ar"));
                break;
            }
        }
    }

    private void speak(){
        textToSpeech.speak(binding.etTextTranslate.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void speak1(){
        textToSpeech1.speak(binding.tvTranslatedText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private String gettingSourceLanguage(String languageCode) {
        switch (languageCode) {
            case "en":
                return "ENGLISH";
            case "de":
                return "GERMANY";
            case "fr":
                return "FRENCH";
            case "ar":
                return "ARABIC";
            default:
                return "unk";
        }
    }

    private void downloadEnglishLanguageModel() {
        // Download english model.
        TranslateRemoteModel englishModel =
                new TranslateRemoteModel.Builder("en").build();

        CoreTranslateText.modelManager.isModelDownloaded(englishModel).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean) { // model is not downloaded

                    DownloadConditions conditions = new DownloadConditions.Builder()
                            .requireWifi()
                            .build();
                    CoreTranslateText.modelManager.download(englishModel, conditions)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void v) {
                                    Core.sharedViewModel.setIsDownloadingModel(false);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error.
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
        CoreTranslateText.viewModel = null;
        CoreTranslateText.modelManager = null;

        //  hide keyboard when exiting from this activity because if keyboard is shown, the keyboard will not hide
        //  when exiting it, so hide it manually
        Core.hideKeyboard();
    }

}