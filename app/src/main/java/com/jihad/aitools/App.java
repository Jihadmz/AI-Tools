package com.jihad.aitools;

import android.app.Application;
import android.os.Handler;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.jihad.aitools.feature_translatetext.presentation.components.DialogDownloading;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 3000);
    }
}
