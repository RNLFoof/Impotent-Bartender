package com.example.impotentbartender;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static java.lang.Math.pow;

public class SuggestActivity extends AppCompatActivity {

    LinearLayout llSuggestions;
    Context context;
    Button btnGo;
    ArrayList<HashSet<String>> suggestions;
    String[] owned;
    ArrayList<Cocktail> allCocktails;
    TextView progress;
    EditText etAlc;
    EditText etNon;

    int prog_layer = 0;
    int prog_totallayers = 0;
    int prog_appends = 0;
    int prog_totalappends = 0;

    HashSet<String> s_alc = new HashSet<>();
    HashSet<String> s_non = new HashSet<>();
    HashSet<String> s_all = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        context = this;

        llSuggestions = findViewById(R.id.llSuggestions);
        btnGo = findViewById(R.id.btnGo);
        etAlc = findViewById(R.id.etAlc);
        etNon = findViewById(R.id.etNon);
        allCocktails = Cocktail.getAllCocktails(context);
        JSONObject allIngredients = JsonIO.load(context, R.raw.allingredients);

        Iterator<String> ks = allIngredients.keys();
        while (ks.hasNext())
        {
            String k = ks.next();
            try
            {
                if (allIngredients.getJSONObject(k).getString("strAlcohol").equals("Yes"))
                {
                    s_alc.add(k);
                    s_all.add(k);
                }
                else if (allIngredients.getJSONObject(k).getString("strAlcohol").equals("No"))
                {
                    s_non.add(k);
                    s_all.add(k);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        try {
            owned = PossibleDrinks.jsonListToStringArray(JsonIO.load(context, "owned").getJSONArray("list"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnGo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SuggestionsThread st = new SuggestionsThread();
                st.execute();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void populate()
    {
        llSuggestions.removeAllViews();
        Log.d("fuck1", suggestions.toString());
        ArrayList<Cocktail> alreadyPossible = PossibleDrinks.getPossibleDrinks(context, owned);

        int n = 0;
        for (HashSet<String> sug: sortByPotential(context, suggestions, owned))
        {

            // Ingredients
            TextView tvItem = new TextView(context);
            tvItem.setText(sug.toString());
            tvItem.setTextSize(30);
            llSuggestions.addView(tvItem);

            // New drinks
            tvItem = new TextView(context);
            StringBuilder s = new StringBuilder();
            FlowLayout flPreviews = new FlowLayout(context);
            for (Cocktail x: PossibleDrinks.getPossibleDrinks(context, concatenate(owned, sug.toArray(new String[sug.size()])), allCocktails))
            {
                if (!alreadyPossible.contains(x))
                {
                    //s.append(x.name);
                    //s.append(", ");
                    flPreviews.addView(x.getPreview());
                }
            }
            //tvItem.setText(s);
            //tvItem.setTextSize(20);
            //llSuggestions.addView(tvItem);
            llSuggestions.addView(flPreviews);

            if (n++ > 50)
            {
                break;
            }
        }
    }


    class SuggestionsThread extends AsyncTask
    {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Object doInBackground(Object[] objects)
        {
            runOnUiThread(new Thread(() -> newProgress()));

            int length_alc = Integer.parseInt(etAlc.getText().toString());
            int length_non = Integer.parseInt(etNon.getText().toString());
            ArrayList<HashSet<String>> sets = new ArrayList<>();

            // Startup
            sets.add(new HashSet<>());

            prog_layer = 1;
            prog_totallayers = length_alc + length_non;
            for (int n=0; n < length_alc + length_non; n++)
            // Make big
            {
                ArrayList<HashSet<String>> workingsets = new ArrayList<>();
                HashSet<String> workingsetstracker = new HashSet<>(); // Used to avoid dups

                prog_appends = 0;
                prog_totalappends = sets.size();
                for (HashSet<String> x: sets)
                {
                    for (String y: s_all)
                    {
                        Log.d("fuck2", "a");
                        HashSet<String> s = (HashSet<String>)x.clone();
                        s.add(y);
                        String[] temp = s.toArray(new String[s.size()]);
                        Arrays.sort(temp);
                        String arstring = "";
                        for (String z: temp)
                        {
                            arstring += z;
                        }

                        Log.d("fuck222", arstring);

                        if (s.size() == n+1 && !workingsetstracker.contains(arstring))
                        {
                            // Make sure it's in the limit for each category
                            HashSet<String> test = (HashSet<String>)s.clone();
                            int alc = 0;
                            int non = 0;
                            for (String z: test)
                            {
                                if (s_alc.contains(z))
                                {
                                    alc++;
                                }
                                else
                                {
                                    non++;
                                }
                            }

                            if (alc <= length_alc && non <= length_non)
                            {
                                workingsetstracker.add(arstring);
                                workingsets.add(s);
                                Log.d("fuck3", "d");
                            }

                        }
                        Log.d("fuck4", "b");
                    }

                    prog_appends++;
                    runOnUiThread(new Thread(() -> updateProgress()));
                }
                runOnUiThread(new Thread(() -> updateProgress(true)));
                sets=new ArrayList<>(sortByPotential(context, workingsets, owned).subList(0,30));
                Log.d("fuck6", "c");



                Log.d("fuck7", "2");

                prog_layer++;
            }
            Log.d("fuck8", "done");
            Log.d("fuck9", sets.toString());
            suggestions = sets;


            runOnUiThread(new Thread(() -> populate()));
            return suggestions;
        }
    }

    public <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<HashSet<String>> sortByPotential(Context context, ArrayList<HashSet<String>> in, String[] owned)
    {
        HashMap<String, Integer> d = new HashMap<>();
        for(HashSet<String> x: in)
        {
            d.put(x.toString(),
                    PossibleDrinks.getPossibleDrinks(context,
                            concatenate(owned, x.toArray(new String[x.size()])), allCocktails)
                            .size() );
        }

        Log.d("fuck69", d.toString());

        Collections.sort(in, (o1, o2) -> d.get(o2.toString()) - d.get(o1.toString()));

        return in;
    }

    void updateProgress()
    {
        updateProgress(false);
    }

    void updateProgress(boolean sorting)
    {
        if (sorting)
        {
            progress.setText(String.format("Layer %d/%d, sorting..."
                    , prog_layer
                    , prog_totallayers));
        }
        else
        {
            progress.setText(String.format("Layer %d/%d, %d/%d"
                    , prog_layer
                    , prog_totallayers
                    , prog_appends
                    , prog_totalappends));
        }
    }

    void newProgress()
    {
        llSuggestions.removeAllViews();
        progress = new TextView(context);
        progress.setTextSize(30);
        llSuggestions.addView(progress);
        updateProgress();
    }
}