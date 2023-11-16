package com.example.taskpro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.service.chooser.ChooserAction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Activity4 extends AppCompatActivity {

    // Initialise variables
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    TextView textViewTitle = null;
    TextView textViewDescription = null;
    EditText date = null;
    ImageButton buttonCalendar = null;
    Button buttonCancelar = null;
    Button buttonAceptar = null;

    private SQLiteDatabase db;
    private static String database = "TaskPro";
    private static String id = "id";
    private static String titleDb = "title";
    private static String descDb = "desc";
    private static String taskDateDb = "taskDate";

    int id_received;
    String titleDb_received, descDb_received, taskDateDb_received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        // Get extras from previous intent
        Bundle extras = getIntent().getExtras();
        id_received = extras.getInt(id);
        titleDb_received = extras.getString(titleDb);
        descDb_received = extras.getString(descDb);
        taskDateDb_received = extras.getString(taskDateDb);

        // Set the views
        textViewTitle = (TextView) findViewById(R.id.textViewTitleCaja);
        textViewTitle.setText(titleDb_received);
        textViewDescription = (TextView) findViewById(R.id.textViewDescriptionTitleCaja);
        textViewDescription.setText(descDb_received);
        date = (EditText) findViewById(R.id.date2);
        date.setText(taskDateDb_received);
        buttonCalendar = (ImageButton) findViewById(R.id.buttonCalendar);
        buttonCalendar.setEnabled(false);
        buttonCalendar.setVisibility(View.GONE);
        buttonCancelar = (Button) findViewById(R.id.buttonCancel1);
        buttonAceptar = (Button) findViewById(R.id.buttonAccept1);

        buttonCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date fecha;

                try {
                    // Parse date to calendar app format
                    fecha = dateFormat.parse(taskDateDb_received);
                    long startMillis = fecha.toInstant().toEpochMilli();
                    long endMillis = startMillis + (24 * 60 * 60 * 1000);

                    // Set new intent with extras to send to calendar app
                    Intent intent_new = new Intent(Intent.ACTION_EDIT);
                    intent_new.setType("vnd.android.cursor.item/event");
                    intent_new.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis);
                    intent_new.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis);
                    intent_new.putExtra(CalendarContract.Events.ALL_DAY, true);
                    intent_new.putExtra(CalendarContract.Events.TITLE, textViewTitle.getText().toString());
                    intent_new.putExtra(CalendarContract.Events.DESCRIPTION, textViewDescription.getText().toString());

                    // Initialise chooser, make sure there is an app for the event
                    Intent chooser = intent_new.createChooser(intent_new, getString(R.string.a4_chooseApp));
                    if((chooser.resolveActivity(getPackageManager()) != null)){
                        startActivity(chooser);
                    } else {
                        Toast.makeText(Activity4.this, R.string.a4_no_app_error, Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    Toast.makeText(Activity4.this, R.string.a4_parse_exception, Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Dialog to exit or edit the task
                AlertDialog.Builder b = new AlertDialog.Builder(Activity4.this);
                b.setTitle(getResources().getString(R.string.a4_confirmation));
                String[] types = getResources().getStringArray(R.array.a4_edit_exit);
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // The options for the dialog
                        switch (which) {
                            case 0:
                                setResult(0);
                                finish();
                                break;
                            case 1:
                                setResult(1);
                                finish();
                                break;
                        }
                    }
                });
                // Show the dialog
                b.show();
            }
        });
        buttonAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int max_id = 0, id = 0;

                // Adjust visibility and accessibility of correspondent buttons.
                buttonCalendar.setEnabled(true);
                buttonCalendar.setVisibility(View.VISIBLE);
                buttonCancelar.setEnabled(false);
                buttonCancelar.setVisibility(View.GONE);
                buttonAceptar.setEnabled(false);
                db = openOrCreateDatabase(database, Context.MODE_PRIVATE, null);

                if (id_received == -1) {
                    // Code for insert when type: create.
                    Cursor c = db.rawQuery("SELECT max(id) FROM tasks", null);
                    c.moveToNext();
                    max_id = c.getInt(0);
                    c.close();
                    id = max_id + 1;
                    db.execSQL("INSERT INTO tasks (id, Title, Description, CreationDate) VALUES (" +
                            id + ", '" + titleDb_received + "', '" + descDb_received + "', '" + taskDateDb_received +
                            "');");
                } else {
                    // Code for modification when type: edit.
                    ContentValues values = new ContentValues();
                    values.put("Title", titleDb_received);
                    values.put("Description", descDb_received);
                    values.put("CreationDate", taskDateDb_received);
                    db.update("tasks", values, "id = " + id_received, null);

                }
            }
        });

    }
}