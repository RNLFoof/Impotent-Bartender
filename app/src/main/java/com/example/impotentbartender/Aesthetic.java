package com.example.impotentbartender;

import org.json.JSONException;
import org.json.JSONObject;

public class Aesthetic {
    public static String ingredientString(JSONObject json)
    {
        String icon;
        try {
            if (json.getString("strAlcohol").equals("Yes"))
            {
                icon = "\uD83C\uDF78";
            }
            else if (json.getString("strAlcohol").equals("No"))
            {
                icon = "\uD83C\uDF4E";
            }
            else
            {
                icon = "❓";
            }
            return icon+" "+json.getString("strIngredient");
        }
        catch (JSONException e)
        {
            return "DIE";
        }
    }
}
