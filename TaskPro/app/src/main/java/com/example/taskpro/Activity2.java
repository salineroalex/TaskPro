package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Activity2 extends AppCompatActivity {
    private SQLiteDatabase db;
    private EditText plainTextTitle, plainTextDesc, date;
    private Button buttonAccept1;
    private ImageButton buttonReturn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        db = openOrCreateDatabase("TaskPro", MODE_PRIVATE, null);

        plainTextTitle = findViewById(R.id.plainTextTitle1);
        plainTextDesc = findViewById(R.id.plainTextDesc1);
        date = findViewById(R.id.date1);

        int itemId = getIntent().getIntExtra("id", -1);

        if (itemId != -1) {
            Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE _id = ?", new String[]{String.valueOf(itemId)});
            if (cursor != null && cursor.moveToFirst()) {
                plainTextTitle.setText(cursor.getString(1));
                plainTextDesc.setText(cursor.getString(2));
                date.setText(cursor.getString(3));
                cursor.close();
            }
        }

        buttonAccept1 = findViewById(R.id.buttonAccept1);
        buttonReturn = findViewById(R.id.buttonReturn);

        buttonAccept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = plainTextTitle.getText().toString();
                String desc = plainTextDesc.getText().toString();
                String taskDate = date.getText().toString();
                if (title.isEmpty() || desc.isEmpty() || taskDate.isEmpty()) {
                    Toast.makeText(Activity2.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Activity2.this, Activity4.class);
                    intent.putExtra("id", itemId);
                    intent.putExtra("title", title);
                    intent.putExtra("desc", desc);
                    intent.putExtra("taskDate", taskDate);
                    startActivity(intent);
                }
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