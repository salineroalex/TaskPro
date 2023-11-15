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

public class Activity2 extends AppCompatActivity {

    // Database instance
    private SQLiteDatabase db;

    // UI elements
    private EditText plainTextTitle, plainTextDesc, date;
    private Button buttonAccept1;
    private ImageButton buttonReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        // Open or create the TaskPro database
        db = openOrCreateDatabase("TaskPro", MODE_PRIVATE, null);

        // Initialize UI elements
        plainTextTitle = findViewById(R.id.plainTextTitle1);
        plainTextDesc = findViewById(R.id.plainTextDesc1);
        date = findViewById(R.id.date1);

        // Retrieve item ID from the intent
        int itemId = getIntent().getIntExtra("id", -1);

        // If a valid item ID is provided, fetch data from the database and populate UI
        if (itemId != -1) {
            Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE id = ?", new String[]{String.valueOf(itemId)});
            if (cursor != null && cursor.moveToFirst()) {
                // Populate UI elements with data from the database
                plainTextTitle.setText(cursor.getString(1));
                plainTextDesc.setText(cursor.getString(2));
                date.setText(cursor.getString(3));
                cursor.close();
            }
        }

        // Initialize buttons
        buttonAccept1 = findViewById(R.id.buttonAccept);
        buttonReturn = findViewById(R.id.buttonReturn);

        // Set click listener for the "Accept" button
        buttonAccept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user input from the UI elements
                String title = plainTextTitle.getText().toString();
                String desc = plainTextDesc.getText().toString();
                String taskDate = date.getText().toString();

                // Check if any of the fields are empty
                if (title.isEmpty() || desc.isEmpty() || taskDate.isEmpty()) {
                    Toast.makeText(Activity2.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an intent to launch Activity4 and pass data as extras
                    Intent intent = new Intent(Activity2.this, Activity4.class);
                    intent.putExtra("id", itemId);
                    intent.putExtra("title", title);
                    intent.putExtra("desc", desc);
                    intent.putExtra("taskDate", taskDate);
                    // Start Activity4
                    startActivity(intent);
                }
            }
        });

        // Set click listener for the "Return" button
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the current activity and return to the previous one
                finish();
            }
        });
    }
}