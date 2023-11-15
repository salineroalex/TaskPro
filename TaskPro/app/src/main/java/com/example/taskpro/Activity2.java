package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;

public class Activity2 extends AppCompatActivity {

    // Database instance
    private SQLiteDatabase db;

    // UI elements
    private EditText plainTextTitle, plainTextDesc, date;
    private Button buttonAccept1;
    private ImageButton buttonReturn;
    private static String database = "TaskPro";
    private static String id = "id";
    private static String titleDb= "title";
    private static String descDb = "desc";
    private static String taskDateDb = "taskDate";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        // Open or create the TaskPro database
        db = openOrCreateDatabase(database, MODE_PRIVATE, null);

        // Initialize UI elements
        plainTextTitle = findViewById(R.id.plainTextTitle1);
        plainTextDesc = findViewById(R.id.plainTextDesc1);
        date = findViewById(R.id.date1);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Retrieve item ID from the intent
        int itemId = getIntent().getIntExtra(id, -1);

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
                    Toast.makeText(Activity2.this, R.string.errorFields, Toast.LENGTH_SHORT).show();
                } else {
                    // Create an intent to launch Activity4 and pass data as extras
                    Intent intent = new Intent(Activity2.this, Activity4.class);
                    intent.putExtra(id, itemId);
                    intent.putExtra(titleDb, title);
                    intent.putExtra(descDb, desc);
                    intent.putExtra(taskDateDb, taskDate);
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
    private void showDatePickerDialog(){
        DatePickerFragment newFragment = new DatePickerFragment().newInstance(new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                date.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}