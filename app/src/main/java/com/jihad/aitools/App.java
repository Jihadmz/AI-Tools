package com.jihad.aitools;

import android.app.Application;
import android.os.Handler;

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
