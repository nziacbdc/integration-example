package com.movmintdigitalfiat.icepic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ThirdActivity extends AppCompatActivity {

    TextView text;
    Button completeBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        JSONObject jsonObject = null;
        try {
            assert data != null;
            jsonObject = new JSONObject(data);

            text = findViewById(R.id.finalResponse);
            text.setText(jsonObject.toString(4));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        completeBt = findViewById(R.id.completedBt);

        completeBt.setOnClickListener(v -> {
            Intent homeIntent = new Intent(ThirdActivity.this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
            Toast.makeText(ThirdActivity.this, "Successfully completed!", Toast.LENGTH_SHORT).show();
        });
    }
}