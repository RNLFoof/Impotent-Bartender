package com.example.impotentbartender;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
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
    HashSet<String> owned;
    ArrayList<Cocktail> allCocktails;
    TextView progress;
    String previousProgress;
    String previousProgressWaiting;
    EditText etAlc;
    EditText etNon;
    EditText etFilter;

    int prog_layer = 0;
    int prog_totallayers = 0;
    int prog_appends = 0;
    int prog_totalappends = 0;

    HashSet<String> s_alc = new HashSet<>();
    HashSet<String> s_non = new HashSet<>();
    HashSet<String> s_all = new HashSet<>();

    HashMap<Integer // Size
            , HashMap<HashSet<String> // New ingredients
            , HashSet<Integer> // Possible cocktail indexes
            >> potential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        context = this;

        llSuggestions = findViewById(R.id.llSuggestions);
        btnGo = findViewById(R.id.btnGo);
        etAlc = findViewById(R.id.etAlc);
        etNon = findViewById(R.id.etNon);
        etFilter = findViewById(R.id.etFilter);
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
            owned = new HashSet<>(Arrays.asList(PossibleDrinks.jsonListToStringArray(JsonIO.load(context, "owned").getJSONArray("list"))));
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
    void populate(ArrayList<Pair<HashSet<String>, HashSet<Integer>>> sorted)
    {
        llSuggestions.removeAllViews();


        int length_alc = Integer.parseInt(etAlc.getText().toString());
        int length_non = Integer.parseInt(etNon.getText().toString());
        int length_all = length_alc + length_non;

        int n = 0;
        for (Pair<HashSet<String>, HashSet<Integer>> x: sorted)
        {
            HashSet<String> suggestions = x.first;
            HashSet<Integer> newCocktails = x.second;
            Log.d("uhhhhhhhhhhhhhh", newCocktails.toString());
            // Ingredients
            TextView tvItem = new TextView(context);
            tvItem.setText(suggestions.toString() + String.format(" (%d new)", newCocktails.size()));
            tvItem.setTextSize(30);
            llSuggestions.addView(tvItem);

            // New drinks
            StringBuilder s = new StringBuilder();
            FlowLayout flPreviews = new FlowLayout(context);
            int nn = 0;
            for (Integer y: newCocktails)
            {
                Cocktail working = allCocktails.get(y);
                flPreviews.addView(working.getPreview());
                if (nn++ > 5)
                {
                    break;
                }
            }
            //tvItem.setText(s);
            //tvItem.setTextSize(20);
            //llSuggestions.addView(tvItem);
            llSuggestions.addView(flPreviews);

            if (n++ > 5)
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
            runOnUiThread(new Thread(SuggestActivity.this::newProgress));

            int length_alc = Integer.parseInt(etAlc.getText().toString());
            int length_non = Integer.parseInt(etNon.getText().toString());
            int length_all = length_alc + length_non;
            allCocktails = Cocktail.getAllCocktails(context);
            previousProgress = "";
            previousProgressWaiting = "";

            int originalsize = allCocktails.size();
            for (int n=allCocktails.size()-1; n >= 0; n--)
            {
                setProgressString(String.format("Trimming cocktail %d/%d, removed %d...", originalsize-n, originalsize, originalsize-allCocktails.size()));
                Cocktail working = allCocktails.get(n);
                if (!working.matchesSearch(etFilter.getText().toString()))
                {
                    allCocktails.remove(n);
                    continue;
                }
                for (int nn=working.ingredients.size()-1; nn >= 0; nn--)
                {
                    if (owned.contains(working.ingredients.get(nn).get("ingredient")))
                    {
                        working.ingredientset.remove(working.ingredients.get(nn).get("ingredient"));
                    }
                }
                if (working.ingredientset.size() == 0 || working.ingredientset.size() > length_all)
                {
                    allCocktails.remove(n);
                    continue;
                }
                int alc = 0;
                int non = 0;
                for (String z: working.ingredientset)
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
                if (alc > length_alc || non > length_non)
                {
                    allCocktails.remove(n);
                }
            }

            if (allCocktails.size() == 0)
            {
                setProgressString("NOTHING!!!");
                return suggestions;
            }

            setProgressString(String.format("Trimming cocktail %d/%d, removed %d...", originalsize, originalsize, originalsize-allCocktails.size()));
            setNewPreviousProgress();



            potential = new HashMap<>();

            for (int n=0; n < allCocktails.size(); n++)
            {
                setProgressString(String.format("Cataloging cocktail %d/%d...", n+1, allCocktails.size()));
                Cocktail working = allCocktails.get(n);

                int size = working.ingredientset.size();


                potential.putIfAbsent(size, new HashMap<>());
                potential.get(size).putIfAbsent(working.ingredientset, new HashSet<>());
                potential.get(size).get(working.ingredientset).add(n);
            }
            setNewPreviousProgress();



            ArrayList // List of ing cock tuples
                    <Pair<HashSet<String>, HashSet<Integer> // Tuple of ng, cock
                            >> toSort = new ArrayList<>();

            HashMap<Integer, ArrayList // List of ing cock tuples
                    <Pair<HashSet<String>, HashSet<Integer> // Tuple of ng, cock
                            >>> toSortMap = new HashMap<>();
            for (int n=0; n < length_all; n++)
            {
                setProgressString(String.format("Sorting %d/%d...", n+1, length_all));

                toSort = new ArrayList<>();

                potential.putIfAbsent(n+1, new HashMap<>());
                for (HashSet<String> k: potential.get(n+1).keySet())
                {
                    HashSet<Integer> v = potential.get(n+1).get(k);
                    toSort.add(new Pair<HashSet<String>, HashSet<Integer>>(k, v));
                };
                Collections.sort(toSort, new Comparator<Pair<HashSet<String>, HashSet<Integer>>>() {
                    public int compare(Pair<HashSet<String>, HashSet<Integer>> obj1, Pair<HashSet<String>, HashSet<Integer>> obj2) {
                        return Integer.compare(obj2.second.size(), obj1.second.size());
                    }
                });
//                for (Pair<HashSet<String>, HashSet<Integer>> x: toSort)
//                {
//                    Log.d("8888888888888", x.toString());
//                }
                if (toSort.size()> 50) {
                    toSort.subList(50, toSort.size()).clear();
                };
                toSortMap.put(n+1, toSort);

                int totalcount = 0;
                if (length_all != n+1)
                {
                    setNewPreviousProgress();
                    for (int comparingtolayer=1; comparingtolayer<=n+1; comparingtolayer++)
                    {
                        if (comparingtolayer != 1 && comparingtolayer != 2 && comparingtolayer != n+1)
                        {
                            continue;
                        }
                        for (int nn=0; nn < toSort.size() && nn < 50; nn++)
                        {
                            totalcount++;
                            setProgressString(String.format("Trickling up %d/%d...", totalcount, Math.min(150, (n+1)*50)));
                            for (int nnn=nn; nnn < toSortMap.get(comparingtolayer).size() && nnn < 50; nnn++)
                            {
                                Log.d("7468645845", String.format("Trickling up %d/%d...", nn, nnn));
                                Pair<HashSet<String>, HashSet<Integer>> working = new Pair<>(new HashSet<>(), new HashSet<>());
                                working.first.addAll(toSort.get(nn).first);
                                working.first.addAll(toSortMap.get(comparingtolayer).get(nnn).first);
                                working.second.addAll(toSort.get(nn).second);
                                working.second.addAll(toSortMap.get(comparingtolayer).get(nnn).second);

                                if (working.first.size() > length_all)
                                {
                                    continue;
                                }
                                int alc = 0;
                                int non = 0;
                                for (String z: working.first)
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
                                if (alc > length_alc || non > length_non)
                                {
                                    continue;
                                }

                                potential.putIfAbsent(working.first.size(), new HashMap<>());
                                if (potential.get(working.first.size()).get(working.first) != null)
                                {
                                    Log.d("74686458455", "WOW");
                                }
                                potential.get(working.first.size()).putIfAbsent(working.first, new HashSet<>());
                                potential.get(working.first.size()).get(working.first).addAll(working.second);
                                // Log.d("74686458455", String.valueOf(potential.get(working.first.size()).get(working.first)));
                            }
                        }
                    }
                }
                setNewPreviousProgress();
            }
            ArrayList<Pair<HashSet<String>, HashSet<Integer>>> finalToSort = toSort;
            runOnUiThread(new Thread(() -> populate(finalToSort)));

            // Startup
//            sets.add(new HashSet<>());
//
//            prog_layer = 1;
//            prog_totallayers = length_alc + length_non;
//            for (int n=0; n < length_alc + length_non; n++)
//            // Make big
//            {
//                ArrayList<HashSet<String>> workingsets = new ArrayList<>();
//                HashSet<String> workingsetstracker = new HashSet<>(); // Used to avoid dups
//
//                prog_appends = 0;
//                prog_totalappends = sets.size();
//                for (HashSet<String> x: sets)
//                {
//                    for (String y: s_all)
//                    {
//                        Log.d("fuck2", "a");
//                        HashSet<String> s = (HashSet<String>)x.clone();
//                        s.add(y);
//                        String[] temp = s.toArray(new String[s.size()]);
//                        Arrays.sort(temp);
//                        String arstring = "";
//                        for (String z: temp)
//                        {
//                            arstring += z;
//                        }
//
//                        Log.d("fuck222", arstring);
//
//                        if (s.size() == n+1 && !workingsetstracker.contains(arstring))
//                        {
//                            // Make sure it's in the limit for each category
//                            HashSet<String> test = (HashSet<String>)s.clone();
//                            int alc = 0;
//                            int non = 0;
//                            for (String z: test)
//                            {
//                                if (s_alc.contains(z))
//                                {
//                                    alc++;
//                                }
//                                else
//                                {
//                                    non++;
//                                }
//                            }
//
//                            if (alc <= length_alc && non <= length_non)
//                            {
//                                workingsetstracker.add(arstring);
//                                workingsets.add(s);
//                                Log.d("fuck3", "d");
//                            }
//
//                        }
//                        Log.d("fuck4", "b");
//                    }
//
//                    prog_appends++;
//                    runOnUiThread(new Thread(() -> updateProgress()));
//                }
//                runOnUiThread(new Thread(() -> updateProgress(true)));
//                Log.d("fuck6dfdfdfdfdfdf", "c");
////                sets=new ArrayList<>(sortByPotential(context, workingsets, owned).subList(0,30));
//                Log.d("fuck6", "c");
//
//
//
//                Log.d("fuck7", "2");
//
//                prog_layer++;
//            }
//            Log.d("fuck8", "done");
//            Log.d("fuck9", sets.toString());
//            suggestions = sets;


//            runOnUiThread(new Thread(() -> populate()));
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
        HashSet<String> ownedset = new HashSet<>(Arrays.asList(owned));
        for(HashSet<String> x: in)
        {
            HashSet<String> working = (HashSet<String>) x.clone();
            working.addAll(ownedset);

            d.put(x.toString(),
                    PossibleDrinks.getPossibleDrinks(context, working, allCocktails)
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

    void setProgressString(String s)
    {
        previousProgressWaiting = previousProgress + s;
        runOnUiThread(new Thread(() -> progress.setText(previousProgress + s)));
    }

    void setNewPreviousProgress()
    {
        previousProgress = previousProgressWaiting + "\n\n";
    }

    void newProgress()
    {
        previousProgress = "";
        llSuggestions.removeAllViews();
        progress = new TextView(context);
        progress.setTextSize(30);
        llSuggestions.addView(progress);
        // updateProgress();
    }
}