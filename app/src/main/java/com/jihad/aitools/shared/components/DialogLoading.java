package com.jihad.aitools.shared.components;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.jihad.aitools.R;

public class DialogLoading extends Dialog {

    public DialogLoading(@NonNull Context context) {
        super(context);
    }

    @Override
    public void create() {
        super.create();
        setContentView(R.layout.dialog_loading);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(false);
    }
}
