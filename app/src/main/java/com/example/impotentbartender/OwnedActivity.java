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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;



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

    @RequiresApi(api = Build.VERSION_CODES.N)
    void populate()
    {
        llList.removeAllViews();
        try
        {
            for (String k: keys)
            {
                if (allIngredients.getJSONObject(k).getString("variantOf").equals("null"))
                {
                    addSingleIng(k, 0);
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    IngredientRep addSingleIng(String k, int layer)
    {
        try
        {
            IngredientRep tvItem = new IngredientRep(context, k);
            JSONObject ing = allIngredients.getJSONObject(k);

            tvItem.setText(String.format("%s%s", new String(new char[4*layer]).replace("\0", " "), Aesthetic.ingredientString(ing)));
            tvItem.setTextSize(30);

            llList.addView(tvItem);
            setColor(tvItem);

            for (String kk: keys)
            {
                Log.d("hhh", allIngredients.getJSONObject(kk).getString("variantOf"));
                if (allIngredients.getJSONObject(kk).getString("variantOf").equals(k))
                {
                    Log.d("hhh", "why");
                    tvItem.iHide.add(addSingleIng(kk, layer+1));
                }
            }


            tvItem.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    try {
                        JSONArray l = owned.getJSONArray("list");
                        String t = ((IngredientRep)v).ingredientStr;
                        int pos = JSONArrayPos(l, t);
                        if (pos == -1)
                        {
                            l.put(t);
                            for (IngredientRep x: tvItem.iHide)
                            {
                                x.setVisibility(View.VISIBLE);
                                x.setChildVisibility();
                            }
                        }
                        else
                        {
                            l.remove(pos);
                            for (IngredientRep x: tvItem.iHide)
                            {
                                x.setVisibility(View.GONE);
                                x.setChildVisibility();
                            }
                        }
                        setColor((IngredientRep) v);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonIO.save(context, "owned", owned.toString());
                    Log.d("fuck", owned.toString());
                }
            });

            return tvItem;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return new IngredientRep(context, "fuck");
    }

    void setColor(IngredientRep tv)
    {
        try {
            if (JSONArrayPos(owned.getJSONArray("list"), tv.ingredientStr) == -1)
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

    class IngredientRep extends androidx.appcompat.widget.AppCompatTextView
    {
        ArrayList<IngredientRep> iHide = new ArrayList<>();
        String ingredientStr;
        JSONObject ing;

        public IngredientRep(Context context, String ingredientStr)
        {
            super(context);
            try
            {
                ing = allIngredients.getJSONObject(ingredientStr);
                this.ingredientStr = ingredientStr;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void setChildVisibility()
        {
            try
            {
                if (ing.getJSONArray("variants").length() != 0)
                {
                    JSONArray l = owned.getJSONArray("list");
                    String t = this.ingredientStr;
                    int pos = JSONArrayPos(l, t);
                    for(int index = 0; index < llList.getChildCount(); index++)
                    {
                        IngredientRep ir = (IngredientRep) llList.getChildAt(index);
                        if (allIngredients.getJSONObject(ir.ingredientStr).getString("variantOf").equals(ingredientStr))
                        {
                            if (pos == -1 || getVisibility() == View.GONE)
                            {
                                ir.setVisibility(View.GONE);
                            }
                            else
                            {
                                ir.setVisibility(View.VISIBLE);
                            }
                            ir.setChildVisibility();
                        }
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}