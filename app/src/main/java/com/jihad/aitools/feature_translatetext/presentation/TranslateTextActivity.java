package com.jihad.aitools.feature_translatetext.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityTranslateTextBinding;

public class TranslateTextActivity extends AppCompatActivity {

    private ActivityTranslateTextBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranslateTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.TranslateText));
        actionBar.setDisplayHomeAsUpEnabled(true);


        binding.etTextTranslate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                LanguageIdentifier languageIdentifier =
                        LanguageIdentification.getClient();
                languageIdentifier.identifyLanguage(editable.toString())
                        .addOnSuccessListener(
                                new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(@Nullable String languageCode) {
                                        if (languageCode.equals("und")) {
                                        } else {
                                            binding.tvSourceId.setText(gettingSourceLanguage(languageCode));

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    downloadingAndTranslating(editable.toString());
                                                }
                                            }, 1000);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private String gettingSourceLanguage(String languageCode){
        switch (languageCode){
            case "en": return "ENGLISH";
            case "de": return "GERMANY";
            case "fr": return "FRENCH";
            default: return "unk";
        }
    }

    private void downloadingAndTranslating(String text){

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage("en")
                        .setTargetLanguage("fr")
                        .build();
        final Translator translator = Translation.getClient(options);


        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                translator.translate(text)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<String>() {
                                                    @Override
                                                    public void onSuccess(@NonNull String translatedText) {
                                                        // Translation successful.
                                                        binding.tvTranslatedText.setText(translatedText);
                                                    }
                                                })
                                        .addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error.
                                                        // ...
                                                    }
                                                });
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                            }
                        });
    }
}