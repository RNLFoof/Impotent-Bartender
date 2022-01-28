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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PossibleDrinks {
    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Cocktail> getPossibleDrinks(Context context, String[] owned)
    {
        return getPossibleDrinks(context, owned, Cocktail.getAllCocktails(context));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Cocktail> getPossibleDrinks(Context context, String[] owned, ArrayList<Cocktail> allCocktails)
    {
        return getPossibleDrinks(context, new HashSet<>(Arrays.asList(owned)), allCocktails);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Cocktail> getPossibleDrinks(Context context, String[] owned, ArrayList<Cocktail> allCocktails, boolean usesimpllified)
    {
        return getPossibleDrinks(context, new HashSet<>(Arrays.asList(owned)), allCocktails, usesimpllified);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Cocktail> getPossibleDrinks(Context context, HashSet<String> owned, ArrayList<Cocktail> allCocktails)
    {
        return getPossibleDrinks(context, owned, allCocktails, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static ArrayList<Cocktail> getPossibleDrinks(Context context, HashSet<String> owned, ArrayList<Cocktail> allCocktails, boolean usesimpllified)
    {
        ArrayList<Cocktail> keys = new ArrayList<>();
        for (Cocktail cock: allCocktails)
        {
            HashSet<String> working;
            if (usesimpllified)
            {
                working = (HashSet<String>) cock.simplifiedingredientset.clone();
            }
            else
            {
                working = (HashSet<String>) cock.ingredientset.clone();
            }
            working.removeAll(owned);
            if (working.size() == 0)
            {
                keys.add(cock);
            }
        }
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
