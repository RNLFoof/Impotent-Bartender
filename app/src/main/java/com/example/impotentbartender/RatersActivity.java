package com.example.impotentbartender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class RatersActivity extends AppCompatActivity {

    LinearLayout llRaters;
    EditText etRater;
    Button btnAddRater;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raters);

        llRaters = findViewById(R.id.llRaters);
        etRater = findViewById(R.id.etRater);
        btnAddRater = findViewById(R.id.btnAddRater);
        context = this;
        populateList();

        btnAddRater.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                llRaters.removeAllViews();
                try
                {
                    JSONObject raters = JsonIO.load(context, "raters.json");
                    if (!raters.has("list"))
                    {
                        raters.put("list", new JSONArray());
                    }
                    JSONArray l = raters.getJSONArray("list");
                    l.put(etRater.getText());
                    JsonIO.save(context, "raters.json", raters.toString());
                    populateList();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    void populateList()
    {
        llRaters.removeAllViews();
        try
        {
            JSONObject raters = JsonIO.load(context, "raters.json");
            if (!raters.has("list"))
            {
                raters.put("list", new JSONArray());
            }
            JSONArray l = raters.getJSONArray("list");
            for (int x = 0; x<l.length(); x++)
            {
                String k = l.get(x).toString();
                TextView tv = new TextView(context);
                tv.setText(k);
                tv.setTextSize(30);
                llRaters.addView(tv);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
