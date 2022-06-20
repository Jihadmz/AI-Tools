package com.jihad.aitools.feature_extracttext.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityTextExtractionBinding;
import com.jihad.aitools.feature_extracttext.Core;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;
import com.jihad.aitools.feature_extracttext.presentation.components.DialogDeleteAll;
import com.jihad.aitools.feature_extracttext.presentation.extract_text.ExtractTextViewModel;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryAdapter;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryFragment;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class TextExtractionActivity extends AppCompatActivity {

    private ActivityTextExtractionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextExtractionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Core.extractTextViewModel = new ViewModelProvider(this).get(ExtractTextViewModel.class);
        Core.extractTextHistoryViewModel = new ViewModelProvider(this).get(ExtractTextHistoryViewModel.class);
        Core.list = new ArrayList<>();
        Core.extractTextHistoryAdapter = new ExtractTextHistoryAdapter(this, Core.list, binding.tl);

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.ExtractText));
        actionBar.setDisplayHomeAsUpEnabled(true);

        settingTabs();

        Core.extractTextHistoryViewModel.list.observe(this, new Observer<List<ExtractTextEntity>>() {
            @Override
            public void onChanged(List<ExtractTextEntity> extractTextEntities) {
                if (extractTextEntities.size() > 0){
                    Core.list = extractTextEntities;
                    Core.extractTextHistoryAdapter.setEntities(Core.list);
                }else{
                    Core.extractTextHistoryAdapter.notifyDataSetChanged();
                }
            }
        });
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
        if (item.getItemId() == R.id.mu_deleteAll) {
            // if the selected tab is not the history, navigate to it
            if (binding.tl.getSelectedTabPosition() == 0)
                binding.tl.selectTab(binding.tl.getTabAt(1));

            if (Core.list.size() > 0) { // if there is entities
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(TextExtractionActivity.this);
                dialogDeleteAll.create();

                //  showing the dialog after 300 millis to ensure that the entering anim is played
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogDeleteAll.show();
                    }
                }, 100);
            }
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