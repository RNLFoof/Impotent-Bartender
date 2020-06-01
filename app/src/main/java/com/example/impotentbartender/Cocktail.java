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

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Cocktail
{
    Context context;
    String name;
    String instructions;
    String source;
    ArrayList<String> optional;
    ArrayList<HashMap<String, String>> ingredients;

    public Cocktail(Context context, String name)
    {
        IMissPython(context, name, JsonIO.load(context, R.raw.allcocktails));
    }

    public Cocktail(Context context, String name, JSONObject allCocktails)
    {
        IMissPython(context, name, allCocktails);
    }

    private void IMissPython(Context context, String name, JSONObject allCocktails)
    {
        this.context = context;
        try
        {
            JSONObject current = allCocktails.getJSONObject(name);
            this.name = name;
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
            JSONArray ingredientsjson = current.getJSONArray("ingredients");
            for (int i=0; i<ingredientsjson.length(); i++)
            {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("ingredient", ingredientsjson.getJSONObject(i).getString("ingredient"));
                hm.put("quantity", ingredientsjson.getJSONObject(i).getString("quantity"));
                hm.put("unit", ingredientsjson.getJSONObject(i).getString("unit"));
                ingredients.add(hm);
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
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600, 800);
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
        tvName.setTextSize(35f);
        ret.addView(tvName);

        ImageView ivPic = new ImageView(context);
        int resID = context.getResources().getIdentifier(("drinkimg_"+name.toLowerCase().replaceAll("[^a-z0-9]","_"))+"_small" , "drawable", context.getPackageName());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(500, 500);
        ivPic.setLayoutParams(layoutParams2);
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
                intent.putExtra("name", name);
                context.startActivity(intent);
            }
        });

        Handler handler = new Handler();
        int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if (ivPic.getVisibility() != View.GONE)
                {
                    ivPic.setImageResource(resID);
                    Log.d("fuck", "nah");
                }
                else
                {
                    Log.d("fuck", "yeah");
                    ivPic.setImageResource(0);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);

        return ret;
    }

    public static ArrayList<Cocktail> getAllCocktails(Context context)
    {
        ArrayList<Cocktail> ret = new ArrayList<>();
        JSONObject allCocktails = JsonIO.load(context, R.raw.allcocktails);
        Iterator<String> keys = allCocktails.keys();
        while (keys.hasNext())
        {
            String k = keys.next();
            Log.d("fuck", k);
            ret.add(new Cocktail(context, k, allCocktails));
        }
        return ret;
    }
}
