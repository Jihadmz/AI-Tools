package com.jihad.aitools.feature_extracttext.presentation.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.jihad.aitools.databinding.ListitemExtracttexthistoryBinding;
import com.jihad.aitools.feature_extracttext.CoreET;
import com.jihad.aitools.feature_extracttext.domain.model.ExtractTextEntity;

import java.util.List;

public class ExtractTextHistoryAdapter extends RecyclerView.Adapter<ExtractTextHistoryAdapter.ViewHolder> {

    private List<ExtractTextEntity> entities;
    private final Context context;
    private final TabLayout tabLayout;

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

        // loading the bitmap into the imageview
        Glide.with(context).load(entity.getImage()).into(holder.binding.iv);

        /*
        * on history item click, we want to navigate the user back to the extract text fragment with the text and chosen
        * image of this entity
        */
        holder.binding.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.selectTab(tabLayout.getTabAt(0), true);
                CoreET.shouldAddEntity = false;
                CoreET.extractTextViewModel.setExtractedText(holder.binding.tv.getText().toString());
                CoreET.extractTextViewModel.setChosenImage(entity.getImage(), context.getContentResolver());
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
