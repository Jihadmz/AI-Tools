package com.jihad.aitools.feature_extracttext.presentation.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.FragmentExtractTextHistoryBinding;
import com.jihad.aitools.feature_extracttext.Core;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;
import com.jihad.aitools.feature_extracttext.presentation.extract_text.ExtractTextViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExtractTextHistoryFragment extends Fragment {

    private FragmentExtractTextHistoryBinding binding;
    private ExtractTextHistoryAdapter adapter;
    private List<ExtractTextEntity> list;
    private boolean isFirstTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isFirstTime = true;
        list = new ArrayList<>();
        TabLayout tabLayout = (TabLayout) requireActivity().findViewById(R.id.tl);
        adapter = new ExtractTextHistoryAdapter(
                requireContext(),
                list,
                tabLayout);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extract_text_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentExtractTextHistoryBinding.bind(view);

        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rv);

        Core.extractTextHistoryViewModel.getExtractTextEntityList().observe(requireActivity(), new Observer<List<ExtractTextEntity>>() {
            @Override
            public void onChanged(List<ExtractTextEntity> extractTextEntities) {
                if (extractTextEntities.size() > 0) {
                    list = extractTextEntities;
                    adapter.setEntities(list);
                    if (isFirstTime) { // here we are ensuring that only the first time the user visits this screen
                        // the animation will be played
                        binding.rv.startLayoutAnimation();
                        isFirstTime = false;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            ExtractTextEntity entity = list.get(position);

            if (direction == ItemTouchHelper.LEFT){
                Core.extractTextHistoryViewModel.deleteEntity(entity);
            }
        }
    };
}