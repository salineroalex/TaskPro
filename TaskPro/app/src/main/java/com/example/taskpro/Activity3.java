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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.taskpro.R.layout.activity_2;

public class Activity3 extends AppCompatActivity {
    private SQLiteDatabase db;
    private TableLayout tableLayout;
    private EditText editText1;
    private ImageButton buttonSearch;
    private TextView rowtitle;
    private TextView rowDescription;
    private TextView rowDate;
    private TextView rowEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        rowtitle = (TextView) findViewById(R.id.rowTitle);
        rowDate = (TextView) findViewById(R.id.rowDate);
        rowEdit = (TextView) findViewById(R.id.rowTitle);
        // Inicializa tu base de datos (aquí asumimos que ya la has creada)
        db = openOrCreateDatabase("TaskPro", MODE_PRIVATE, null);

        // Obtén el TableLayout del archivo XML
        tableLayout = findViewById(R.id.tableLayout);
        editText1 = findViewById(R.id.EditText1);
        buttonSearch = findViewById(R.id.ButtonSearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               crearTabla();


            }
        });
    }
    private void crearTabla(){
        Intent intent = getIntent();

        if (intent.getBooleanExtra("fromButtonSearch", true)) {
            // Execute de Query with
            String  editText= editText1.getText().toString();
            if (!editText.equals("")) {
                tableLayout.setVisibility(View.VISIBLE);
                tableLayout.removeAllViews();
                String searchString = "%" + editText1.getText().toString() + "%";
                Cursor cursor = db.rawQuery("SELECT * FROM tasks WHERE Title LIKE ?", new String[]{searchString});

                if (cursor.moveToFirst()) {
                    // Añadir fila de cabecera
                    View headerView = LayoutInflater.from(this).inflate(R.layout.table, null, false);
                    TextView headerTitle = headerView.findViewById(R.id.rowTitle);
                    TextView headerDescription = headerView.findViewById(R.id.rowDescription);
                    TextView headerDate = headerView.findViewById(R.id.rowDate);
                    TextView headerEdit = headerView.findViewById(R.id.rowEdit);

                    // Configura los textos para la fila de cabecera según tus necesidades
                    headerTitle.setText(R.string.title);
                    headerDescription.setText(R.string.description);
                    headerDate.setText(R.string.date);
                    headerDate.setText(R.string.edit);

                    tableLayout.addView(headerView);
                    do {
                        View view = LayoutInflater.from(this).inflate(R.layout.table, null, false);
                     TextView title = view.findViewById(R.id.rowTitle);
                     TextView description = view.findViewById(R.id.rowDescription);
                     TextView date = view.findViewById(R.id.rowDate);
                     //Button editButton = view.findViewById(R.id.rowEdit);
                     title.setText(cursor.getString(1));
                     description.setText(cursor.getString(2));
                     date.setText(cursor.getString(3));
                     /*editButton.setBackgroundResource(R.drawable.pencil);
                     editButton.setText(cursor.getString(0));
                     int  columnWidth= headerEdit.getWidth();
                     ViewGroup.LayoutParams layoutParams = new TableRow.LayoutParams(columnWidth, columnWidth);
                     editButton.setLayoutParams(layoutParams);
                     editButton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {

                         }
                     }); */
                     tableLayout.addView(view);
                    } while (cursor.moveToNext());
                }else{
                    Toast.makeText(Activity3.this, "No information", Toast.LENGTH_SHORT).show();
                }

                // Cierra el cursor después de usarlo
                cursor.close();

            } else {
                Toast.makeText(Activity3.this, "The search is blank", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
