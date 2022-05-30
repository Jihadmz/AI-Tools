package com.jihad.aitools.feature_extracttext.presentation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jihad.aitools.feature_extracttext.presentation.extract_text.ExtractTextFragment;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryFragment;

public class TabsAdapter extends FragmentStateAdapter {

    public TabsAdapter(@NonNull FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new ExtractTextFragment();
        }
        return new ExtractTextHistoryFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
