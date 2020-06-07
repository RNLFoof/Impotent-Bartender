package com.example.impotentbartender;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class PossibleActivity extends AppCompatActivity {

    FlowLayout flPossibleDrinks;
    ArrayList<Cocktail> keys;
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
            keys = PossibleDrinks.getPossibleDrinks(context, PossibleDrinks.jsonListToStringArray(owned.getJSONArray("list")), Cocktail.getAllCocktails(context));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        populate();
    }

    void populate()
    {
        flPossibleDrinks.removeAllViews();
        for (Cocktail cock: keys)
        {
            flPossibleDrinks.addView( cock.getPreview());
        }
    }
}
