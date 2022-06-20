package com.jihad.aitools.feature_extracttext.presentation.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jihad.aitools.R;
import com.jihad.aitools.databinding.DialogDeleteallBinding;
import com.jihad.aitools.feature_extracttext.Core;
import com.jihad.aitools.feature_extracttext.presentation.history.ExtractTextHistoryFragment;

public class DialogDeleteAll extends Dialog {

    private DialogDeleteallBinding binding;

    public DialogDeleteAll(@NonNull Context context) {
        super(context);
    }

    @Override
    public void create() {
        super.create();
        binding = DialogDeleteallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().getAttributes().windowAnimations = R.style.dialogAnimations;

        binding.ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Core.extractTextHistoryViewModel.deleteAll();
                dismiss();
                //  to make the empty box visible
                ExtractTextHistoryFragment.INSTANCE.makeEmptyBoxVisible();
            }
        });

        binding.ivReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);

        binding = null;
    }
}
