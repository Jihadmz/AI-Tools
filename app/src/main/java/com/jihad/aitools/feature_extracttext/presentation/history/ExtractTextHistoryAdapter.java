package com.jihad.aitools.feature_extracttext.presentation.history;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.jihad.aitools.databinding.ListitemExtracttexthistoryBinding;
import com.jihad.aitools.feature_extracttext.Core;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;
import com.jihad.aitools.feature_extracttext.presentation.extract_text.ExtractTextViewModel;

import java.io.FileNotFoundException;
import java.util.List;

public class ExtractTextHistoryAdapter extends RecyclerView.Adapter<ExtractTextHistoryAdapter.ViewHolder> {

    private List<ExtractTextEntity> entities;
    private Context context;
    private TabLayout tabLayout;

    public ExtractTextHistoryAdapter(Context context,
                                     List<ExtractTextEntity> entities,
                                     TabLayout tabLayout){
        this.entities = entities;
        this.context = context;
        this.tabLayout = tabLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ListitemExtracttexthistoryBinding.
                        inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExtractTextEntity entity = entities.get(position);

        holder.binding.tv.setText(entity.getText());

        holder.binding.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.selectTab(tabLayout.getTabAt(0), true);
                Core.extractTextViewModel.setExtractedText(holder.binding.tv.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public void setEntities(List<ExtractTextEntity> newList){
       DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return entities.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }
        });
        entities = newList;
        result.dispatchUpdatesTo(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ListitemExtracttexthistoryBinding binding;


        public ViewHolder(@NonNull ListitemExtracttexthistoryBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
