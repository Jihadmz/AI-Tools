package com.jihad.aitools.feature_translatetext.presentation.components;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.BottomsheetLanguagechooserBinding;
import com.jihad.aitools.feature_translatetext.CoreTranslateText;

public class DialogLanguageChooser extends Dialog {

    private BottomsheetLanguagechooserBinding binding;
    private final Context context;

    private boolean isEnglishAvailable;
    private boolean isGermanyAvailable;
    private boolean isFrenchAvailable;
    private boolean isArabicAvailable;

    public DialogLanguageChooser(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void create() {
        super.create();
        binding = BottomsheetLanguagechooserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getAttributes().windowAnimations = R.style.bottomSheetAnimations;
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        checkingIfDownloadNeeded("fr", binding.ivDownloadFrench);
        checkingIfDownloadNeeded("ar", binding.ivDownloadSaudi);
        checkingIfDownloadNeeded("de", binding.ivDownloadGermany);
        checkingIfDownloadNeeded("en", binding.ivDownloadEnglish);

        binding.rlUsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEnglishAvailable)
                    Toast.makeText(context, context.getString(R.string.DownloadItFirst), Toast.LENGTH_SHORT).show();
                else {
                    CoreTranslateText.viewModel.setChosenLanguage("ENGLISH");
                    dismiss();
                }
            }
        });

        binding.rlFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFrenchAvailable)
                    Toast.makeText(context, context.getString(R.string.DownloadItFirst), Toast.LENGTH_SHORT).show();
                else {
                    CoreTranslateText.viewModel.setChosenLanguage("FRENCH");
                    dismiss();
                }
            }
        });

        binding.rlGermany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGermanyAvailable)
                    Toast.makeText(context, context.getString(R.string.DownloadItFirst), Toast.LENGTH_SHORT).show();
                else {
                    CoreTranslateText.viewModel.setChosenLanguage("GERMANY");
                    dismiss();
                }
            }
        });

        binding.rlSaudiArabia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isArabicAvailable)
                    Toast.makeText(context, context.getString(R.string.DownloadItFirst), Toast.LENGTH_SHORT).show();
                else {
                    CoreTranslateText.viewModel.setChosenLanguage("ARABIC");
                    dismiss();
                }
            }
        });

        binding.ivDownloadSaudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoreTranslateText.viewModel.setIsDownloadingModel(true);
                downloadingLanguageModel("ar", binding.ivDownloadSaudi);
            }
        });

        binding.ivDownloadFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoreTranslateText.viewModel.setIsDownloadingModel(true);
                downloadingLanguageModel("fr", binding.ivDownloadFrench);
            }
        });

        binding.ivDownloadEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoreTranslateText.viewModel.setIsDownloadingModel(true);
                downloadingLanguageModel("en", binding.ivDownloadEnglish);
            }
        });

        binding.ivDownloadGermany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoreTranslateText.viewModel.setIsDownloadingModel(true);
                downloadingLanguageModel("de", binding.ivDownloadGermany);
            }
        });

    }

    private void checkingIfDownloadNeeded(String languageCode, ImageView imageView){
        TranslateRemoteModel model =
                new TranslateRemoteModel.Builder(languageCode).build();

        CoreTranslateText.modelManager.isModelDownloaded(model).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean){ // not downloaded
                    imageView.setVisibility(View.VISIBLE);
                    switch (languageCode){
                        case "en": isEnglishAvailable = false; break;
                        case "fr": isFrenchAvailable = false; break;
                        case "de": isGermanyAvailable = false; break;
                        case "ar": isArabicAvailable = false; break;
                    }
                }else { // downloaded
                    switch (languageCode) {
                        case "en":
                            isEnglishAvailable = true;
                            break;
                        case "fr":
                            isFrenchAvailable = true;
                            break;
                        case "de":
                            isGermanyAvailable = true;
                            break;
                        case "ar":
                            isArabicAvailable = true;
                            break;
                    }
                }
            }
        });
    }

    private void downloadingLanguageModel(String languageCode, ImageView imageView){

        // Download a model.
        TranslateRemoteModel frenchModel =
                new TranslateRemoteModel.Builder(languageCode).build();
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        CoreTranslateText.modelManager.download(frenchModel, conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        CoreTranslateText.viewModel.setIsDownloadingModel(false);
                        imageView.setVisibility(View.INVISIBLE);
                        checkingIfDownloadNeeded(languageCode, imageView);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error.
                    }
                });
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);

        binding = null;
    }
}
