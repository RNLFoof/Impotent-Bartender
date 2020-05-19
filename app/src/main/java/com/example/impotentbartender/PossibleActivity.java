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
        try {
            llPossibleDrinks.removeAllViews();
            for (String k: keys)
            {
                TextView tvItem = new TextView(context);
                JSONObject cock = allCocktails.getJSONObject(k);

                tvItem.setText(k);
                tvItem.setTextSize(30);

                tvItem.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CocktailInfoActivity.class);
                        intent.putExtra("json", cock.toString());
                        startActivity(intent);
                    }
                });

                llPossibleDrinks.addView(tvItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
