package com.example.impotentbartender;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class PossibleActivity extends AppCompatActivity {

    LinearLayout llPossibleDrinks;
    ArrayList<String> keys;
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

        llPossibleDrinks = findViewById(R.id.llPossibleDrinks);

        owned = JsonIO.load(context, "owned");
        try {
            if (owned.opt("list") ==  null)
            {
                owned.put("list", new JSONArray());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try
        {
            keys = PossibleDrinks.getPossibleDrinks(context, PossibleDrinks.jsonListToStringArray(owned.getJSONArray("list")), allCocktails);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        populate();
    }

    void populate()
    {
        int n = 0;
        llPossibleDrinks.removeAllViews();
        for (String k: keys)
        {
            Log.d("fuck", k);
            Cocktail ttt = new Cocktail(context, k, allCocktails);
            llPossibleDrinks.addView( ttt.getPreview());
        }
    }
}
