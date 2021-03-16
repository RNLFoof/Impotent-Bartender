package com.example.impotentbartender;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Cocktail
{
    Context context;
    String name;
    String instructions;
    String source;
    ArrayList<String> optional;
    ArrayList<HashMap<String, String>> ingredients;
    HashSet<String> ingredientset;
    LinearLayout preview;
    int index;

    public Cocktail(Context context, int index)
    {
        IMissPython(context, index, JsonIO.loadArray(context, R.raw.allcocktails));
    }

    public Cocktail(Context context, int index, JSONArray allCocktails)
    {
        IMissPython(context, index, allCocktails);
    }

    private void IMissPython(Context context, int index, JSONArray allCocktails)
    {
        this.context = context;
        try
        {
            JSONObject current = allCocktails.getJSONObject(index);
            this.name = current.getString("name");;
            this.index = index;
            this.instructions = current.getString("instructions");
            this.source = current.getString("source");

            optional = new ArrayList<>();
            if (current.has("optional"))
            {
                JSONArray optionaljson = current.getJSONArray("optional");
                for (int i=0; i<optionaljson.length(); i++)
                {
                    optional.add(optionaljson.getString(i));
                }
            }

            ingredients = new ArrayList<>();
            ingredientset = new HashSet<>();
            JSONArray ingredientsjson = current.getJSONArray("ingredients");
            for (int i=0; i<ingredientsjson.length(); i++)
            {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("ingredient", ingredientsjson.getJSONObject(i).getString("ingredient"));
                hm.put("quantity", ingredientsjson.getJSONObject(i).getString("quantity"));
                hm.put("unit", ingredientsjson.getJSONObject(i).getString("unit"));
                ingredients.add(hm);
                ingredientset.add(ingredientsjson.getJSONObject(i).getString("ingredient"));
            }
        }
        catch (JSONException e) {
            Log.d("fuck", "uh");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.d("fuck", exceptionAsString);
        }
    }



    public LinearLayout getPreview()
    {
        LinearLayout ret = new LinearLayout(context);
        ret.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600, 1000);
        ret.setPadding(10, 10, 10, 10);
        ret.setLayoutParams(layoutParams);

        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(3, 0xFF000000); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            ret.setBackgroundDrawable(border);
        } else {
            ret.setBackground(border);
        }

        TextView tvName = new TextView(context);
        tvName.setText(name);
        tvName.setTextSize(30f);
        ret.addView(tvName);

        ImageView ivPic = new ImageView(context);
        int resID = context.getResources().getIdentifier(("drinkimg_"+name.toLowerCase().replaceAll("[^a-z0-9]","_"))+"_small" , "drawable", context.getPackageName());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(500, 500);
        ivPic.setLayoutParams(layoutParams2);
        ivPic.setImageResource(resID);
        ret.addView(ivPic);

        TextView tvIng = new TextView(context);
        StringBuilder sb = new StringBuilder();
        for (HashMap<String, String> x: ingredients)
        {
            sb.append(x.get("ingredient"));
            sb.append(", ");
        }
        tvIng.setText(sb.substring(0, sb.length()-2));
        tvIng.setTextSize(20f);
        ret.addView(tvIng);

        ret.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CocktailInfoActivity.class);
                intent.putExtra("index", index);
                context.startActivity(intent);
            }
        });

//        Handler handler = new Handler();
//        int delay = 5000; //milliseconds
//
//        handler.postDelayed(new Runnable(){
//            public void run(){
//                if (ivPic.getVisibility() != View.GONE)
//                {
//                    ivPic.setImageResource(resID);
//                    Log.d("fuck", "nah");
//                }
//                else
//                {
//                    Log.d("fuck", "yeah");
//                    ivPic.setImageResource(0);
//                }
//                handler.postDelayed(this, delay);
//            }
//        }, delay);

        preview = ret;

        return ret;
    }

    public static ArrayList<Cocktail> getAllCocktails(Context context)
    {
        ArrayList<Cocktail> ret = new ArrayList<>();
        JSONArray allCocktails = JsonIO.loadArray(context, R.raw.allcocktails);
        for (int x = 0; x < allCocktails.length();  x++)
        {
            ret.add(new Cocktail(context, x, allCocktails));
        }
        return ret;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (obj instanceof Cocktail)
        {
            return this.name.equals(((Cocktail)obj).name);
        }
        return false;
    }
}
