package com.example.impotentbartender;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

public class ExportActivity extends AppCompatActivity {
    Button btnExportDialog;
    final Activity context = this;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.CreateDocument(),
            new ActivityResultCallback<Uri>()
            {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onActivityResult(Uri uri)
                {
                    String json = Cocktail.getCocktailJsonString(context);
                    try {
                        // Write
                        OutputStream os = getContentResolver().openOutputStream(uri);
                        os.write(json.getBytes());
                        os.flush();
                        os.close();
                        // Say you writed
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("good work");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } catch (Exception e)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(e.getMessage());
                        e.printStackTrace();
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        btnExportDialog = (Button)findViewById(R.id.btnExportDialog);

        btnExportDialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                // Pass in the mime type you'd like to allow the user to select
                // as the input
                mGetContent.launch("impotentbartendercocktails.json");
            }
        });
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_export);
//
//        btnExportDialog = (Button)findViewById(R.id.btnExportDialog);
//
//        btnExportDialog.setOnClickListener(new View.OnClickListener()
//        {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v)
//            {
//                String saveTo = Misc.chooseDirectory(context);
//            }
//        });
//    }
}
