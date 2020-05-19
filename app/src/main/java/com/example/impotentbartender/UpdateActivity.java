package com.example.impotentbartender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;

public class UpdateActivity extends AppCompatActivity {

    Context context;
    LinearLayout llList;
    TextView tvProgress;

    int done = 0;
    int needed = 0;
    int step = 1;
    int stepsNeeded = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        context = this;

        llList = (LinearLayout)findViewById(R.id.llOwnedList);
        tvProgress = (TextView)findViewById(R.id.tvProgress);
        Log.d("fuck1", "uh");

        for(int x = 97; x<123; x++)
        {
            requestAndSave("search.php?f=" + (char)x);
        }
    }

    void updateProgress()
    {
        if (needed == 0) {
            tvProgress.setText(String.format("Step %d/%d | %d/%d | 0%%", step, stepsNeeded, done, needed));
        }
        else {
            tvProgress.setText(String.format("Step %d/%d | %d/%d | %d%%", step, stepsNeeded, done, needed, done / needed * 100));
        }
    }

    void requestAndSave(final String input)
    {
        needed += 1;
        updateProgress();
        String base = "https://www.thecocktaildb.com/api/json/v1/1/";
        String url = base + input;
        final TextView tv = new TextView(context);
        tv.setText("Making request to "+input);
        tv.setPaintFlags(tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tv.setTextColor(Color.BLUE);
        llList.addView(tv);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tv.setText("Request to " + input + " succeeded!");
                        JsonIO.save(context, input, response.toString());
                        done += 1;
                        tv.setTextColor(Color.GREEN);
                        updateProgress();
                        wrapUp();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("Request to " + input + " failed. :'(");
                tv.setTextColor(Color.RED);
                done += 1;
                wrapUp();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    void wrapUp()
    {
        if (done<needed)
        {
            return;
        }

        done = 0;
        needed = 0;
        step += 1;

        Log.d("fuck1", "uh");
        tvProgress.setText(String.format("Step %d/%d | Processing...", step, stepsNeeded));
        if (step==2)
        {
            // Make one fat DB
            JSONObject allCocktails = new JSONObject();
            for(int x = 97; x<123; x++)
            {
                JSONArray working = null;
                try
                {
                    working = JsonIO.load(context,"search.php?f=" + (char)x)
                            .getJSONArray("drinks");
                    for (int y=0; y<working.length(); y++)
                    {
                        allCocktails.put(working.getJSONObject(y).getString("strDrink"), working.getJSONObject(y));
                        Log.d("fuck1", working.getJSONObject(y).getString("strDrink"));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            JsonIO.save(context, "allCocktails", allCocktails.toString());

            // Gather ingredients
            HashSet<String> ingredientSet = new HashSet<>();
            Iterator<String> keys = allCocktails.keys();
            while (keys.hasNext())
            {
                String k = keys.next();
                try
                {
                    for(int x = 1; x<15; x++)
                    {
                        if (((JSONObject)allCocktails.get(k)).get(String.format("strIngredient%d", x)) != null)
                        {
                            String ingr = allCocktails.getJSONObject(k).getString(String.format("strIngredient%d", x));
                            Log.d("fuck2", k);
                            ingredientSet.add(ingr);
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            JSONObject allIngredients = new JSONObject();
            Log.d("fuck1", ingredientSet.toString());
            for (String x: ingredientSet)
            {
                needed += 1;

                Log.d("fuck1", "hehe------------------");
                requestAndSave("search.php?i=" + x);
                try {
                    allIngredients.put(x, JsonIO.load(context,"search.php?i=" + x).getJSONArray("ingredients").getJSONObject(0));
                    Log.d("fuck1", x + " the best");
                    Toast.makeText(context, x + " the best", Toast.LENGTH_SHORT);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JsonIO.save(context, "allIngredients", allIngredients.toString());
        }
        Log.d("fuck1", "done");

        updateProgress();
    }
}
