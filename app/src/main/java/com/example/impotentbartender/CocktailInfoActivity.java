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
    TextView tvJson;
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
        tvJson = findViewById(R.id.tvJson);
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

        String name = getIntent().getStringExtra("name");
        Cocktail cock = new Cocktail(context, name);

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
        Log.d("fuck", "2");

    }
}
