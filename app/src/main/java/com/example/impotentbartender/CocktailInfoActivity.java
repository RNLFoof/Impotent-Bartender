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

        Log.d("fuck", "1");
        try
        {
            String jsonString = getIntent().getStringExtra("json");
            JSONObject json = new JSONObject(jsonString);

            tvName.setText(json.getString("strDrink"));
            tvDesc.setText("lol");
            String s1 = "";
            String s2 = "";
            for (int i = 1; i <= 15; i++)
            {
                if (json.getString("strIngredient"+i) != null)
                {
                    try
                    {
                        s1 += json.getString("strIngredient"+i) + "\n";
                        s2 += json.getString("strMeasure"+i).toString() + "\n";
                    }
                    catch (JSONException e)
                    {
                        Log.d("fuck", e.getMessage());
                    }
                }
                tvIng.setText(s1.trim());
                tvQuantity.setText(s2.trim());
            }
            tvInstructions.setText(json.getString("strInstructions"));
            tvJson.setText(jsonString);
        }
        catch (JSONException e)
        {
            Log.d("fuck", e.getMessage());
        }
        Log.d("fuck", "2");

    }
}
