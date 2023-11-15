package com.example.taskpro;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
public class Activity4 extends AppCompatActivity implements OnClickListener {

    Intent intent_new = null;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    String id_received, titleDb_received, descDb_received, taskDateDb_received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        Bundle extras = getIntent().getExtras();
        id_received = extras.getString(id);
        titleDb_received = extras.getString(titleDb);
        descDb_received = extras.getString(descDb);
        taskDateDb_received = extras.getString(taskDateDb);


        textViewTitle = (TextView) findViewById(R.id.textViewTitleCaja);
        textViewTitle.setText(titleDb_received);
        textViewDescription = (TextView) findViewById(R.id.textViewDescriptionTitleCaja);
        textViewDescription.setText(descDb_received);
        date = (EditText) findViewById(R.id.date2);
        date.setText(taskDateDb_received);
        buttonCalendar = (ImageButton) findViewById(R.id.buttonCalendar);
        buttonCalendar.setOnClickListener(this);
        buttonCalendar.setEnabled(false);
        buttonCalendar.setVisibility(View.GONE);
        buttonCancelar = (Button) findViewById(R.id.buttonCancel1);
        buttonCancelar.setOnClickListener(this);
        buttonAceptar = (Button) findViewById(R.id.buttonAccept1);
        buttonAceptar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCalendar:
                String fechaTexto = date.getText().toString();

                LocalDate fecha;

                fecha = LocalDate.parse(fechaTexto, dateFormat);
                long startMillis = fecha.atStartOfDay().toInstant(ZoneOffset.of("Europe/Madrid")).toEpochMilli();
                long endMillis = startMillis + (24 * 60 * 60 * 1000);

                Intent intent_new = new Intent(Intent.ACTION_EDIT);
                intent_new.setType("vnd.android.cursor.item/event");
                intent_new.putExtra(CalendarContract.Events.DTSTART, startMillis);
                intent_new.putExtra(CalendarContract.Events.DTEND, endMillis);
                intent_new.putExtra(CalendarContract.Events.ALL_DAY, true);
                intent_new.putExtra(CalendarContract.Events.TITLE, textViewTitle.getText().toString());
                intent_new.putExtra(CalendarContract.Events.DESCRIPTION, textViewDescription.getText().toString());

                Intent chooser = intent_new.createChooser(intent_new, getString(R.string.a4_chooseApp));
                startActivity(chooser);
                break;

            case R.id.buttonCancel1:
                finish();
                break;

            case R.id.buttonAccept1:
                int max_id = 0, id = 0;
                LocalDate date_parsed = null;
                Cursor response;

                // Code for verification when type: edit.
                buttonCalendar.setEnabled(true);
                buttonCalendar.setVisibility(View.VISIBLE);
                buttonCancelar.setEnabled(false);
                buttonCancelar.setVisibility(View.GONE);
                buttonAceptar.setEnabled(false);
                db = openOrCreateDatabase(database, Context.MODE_PRIVATE, null);

                Cursor c = db.rawQuery("SELECT max(id) FROM tasks", null);
                c.moveToNext();
                if (id_received == null) {
                    max_id = c.getInt(0);
                    id = max_id + 1;
                    date_parsed = LocalDate.parse(taskDateDb_received, dateFormat);
                    db.execSQL("INSERT INTO tasks (id, Title, Description, CreationDate) VALUES (" +
                            id + ", '" + titleDb_received + "', '" + descDb_received + "', " + date_parsed +
                            ");");
                } else {
                    id = Integer.parseInt(id_received);


                }


                /*db.execSQL("INSERT INTO tasks (id, Title, Description, CreationDate) VALUES (" +
                        id + "," + titleDb_received + descDb_received + date +
                        ");");*/

                break;
            default:

        }
    }
}