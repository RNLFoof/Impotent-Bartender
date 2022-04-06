package com.example.impotentbartender;

//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import net.rdrei.android.dirchooser.DirectoryChooserActivity;
//import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Misc
{
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String chooseDirectory(Activity activity)
    {
//        final int REQUEST_DIRECTORY = 0;
//        final String TAG = "DirChooserSample";
//
//        final Intent chooserIntent = new Intent(
//                activity,
//                DirectoryChooserActivity.class);
//
//        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
//                .newDirectoryName("DirChooserSample")
//                .allowReadOnlyDirectory(false)
//                .allowNewDirectoryNameModification(true)
//                .build();
//
//        chooserIntent.putExtra(
//                DirectoryChooserActivity.EXTRA_CONFIG,
//                config);
//
//        activity.startActivityForResult(chooserIntent, REQUEST_DIRECTORY);
//        return DirectoryChooserActivity.RESULT_SELECTED_DIR;
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
//        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "cocktails.json");

        activity.startActivityForResult(intent, 1);
        return "lol";
    }
}
