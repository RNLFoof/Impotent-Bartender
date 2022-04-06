package com.example.impotentbartender;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnOwned;
    Button btnUpdate;
    Button btnJsonList;
    Button btnPossible;
    Button btnSuggest;
    Button btnRaters;
    Button btnExport;

    Button btnOwnedHelp;
    Button btnJsonListHelp;
    Button btnPossibleHelp;
    Button btnSuggestHelp;
    Button btnExportHelp;

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
        btnExport = (Button)findViewById(R.id.btnExport);

        btnOwnedHelp = (Button)findViewById(R.id.btnOwnedHelp);
        btnJsonListHelp = (Button)findViewById(R.id.btnJsonListHelp);
        btnPossibleHelp = (Button)findViewById(R.id.btnPossibleHelp);
        btnSuggestHelp = (Button)findViewById(R.id.btnSuggestHelp);
        btnExportHelp = (Button)findViewById(R.id.btnExportHelp);

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

        btnExport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, ExportActivity.class);
                startActivity(intent);
            }
        });



        btnOwnedHelp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Red things you don't have, green things you do. Tap to toggle. The icon on the left indicates if it's alcoholic or not. The number on the right indicates how many cocktails this ingredient appears in.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnSuggestHelp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("\"Alc\" and \"Non\" are alcoholic and non-alcoholic ingredients respectively. The filter uses regex and counts if it matches the name of the drink or any of the ingredients. Use semicolons to separate multiple regular expressions that must all be matched.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnPossibleHelp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Can be sorted by name, number of ingredients (complexity), and shuffled. The search uses regex and counts if it matches the name of the drink or any of the ingredients. Use semicolons to separate multiple regular expressions that must all be matched.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnJsonListHelp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("For if you need to edit the save data directly for some reason. I'm pretty sure whatever text editor you use is gonna need root access. Die");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnExportHelp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Lets you save your possible recipes as a json file.");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
