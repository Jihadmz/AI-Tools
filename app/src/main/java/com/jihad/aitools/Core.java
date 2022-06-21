package com.jihad.aitools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Core {

   public static boolean checkPermission(Activity activity, String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity, new String[] {permission}, requestCode);
            return false;
        }
        return true;
    }

    public static void startAnAction(Activity activity, String action, @Nullable Bundle extras, @Nullable String name){
        Intent intent = new Intent(action);
        if (extras != null && name != null){
            intent.putExtra(name, extras);
        }
        activity.startActivity(intent);
    }
}
