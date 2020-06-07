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
import java.util.HashMap;
import java.util.Iterator;

public class PossibleDrinks {
    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Cocktail> getPossibleDrinks(Context context, String[] owned)
    {
        return getPossibleDrinks(context, owned, Cocktail.getAllCocktails(context));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Cocktail> getPossibleDrinks(Context context, String[] owned, ArrayList<Cocktail> allCocktails)
    {
        ArrayList<Cocktail> keys = new ArrayList<>();
        for (Cocktail cock: allCocktails)
        {
            boolean add = true;
            for (HashMap<String, String> ing: cock.ingredients)
            {
                if (JSONArrayPos(owned, ing.get("ingredient"))==-1)
                {
                    add = false;
                    break;
                }
            }
            if (add)
            {
                keys.add(cock);
            }
        }
        keys.sort((Cocktail c1, Cocktail c2) -> c1.name.compareTo(c2.name));
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
