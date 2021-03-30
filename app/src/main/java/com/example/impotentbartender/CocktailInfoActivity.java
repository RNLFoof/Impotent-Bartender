package com.example.impotentbartender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CocktailInfoActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvDesc;
    TextView tvIng;
    TextView tvQuantity;
    TextView tvInstructions;
    TextView tvSource;
    Button btnAddRatings;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail_info);

        tvName = findViewById(R.id.tvName);
        tvDesc = findViewById(R.id.tvDesc);
        tvIng = findViewById(R.id.tvIng);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvSource = findViewById(R.id.tvSource);
        btnAddRatings = findViewById(R.id.btnAddRatings);
        context = this;

        btnAddRatings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, AddRatingActivity.class);
                startActivity(intent);
            }
        });

        int index = getIntent().getIntExtra("index", 0);
        Cocktail cock = new Cocktail(context, index);

        tvName.setText(cock.name);
        tvDesc.setText("lol");
        String s1 = "";
        String s2 = "";
        for (HashMap<String, String> ing: cock.ingredients)
        {
            s1 += ing.get("ingredient")+"\n";
            s2 += ing.get("quantity")+" "+ing.get("unit")+"\n";
        }
        tvIng.setText(s1.trim());
        tvQuantity.setText(s2.trim());
        tvInstructions.setText(cock.instructions);

        String source = "From "+cock.source+".\n";
        switch(cock.source) {
            case "TheCocktailDB":
                source += "Anybody can add to the database and it shows. Quality varies wildly, but there's some good stuff in there. Also, it uses powdered sugar instead of simple syrup like 99% of the time for some reason, so swap those in your head.";
                break;
            case "Reddit":
                source += "These are entered manually so they should be fine";
                break;
            case "Van Gogh":
                source += "Taken from an SQLite file with 10,000+ cocktails on it. Has some really tasty stuff, including stuff you wouldn't think would be good (orange vodka and toasted marshmallow syrup), but like, it was made to sell vodka, and there's ten freaking thousand recipes, so be at least a little wary. Also, has a few that are glitched.";
                break;
            default:
                source += "I fucked up, there should be a more specific comment here.";
        }
        tvSource.setText(source);

        Log.d("fuck", "2");

    }
}
