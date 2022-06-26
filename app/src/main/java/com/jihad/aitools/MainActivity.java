package com.jihad.aitools;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Messenger;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jihad.aitools.databinding.ActivityMainBinding;
import com.jihad.aitools.feature_extracttext.presentation.TextExtractionActivity;
import com.jihad.aitools.feature_recognizedigitalink.presentation.RecognizeDigitalInkActivity;
import com.jihad.aitools.feature_translatetext.presentation.TranslateTextActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.laEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TextExtractionActivity.class);
                startActivity(intent);
            }
        });

        binding.laTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), TranslateTextActivity.class));
            }
        });

        binding.laDigitalInk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), RecognizeDigitalInkActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}