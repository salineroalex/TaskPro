package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;

public class Activity4 extends AppCompatActivity implements OnClickListener {

    Intent intent_new = null;

    TextView textViewTitle = null;
    TextView textViewDescription = null;
    EditText date = null;
    ImageButton buttonCalendar = null;
    Button buttonCancelar = null;
    Button buttonAceptar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        textViewTitle = (TextView) findViewById(R.id.textViewTitleCaja);
        textViewTitle.setEnabled(false);
        textViewDescription = (TextView) findViewById(R.id.textViewDescriptionTitleCaja);
        textViewDescription.setEnabled(false);
        date = (EditText) findViewById(R.id.date2);
        date.setEnabled(false);
        buttonCalendar = (ImageButton) findViewById(R.id.buttonCalendar);
        buttonCalendar.setOnClickListener(this);
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
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha;

                try {
                    fecha = dateFormat.parse(fechaTexto);
                    long startMillis = fecha.getTime();
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
                } catch (ParseException e) {
                    Toast.makeText(Activity4.this, "Couldn't parse date", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.buttonCancel1:
                finish();
                break;

            case R.id.buttonAccept1:
                // Code for verification when type: edit.
                buttonCalendar.setEnabled(true);
                buttonCalendar.setVisibility(View.VISIBLE);
                buttonCancelar.setEnabled(false);
                buttonCancelar.setVisibility(View.GONE);
                buttonAceptar.setEnabled(false);
                //finish();
                break;
            default:

        }
    }
}