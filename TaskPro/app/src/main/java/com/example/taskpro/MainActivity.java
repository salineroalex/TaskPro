package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonNew = null;
    Button buttonSearch = null;
    Button buttonShow = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNew =(Button)findViewById(R.id.buttonNew);
        buttonSearch =(Button)findViewById(R.id.buttonSearch);
        buttonShow =(Button)findViewById(R.id.buttonShow);

        buttonSearch.setOnClickListener(this);
        buttonNew.setOnClickListener(this);
        buttonShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonSearch:
                Intent intentSearch = new Intent(this, Activity3.class);
                intentSearch.putExtra("showAll", false);
                startActivity(intentSearch);
                break;

            case R.id.buttonNew:
                Intent intent = new Intent(MainActivity.this, Activity4.class);
                intent.putExtra("newTask", true);
                startActivity(intent);
                break;

            case R.id.buttonShow:
                Intent intentAll = new Intent(this, Activity3.class);
                intentAll.putExtra("showAll", true);
                startActivity(intentAll);
                break;
        }
    }
}