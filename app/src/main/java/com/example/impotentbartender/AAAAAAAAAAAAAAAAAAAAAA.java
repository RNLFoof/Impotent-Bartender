package com.example.impotentbartender;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AAAAAAAAAAAAAAAAAAAAAA extends AppCompatActivity {

    Context context;
    LinearLayout llAAAAAAAAAAAAAAAAA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aaaaaaaaaaaaaaaaaaaaa);
        context = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //StrictMode.setVmPolicy(builder.build());

        llAAAAAAAAAAAAAAAAA = findViewById(R.id.llAAAAAAAAAAAAAAAAA);

        final File[] files = getFilesDir().listFiles();
        for(int x = 0; x<files.length; x++)
        {
            TextView tvItem = new TextView(context);

            tvItem.setText(files[x].getName());
            tvItem.setTextSize(20);

            final int finalX = x;
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepareFile(context, files[finalX].getName());
                    Intent viewDoc = new Intent(Intent.ACTION_VIEW);
                    viewDoc.setDataAndType(
                            Uri.fromFile(getFileStreamPath(files[finalX].getName())),
                            "text/plain");

                    PackageManager pm = getPackageManager();
                    List<ResolveInfo> apps =
                            pm.queryIntentActivities(viewDoc, PackageManager.MATCH_DEFAULT_ONLY);

                    if (apps.size() > 0)
                        startActivity(viewDoc);

                    return;

//                    Intent intent = new Intent(getBaseContext(), EditJsonActivity.class);
//                    Log.d("fuck", files[finalX].getName());
//                    intent.putExtra("json", JsonIO.load(context, files[finalX].getName()).toString());
//                    intent.putExtra("filename", files[finalX].getName());
//                    // startActivity(intent);
//                    // FileProvider.getUriForFile(this,"your_package.fileprovider",photoFile);
//
//                    Intent actually = new Intent(Intent.ACTION_VIEW, Uri.parse(files[finalX].toURI().toString()));
//                    actually.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    startActivity(actually);
                }
            });

            Log.d("fuck", llAAAAAAAAAAAAAAAAA.toString());
            Log.d("fuck", tvItem.toString());
            llAAAAAAAAAAAAAAAAA.addView(tvItem);
        }
    }

    void prepareFile(Context context, String fileName)
    {
        String json = JsonIO.load(context, fileName).toString();
        StringBuilder sb = new StringBuilder();
        String spaces = "";
        String spaceInterval = "    ";
        for (int x = 0; x<json.length(); x++)
        {
            char c = json.charAt(x);
            if (c == '[' || c == '{')
            {
                sb.append("\n" + spaces + c);
                spaces += spaceInterval;
                sb.append("\n" + spaces);
            }
            else if (c == ']' || c == '}')
            {
                spaces = spaces.replaceFirst(spaceInterval, "");
                sb.append("\n" + spaces + c);
                sb.append("\n" + spaces);
            }
            else if (c == '\n')
            {
                sb.append(spaces + c);
            }
            else if (c == ',')
            {
                sb.append(c + "\n" + spaces);
            }
            else
            {
                sb.append(c);
            }
        }

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("fuck2", "ow");
        } catch (IOException e) {
            Log.d("fuck2", "oww");
        }
    }

}
