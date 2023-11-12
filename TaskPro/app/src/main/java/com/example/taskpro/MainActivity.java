package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SQLiteDatabase db;
    private Button buttonNew;
    private Button buttonSearch;
    private Button buttonShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNew =(Button)findViewById(R.id.ButtonNew);
        buttonSearch =(Button)findViewById(R.id.ButtonSearch);
        buttonShow =(Button)findViewById(R.id.ButtonShow);

        buttonSearch.setOnClickListener(this);
        buttonNew.setOnClickListener(this);
        buttonShow.setOnClickListener(this);
        db=openOrCreateDatabase("TaskPro", Context.MODE_PRIVATE,null);

        db.execSQL("CREATE TABLE IF NOT EXISTS tasks(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title VARCHAR," +
                "Description VARCHAR," +
                "CreationDate DATE" +
                ");");


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ButtonSearch:
                Intent intentSearch = new Intent(this, Activity3.class);
                intentSearch.putExtra("showAll", false);
                startActivity(intentSearch);
                break;

            case R.id.ButtonNew:
                break;

            case R.id.ButtonShow:
                Intent intentAll = new Intent(this, Activity3.class);
                intentAll.putExtra("showAll", true);
                startActivity(intentAll);
                break;
        }
    }
}