package com.jihad.aitools.feature_extracttext.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityTextExtractionBinding;

public class TextExtractionActivity extends AppCompatActivity {

    private ActivityTextExtractionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTextExtractionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.ExtractText));
        actionBar.setDisplayHomeAsUpEnabled(true);

        settingTabs();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void settingTabs(){
        FragmentManager fm = getSupportFragmentManager();
        TabsAdapter adapter = new TabsAdapter(fm, getLifecycle());
        binding.vp.setAdapter(adapter);
        binding.vp.setUserInputEnabled(false);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tl, binding.vp, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText(getString(R.string.ExtractText));
                }else
                    tab.setText(getString(R.string.History));
            }
        });
        tabLayoutMediator.attach();

    }
}