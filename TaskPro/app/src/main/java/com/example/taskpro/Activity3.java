package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.example.taskpro.R.layout.activity_2;


public class Activity3 extends AppCompatActivity {
    private SQLiteDatabase db;
    private TableLayout tableLayout;
    private EditText editText1;
    private ImageButton buttonSearch;
    private ScrollView scrollView;
    private ImageButton buttonBack;
    private static String id = "id";
    private static String showAll ="showAll";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        // Initialize views and the database
        initializeViews();

        //Get the intent
        Intent intent = getIntent();
        if (intent.getBooleanExtra(showAll, true)) {
            // If the intent indicates to show all, create the table with all data
            createTable(true);
            // Hide the search button and text field
            buttonSearch.setVisibility(View.GONE);
            editText1.setVisibility(View.GONE);
        }
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the search button is clicked, create the table based on the search
                createTable(false);

            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the current activity
            }
        });


    }

    private void createTable(Boolean seeAll) {

        Cursor cursor = null;
        String editText = editText1.getText().toString();
        tableLayout.removeAllViews();
        if (!seeAll) {
            if (!editText.equals("")) {
                // Perform the search in the database based on the entered term
                String searchString = "%" + editText1.getText().toString() + "%";
                cursor = db.rawQuery("SELECT * FROM tasks WHERE Title LIKE ?", new String[]{searchString});
            } else {
                // If the search term is blank, show a message to the user
                Toast.makeText(Activity3.this, R.string.error1, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Show all data if the seeAll flag is true
            cursor = db.rawQuery("SELECT * FROM tasks", null);
        }

        if (cursor != null) {
            scrollView.setVisibility(View.VISIBLE);
            while(cursor.moveToNext()) {
                // Inflate the view for the table row
                View view = LayoutInflater.from(this).inflate(R.layout.table, null, false);
                TextView title = view.findViewById(R.id.rowTitle);
                TextView description = view.findViewById(R.id.rowDescription);
                TextView date = view.findViewById(R.id.rowDate);
                ImageButton editButton = view.findViewById(R.id.imageButton);
                // Assign the values of the current row to the corresponding views
                title.setText(cursor.getString(1));
                description.setText(cursor.getString(2));
                date.setText(cursor.getString(3));

                // Set the ID to use it in the intentEdit
                editButton.setId(Integer.parseInt(cursor.getString(0)));
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // When the button is clicked, open the edit activity with the param id
                        Intent intentEdit = new Intent(Activity3.this, Activity2.class);
                        intentEdit.putExtra(id , v.getId());
                        startActivityForResult(intentEdit, 1);
                    }
                });
                // Add the row view to the table
                tableLayout.addView(view);
            }
        } else {
            // If no results are found, hide the table and show a message to the user
            scrollView.setVisibility(View.GONE);
            Toast.makeText(Activity3.this, R.string.error2, Toast.LENGTH_SHORT).show();
        }

        // Close the cursor after using it
        if (cursor != null) {
            cursor.close();
        }

    }

    private void initializeViews() {
        // Initialize views
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        buttonBack =(ImageButton) findViewById(R.id.buttonVolver);
        buttonSearch = (ImageButton)  findViewById(R.id.buttonSearch1);
        editText1 = (EditText) findViewById(R.id.editText1);
        scrollView.setVisibility(View.GONE);

        // Initialize the database
        db = openOrCreateDatabase("TaskPro", MODE_PRIVATE, null);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){
                finish();
            }
        }
    }
}


