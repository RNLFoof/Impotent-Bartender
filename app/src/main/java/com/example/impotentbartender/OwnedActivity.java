package com.example.impotentbartender;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
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

public class OwnedActivity extends AppCompatActivity
{
    LinearLayout llList;
    JSONObject allIngredients;
    ArrayList<String> keys;
    Context context;
    JSONObject owned;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned);
        context = this;

        llList = findViewById(R.id.llOwnedList);

        owned = JsonIO.load(context, "owned");
        Log.d("fuck", owned.toString());
        try {
            if (owned.opt("list") ==  null)
            {
                owned.put("list", new JSONArray());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("fuck", owned.toString());

        allIngredients = JsonIO.load(context, R.raw.allingredients);
        keys = new ArrayList<>();
        Iterator<String> ks = allIngredients.keys();
        while (ks.hasNext())
        {
            keys.add(ks.next());
        }
        keys.sort(String::compareToIgnoreCase);

        populate();
        Log.d("fuck", owned.toString());
    }

    void populate()
    {
        try {
            llList.removeAllViews();
            for (String k: keys)
            {
                TextView tvItem = new TextView(context);
                JSONObject ing = allIngredients.getJSONObject(k);

                tvItem.setText(Aesthetic.ingredientString(ing));
                tvItem.setTextSize(30);

                tvItem.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        try {
                            JSONArray l = owned.getJSONArray("list");
                            String t = getIngredientFromTextView((TextView)v);
                            int pos = JSONArrayPos(l, t);
                            if (pos == -1)
                            {
                                l.put(t);
                            }
                            else
                            {
                                l.remove(pos);
                            }
                            setColor((TextView)v);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonIO.save(context, "owned", owned.toString());
                        Log.d("fuck", owned.toString());
                    }
                });

                llList.addView(tvItem);
                setColor(tvItem);
            }
        } catch (JSONException e) {
        e.printStackTrace();
    }
    }

    void setColor(TextView tv)
    {
        try {
            if (JSONArrayPos(owned.getJSONArray("list"), getIngredientFromTextView(tv)) == -1)
            {
                tv.setBackgroundColor(Color.RED);
            }
            else
            {
                tv.setBackgroundColor(Color.GREEN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String getIngredientFromTextView(TextView tv)
    {
        return keys.get(llList.indexOfChild(tv));
    }

    int JSONArrayPos(JSONArray l, Object looking)
    {
        try {
            for (int i = 0; i < l.length(); i++)
                if (l.get(i).equals(looking))
                    return i;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}