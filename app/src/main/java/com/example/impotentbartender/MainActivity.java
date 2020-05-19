package com.example.impotentbartender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnOwned;
    Button btnUpdate;
    Button btnJsonList;
    Button btnPossible;
    Button btnSuggest;
    Button btnRaters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = this;

        btnOwned = (Button)findViewById(R.id.btnOwned);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnJsonList = (Button)findViewById(R.id.btnJsonList);
        btnPossible = (Button)findViewById(R.id.btnPossible);
        btnSuggest = (Button)findViewById(R.id.btnSuggest);
        btnRaters = (Button)findViewById(R.id.btnRaters);

        btnOwned.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, OwnedActivity.class);
                startActivity(intent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, UpdateActivity.class);
                startActivity(intent);
            }
        });

        btnJsonList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, AAAAAAAAAAAAAAAAAAAAAA.class);
                startActivity(intent);
            }
        });

        btnPossible.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, PossibleActivity.class);
                startActivity(intent);
            }
        });

        btnSuggest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, SuggestActivity.class);
                startActivity(intent);
            }
        });

        btnPossible.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, PossibleActivity.class);
                startActivity(intent);
            }
        });

        btnRaters.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, RatersActivity.class);
                startActivity(intent);
            }
        });
    }
}
