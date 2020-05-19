package com.example.impotentbartender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddRatingActivity extends AppCompatActivity {
    LinearLayout llListOfRatings;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);

        context = this;
        llListOfRatings = findViewById(R.id.llListOfRatings);

        populate();
    }

    void populate()
    {
        llListOfRatings.removeAllViews();

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
                llListOfRatings.addView(tv);

                RatingBar rb = new RatingBar(context);
                rb.setNumStars(5);
                rb.setStepSize(0.5f);
                rb.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                llListOfRatings.addView(rb);

                TextView tvDesc = new TextView(context);
                tv.setTextSize(30);
                rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
                {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
                    {

                    }
                });
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    String ratingName(float rating)
    {
        if (rating == 0)
            return "DEAR GOD WHY";
        else if (rating == 0.5)
            return "Never want this again";
        else if (rating == 1)
            return "Bad";
        else if (rating == 1.5)
            return "Tolerable";
        else if (rating == 2)
            return "Fine";
        else if (rating == 2.5)
            return "Decent";
        else if (rating == 3)
            return "Good";
        else if (rating == 3.5)
            return "Great";
        else if (rating == 4)
            return "Love it";
        else if (rating == 4.5)
            return "One of my favorites";
        return "INVALID VALUE LOL fuck me";
    }
}
