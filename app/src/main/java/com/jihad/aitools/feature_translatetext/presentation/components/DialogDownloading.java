package com.jihad.aitools.feature_translatetext.presentation.components;

import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.jihad.aitools.Core;
import com.jihad.aitools.R;
import com.jihad.aitools.databinding.DialogDownloadingBinding;
import com.jihad.aitools.feature_translatetext.CoreTranslateText;

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
