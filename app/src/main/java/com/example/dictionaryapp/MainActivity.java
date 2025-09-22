package com.example.dictionaryapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText searchInput;
    Button searchBtn;
    TextView resultText;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInput = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);
        resultText = findViewById(R.id.resultText);

        dbHelper = new DBHelper(this);

        searchBtn.setOnClickListener(v -> {
            String word = searchInput.getText().toString().trim();
            if (!word.isEmpty()) {
                String result = dbHelper.searchWord(word);
                resultText.setText(result);
            } else {
                resultText.setText("Please enter a word");
            }
        });
    }
}
