package com.example.impotentbartender;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.impotentbartender.JsonIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class PossibleDrinks {
    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<String> getPossibleDrinks(Context context, String[] owned)
    {
        JSONObject allCocktails = JsonIO.load(context, "allCocktails");
        return getPossibleDrinks(context, owned, allCocktails);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<String> getPossibleDrinks(Context context, String[] owned, JSONObject allCocktails)
    {
        ArrayList<String> keys = new ArrayList<>();
        Iterator<String> ks = allCocktails.keys();
        while (ks.hasNext())
        {
            String k = ks.next();
            boolean add = true;
            for (int i=1; i<=15; i++)
            {
                try
                {
                    String ing = allCocktails.getJSONObject(k).getString("strIngredient" + i);
                    if (ing.toString() != "null" && JSONArrayPos(owned, ing)==-1)
                    {
                        add = false;
                        break;
                    }
                }
                catch (JSONException e)
                {
                    Log.d("fuck", "if you're reading this, something is very very wrong");
                }
            }
            if (add)
            {
                keys.add(k);
            }
        }
        keys.sort(String::compareToIgnoreCase);
        return keys;
    }



    static int JSONArrayPos(String[] l, Object looking)
    {
            for (int i = 0; i < l.length; i++)
                if (l[i].equals(looking))
                    return i;
        return -1;
    }

    static String[] jsonListToStringArray(JSONArray jsar)
    {
        String[] stringsArray = new String[jsar.length()];
        for (int i = 0; i < jsar.length(); i++)
        {
            try {
                stringsArray[i] = jsar.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stringsArray;
    }
}
