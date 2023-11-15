package com.example.taskpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Button buttonNew;
    private Button buttonSearch;
    private Button buttonShow;
    private Locale locale;
    private Button buttonLanguage;
    private static String showAll = "showAll";
    private static String languageEs = "es";
    private static String languageEn = "en";
    private static String database = "TaskPro";
    private Configuration config = new Configuration();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNew = (Button) findViewById(R.id.buttonNew);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonShow = (Button) findViewById(R.id.buttonShow);
        buttonLanguage = (Button) findViewById(R.id.buttonLanguage);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(MainActivity.this, Activity3.class);
                intentSearch.putExtra(showAll, false);
                startActivity(intentSearch);
            }
        });
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNew = new Intent(MainActivity.this, Activity2.class);
                startActivity(intentNew);
            }
        });
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAll = new Intent(MainActivity.this, Activity3.class);
                intentAll.putExtra(showAll, true);
                startActivity(intentAll);
            }
        });
        buttonLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                String buttonLanguageText = buttonLanguage.getText().toString();
                b.setTitle(getResources().getString(R.string.language));
                String[] types = getResources().getStringArray(R.array.languages);
                b.setItems(types, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which){
                            case 0:
                                locale = new Locale(languageEn);
                                config.locale =locale;
                                break;
                            case 1:
                                locale = new Locale(languageEs);
                                config.locale =locale;
                                break;
                        }
                        getResources().updateConfiguration(config, null);
                        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(refresh);
                        finish();
                    }

                });
                b.show();
            }
        });
        db = openOrCreateDatabase(database, Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS tasks(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title VARCHAR," +
                "Description VARCHAR," +
                "CreationDate DATE" +
                ");");

    }


}