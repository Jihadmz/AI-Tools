package com.jihad.aitools.feature_translatetext.presentation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityTranslateTextBinding;
import com.jihad.aitools.feature_extracttext.CoreET;
import com.jihad.aitools.feature_translatetext.CoreTranslateText;
import com.jihad.aitools.feature_translatetext.presentation.components.DialogDownloading;
import com.jihad.aitools.feature_translatetext.presentation.components.DialogLanguageChooser;

public class TranslateTextActivity extends AppCompatActivity {

    private ActivityTranslateTextBinding binding;
    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranslateTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CoreTranslateText.application = getApplication();
        CoreTranslateText.viewModel = new ViewModelProvider(this).get(ViewModelTranslateText.class);
        CoreTranslateText.modelManager = RemoteModelManager.getInstance();

        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.TranslateText));
        actionBar.setDisplayHomeAsUpEnabled(true);

        CoreTranslateText.viewModel.getTextToTranslate().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.etTextTranslate.setText(s);
            }
        });

        CoreTranslateText.viewModel.getTranslatedText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvTranslatedText.setText(s);
            }
        });

        CoreTranslateText.viewModel.getChosenLanguageId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvChosenId.setText(s);
            }
        });

        CoreTranslateText.viewModel.getSourceLanguageId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvSourceId.setText(gettingSourceLanguage(s));
            }
        });

        DialogDownloading dialogDownloading = new DialogDownloading(this);
        CoreTranslateText.viewModel.getIsDownloadingModel().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    dialogDownloading.create();
                    dialogDownloading.show();
                } else
                    dialogDownloading.dismiss();
            }
        });

        binding.tvChosenId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogLanguageChooser dialogLanguageChooser = new DialogLanguageChooser(view.getContext());
                dialogLanguageChooser.create();
                dialogLanguageChooser.show();
            }
        });


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
                                        if (!languageCode.equals("und")) {
                                            CoreTranslateText.viewModel.setSourceLanguageCode(languageCode);
                                            translating(languageCode, editable.toString());
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

        binding.ivPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteFromClipboard();
            }
        });

        binding.cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etTextTranslate.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });

        if (getIntent().hasExtra("ExtractedText")) { // user coming from Extracted text fragment with the text being copied
            String text = getIntent().getStringExtra("ExtractedText");
            binding.etTextTranslate.setText(text);
        }

        downloadEnglishLanguageModel();

    }

    private void pasteFromClipboard(){
        ClipData clipData = clipboardManager.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        binding.etTextTranslate.setText(binding.etTextTranslate.getText().toString() + item.getText().toString());
        binding.etTextTranslate.setSelection(binding.etTextTranslate.getText().length());
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

    private String gettingChosenLanguageCode() {
        switch (binding.tvChosenId.getText().toString()) {
            case "ENGLISH":
                return "en";
            case "FRENCH":
                return "fr";
            case "GERMANY":
                return "de";
            case "ARABIC":
                return "ar";
            default:
                return "unk";
        }
    }

    private void downloadEnglishLanguageModel() {
        // Download a model.
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
                                    CoreTranslateText.viewModel.setIsDownloadingModel(false);
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

    private void translating(String languageCode, String text) {

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(languageCode)
                        .setTargetLanguage(gettingChosenLanguageCode())
                        .build();
        final Translator translator = Translation.getClient(options);

        translator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                // Translation successful.
                                CoreTranslateText.viewModel.setTranslatedText(translatedText);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
        CoreTranslateText.viewModel = null;
        CoreTranslateText.application = null;
        CoreTranslateText.modelManager = null;
    }
}