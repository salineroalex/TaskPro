package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class Activity2 extends AppCompatActivity {
    private EditText plainTextTitle, plainTextDesc, date;
    private Button buttonAccept1;
    private ImageButton buttonReturn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        plainTextTitle = findViewById(R.id.plainTextTitle1);
        plainTextDesc = findViewById(R.id.plainTextDesc1);
        date = findViewById(R.id.date1);

        buttonAccept1 = findViewById(R.id.buttonAccept1);
        buttonReturn = findViewById(R.id.buttonReturn);

        buttonAccept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = plainTextTitle.getText().toString();
                String desc = plainTextDesc.getText().toString();
                String taskDate = date.getText().toString();
                Intent intent = new Intent(Activity2.this, Activity4.class);
                intent.putExtra("title", title);
                intent.putExtra("desc", desc);
                intent.putExtra("taskDate", taskDate);
                startActivity(intent);
            }
        });

        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}