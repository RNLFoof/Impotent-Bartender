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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PossibleActivity extends AppCompatActivity {

    FlowLayout flPossibleDrinks;
    EditText etSearch;
    ArrayList<Cocktail> shownCocktails;
    JSONObject allCocktails;
    Context context;
    JSONObject owned;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible);
        context = this;

        allCocktails = JsonIO.load(context, R.raw.allcocktails);

        flPossibleDrinks = findViewById(R.id.flPossibleDrinks);
        etSearch = findViewById(R.id.etSearch);

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
            shownCocktails = PossibleDrinks.getPossibleDrinks(context, PossibleDrinks.jsonListToStringArray(owned.getJSONArray("list")), Cocktail.getAllCocktails(context));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        populate();

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                for (Cocktail cock: shownCocktails)
                {
                    cock.preview.setVisibility(View.GONE);
                    if (cock.name.toLowerCase().contains(etSearch.getText().toString().toLowerCase()))
                    {
                        cock.preview.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        for (HashMap<String, String> ing: cock.ingredients)
                        {
                            if (ing.get("ingredient").toLowerCase().contains(etSearch.getText().toString().toLowerCase()))
                            {
                                cock.preview.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    void populate()
    {
        flPossibleDrinks.removeAllViews();
        for (Cocktail cock: shownCocktails)
        {
            flPossibleDrinks.addView( cock.getPreview());
        }
    }
}
