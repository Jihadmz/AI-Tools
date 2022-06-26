package com.jihad.aitools;

import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Core {

    public static SharedViewModel sharedViewModel;
    public static Application application;
    public static ClipboardManager clipboardManager;

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

    public static void copyToClipboard(String text){
        ClipData clipData = ClipData.newPlainText("Translated Text",text);
        clipboardManager.setPrimaryClip(clipData);
    }

    public static void pasteFromClipboardInToEditText(EditText editText){
        ClipData clipData = clipboardManager.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        editText.setText(editText.getText().toString() + item.getText().toString());
        editText.setSelection(editText.getText().length());
    }

    public static void showToast(String text, int length){
        Toast.makeText(application, text, length).show();
    }
}
