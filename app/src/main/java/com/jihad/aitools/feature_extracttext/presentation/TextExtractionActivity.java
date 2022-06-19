package com.jihad.aitools.feature_extracttext.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityTextExtractionBinding;
import com.jihad.aitools.feature_extracttext.Core;
import com.jihad.aitools.feature_extracttext.presentation.extract_text.ExtractTextViewModel;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryViewModel;

public class TextExtractionActivity extends AppCompatActivity {

    private ActivityTextExtractionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Core.extractTextViewModel = new ViewModelProvider(this).get(ExtractTextViewModel.class);
        Core.extractTextHistoryViewModel = new ViewModelProvider(this).get(ExtractTextHistoryViewModel.class);

        binding = ActivityTextExtractionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.ExtractText));
        actionBar.setDisplayHomeAsUpEnabled(true);

        settingTabs();
    }

    /**
     * do free up memory by eliminating text extraction specific variables
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        Core.extractTextHistoryViewModel = null;
        Core.extractTextViewModel = null;
        binding = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.extract_text,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mu_deleteAll){
//            if (Core.extractTextHistoryViewModel.getExtractTextEntityList().getValue().size() > 0){
//                Dialog dialog = new Dialog(this);
//            }
        }
        return super.onOptionsItemSelected(item);
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