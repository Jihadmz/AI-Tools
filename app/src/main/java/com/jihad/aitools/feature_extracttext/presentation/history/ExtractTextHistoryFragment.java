package com.jihad.aitools.feature_extracttext.presentation.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jihad.aitools.R;
import com.jihad.aitools.databinding.FragmentExtractTextHistoryBinding;
import com.jihad.aitools.feature_extracttext.CoreET;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;

public class ExtractTextHistoryFragment extends Fragment {

    private FragmentExtractTextHistoryBinding binding;
    private boolean isFirstTime;
    public static ExtractTextHistoryFragment INSTANCE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isFirstTime = true;
        INSTANCE = this;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extract_text_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentExtractTextHistoryBinding.bind(view);

        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(CoreET.extractTextHistoryAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rv);

        if (isFirstTime) { // here we are ensuring that only the first time the user visits this screen
            // the animation will be played
            binding.rv.startLayoutAnimation();
            isFirstTime = false;
        }

        makeEmptyBoxVisible();
    }

    @Override
    public void onResume() {
        super.onResume();

        makeEmptyBoxVisible();
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
            ExtractTextEntity entity = CoreET.list.get(position);

            if (direction == ItemTouchHelper.LEFT){
                CoreET.extractTextHistoryViewModel.deleteEntity(entity);
            }
        }
    };

    public void makeEmptyBoxVisible(){
        if (CoreET.list.size() > 0)
            binding.laEmptyBox.setVisibility(View.INVISIBLE);
        else
            binding.laEmptyBox.setVisibility(View.VISIBLE);

    }
}