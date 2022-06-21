package com.jihad.aitools.feature_extracttext.presentation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jihad.aitools.Core;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.ActivityTextExtractionBinding;
import com.jihad.aitools.feature_extracttext.CoreET;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;
import com.jihad.aitools.feature_extracttext.presentation.components.DialogDeleteAll;
import com.jihad.aitools.feature_extracttext.presentation.extract_text.ExtractTextViewModel;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryAdapter;
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

        CoreET.extractTextViewModel = new ViewModelProvider(this).get(ExtractTextViewModel.class);
        CoreET.extractTextHistoryViewModel = new ViewModelProvider(this).get(ExtractTextHistoryViewModel.class);
        CoreET.list = new ArrayList<>();
        CoreET.extractTextHistoryAdapter = new ExtractTextHistoryAdapter(this, CoreET.list, binding.tl);

        //  Setting up actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(getString(R.string.ExtractText));
        actionBar.setDisplayHomeAsUpEnabled(true);

        settingTabs();

        CoreET.extractTextHistoryViewModel.list.observe(this, new Observer<List<ExtractTextEntity>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ExtractTextEntity> extractTextEntities) {
                if (extractTextEntities.size() > 0){
                    CoreET.list = extractTextEntities;
                    CoreET.extractTextHistoryAdapter.setEntities(CoreET.list);
                }else{
                    CoreET.extractTextHistoryAdapter.notifyDataSetChanged();
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

        CoreET.extractTextHistoryViewModel = null;
        CoreET.extractTextViewModel = null;
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

            if (CoreET.list.size() > 0) { // if there is entities
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
        else if (item.getItemId() == R.id.mu_camera){
            //  if permission is granted open the camera app, else request it
            if (Core.checkPermission(this,Manifest.permission.CAMERA, 1)){
                Core.startAnAction(this, MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, null, null);
            }
        }
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, getString(R.string.PermissionGranted), Toast.LENGTH_SHORT).show();
                Core.startAnAction(this, MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA, null, null);
            }
        }
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