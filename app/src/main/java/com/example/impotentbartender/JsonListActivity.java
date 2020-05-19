package com.example.impotentbartender;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;














// GO AWAY



















public class JsonListActivity extends AppCompatActivity
{
    LinearLayout stopwastingmyfuckingtime;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned);
        context = this;

        stopwastingmyfuckingtime = findViewById(R.id.stopwastingmyfuckingtime);

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
                    Intent intent = new Intent(getBaseContext(), EditJsonActivity.class);
                    Log.d("fuck", files[finalX].getName());
                    intent.putExtra("json", JsonIO.load(context, files[finalX].getName()).toString());
                    intent.putExtra("filename", files[finalX].getName());

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(files[finalX].toURI().toString())));
                    // startActivity(intent);
                }
            });

            stopwastingmyfuckingtime.addView(tvItem);
        }
    }
}