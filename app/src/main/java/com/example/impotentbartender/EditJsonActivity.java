package com.example.impotentbartender;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class EditJsonActivity extends AppCompatActivity {

    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_json);

        String json = getIntent().getStringExtra("json");
        String title = getIntent().getStringExtra("title");

        etText = findViewById(R.id.etText);

        StringBuilder sb = new StringBuilder();
        String spaces = "";
        String spaceInterval = "    ";
        for (int x = 0; x<json.length(); x++)
        {
            char c = json.charAt(x);
            if (c == '[' || c == '{')
            {
                sb.append("\n" + spaces + c);
                spaces += spaceInterval;
                sb.append("\n" + spaces);
            }
            else if (c == ']' || c == '}')
            {
                spaces = spaces.replaceFirst(spaceInterval, "");
                sb.append("\n" + spaces + c);
                sb.append("\n" + spaces);
            }
            else if (c == '\n')
            {
                sb.append(spaces + c);
            }
            else if (c == ',')
            {
                sb.append(c + "\n" + spaces);
            }
            else
            {
                sb.append(c);
            }
        }

        etText.setText(sb.toString());
    }
}
