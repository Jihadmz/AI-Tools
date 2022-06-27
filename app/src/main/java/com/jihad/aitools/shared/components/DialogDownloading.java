package com.jihad.aitools.shared.components;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jihad.aitools.databinding.DialogDownloadingBinding;

public class DialogDownloading extends Dialog {

    private DialogDownloadingBinding binding;
    private final Context context;

    public DialogDownloading(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    @Override
    public void create() {
        super.create();
        binding = DialogDownloadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);

    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);

        binding = null;
    }
}
