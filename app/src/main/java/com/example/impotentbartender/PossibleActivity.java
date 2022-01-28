package com.example.impotentbartender;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class PossibleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FlowLayout flPossibleDrinks;
    EditText etSearch;
    Spinner spSorts;
    TextView tvPage;
    SeekBar sbPage;
    Button btnPageUp;
    Button btnPageDown;
    ArrayList<Cocktail> shownCocktails;
    JSONObject allCocktails;
    JSONObject allIngredients;
    Context context;
    JSONObject owned;

    int itemsPerPage = 50;
    int onPage = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible);
        context = this;

        allCocktails = JsonIO.load(context, R.raw.allcocktails);
        allIngredients = JsonIO.load(context, R.raw.allingredients);

        flPossibleDrinks = findViewById(R.id.flPossibleDrinks);
        etSearch = findViewById(R.id.etSearch);
        spSorts = findViewById(R.id.spSorts);
        tvPage = findViewById(R.id.tvPage);
        sbPage = findViewById(R.id.sbPage);
        btnPageUp = findViewById(R.id.btnPageUp);
        btnPageDown = findViewById(R.id.btnPageDown);

        spSorts.setOnItemSelectedListener(this);

        owned = JsonIO.load(context, "owned");
        try
        {
            if (owned.opt("list") ==  null)
            {
                owned.put("list", new JSONArray());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        try
        {
            shownCocktails = PossibleDrinks.getPossibleDrinks(context, PossibleDrinks.jsonListToStringArray(owned.getJSONArray("list")), Cocktail.getAllCocktails(context, true), true);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        populate();

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                sbPage.setProgress(0, true);
                populate();
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        sbPage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onPage = progress;
                tvPage.setText("Page " + String.format("%-2d", progress+1));
                populate();
            };

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            };

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            };
        });

        btnPageUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sbPage.setProgress(sbPage.getProgress()+1, true);
                populate();
            }
        });

        btnPageDown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sbPage.setProgress(sbPage.getProgress()-1, true);
                populate();
            }
        });
    };

    void populate()
    {
        int n = 0;
        flPossibleDrinks.removeAllViews();
        for (Cocktail cock: shownCocktails)
        {
            if (cock.matchesSearch(etSearch.getText().toString())) {
                if (Math.floor(n / itemsPerPage) == onPage) {
                    flPossibleDrinks.addView(cock.getPreview());
                };
                n += 1;
            }
        }
        sbPage.setMax((int) Math.floor(n / itemsPerPage));
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int pos, long id) {
        if (parent.getItemAtPosition(pos).toString().equals("Name"))
        {
            Collections.sort(shownCocktails, (a, b) -> a.name.compareTo(b.name));
        }
        else if (parent.getItemAtPosition(pos).toString().equals("Complexity"))
        {
            Collections.sort(shownCocktails, (a, b) -> Integer.compare(b.ingredients.size(), a.ingredients.size()));
        }
        else if (parent.getItemAtPosition(pos).toString().equals("Random"))
        {
            Collections.shuffle(shownCocktails);
        }
        populate();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
