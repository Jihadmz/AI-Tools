package com.jihad.aitools.feature_translatetext;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.jihad.aitools.feature_translatetext.presentation.ViewModelTranslateText;

public class CoreTranslateText {

    public static ViewModelTranslateText viewModel;
    public static RemoteModelManager modelManager;

    public static void translating(String languageCode, String text, String chosenLanguage) {

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(languageCode)
                        .setTargetLanguage(gettingChosenLanguageCode(chosenLanguage))
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

    private static String gettingChosenLanguageCode(String language) {
        switch (language) {
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
}
