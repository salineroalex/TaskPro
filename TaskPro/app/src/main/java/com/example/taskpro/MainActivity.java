package com.example.taskpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Button buttonNew;
    private Button buttonSearch;
    private Button buttonShow;
    private Button buttonVideo;
    private Button buttonPhoto;
    private Locale locale;
    private Button buttonLanguage;
    private static String showAll = "showAll";
    private static String languageEs = "es";
    private static String languageEn = "en";
    private static String database = "TaskPro";
    private String fotoPath;
    static final int GRABAR_VIDEO = 3;
    static final int CAPTURA_IMAGEN = 4;

    private Configuration config = new Configuration();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNew = (Button) findViewById(R.id.buttonNew);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonShow = (Button) findViewById(R.id.buttonShow);
        buttonLanguage = (Button) findViewById(R.id.buttonLanguage);
        buttonVideo = (Button) findViewById(R.id.buttonVideo);
        buttonPhoto = (Button) findViewById(R.id.buttonPhoto);
        buttonVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, GRABAR_VIDEO);
                    }
                }

        });
        buttonPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(MainActivity.this, "com.example.android.fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, CAPTURA_IMAGEN);
                    }
                }
            }
        });
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
                        switch (which) {
                            case 0:
                                locale = new Locale(languageEn);
                                config.locale = locale;
                                break;
                            case 1:
                                locale = new Locale(languageEs);
                                config.locale = locale;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURA_IMAGEN && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            galleryAddPic(fotoPath);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, /* prefix */ ".jpg", /* suffix */ storageDir);
        // Save a file: path for use with ACTION_VIEW intents
        fotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic(String photoPath){
        Intent mediaScanIntent= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f= new File(photoPath);
        Uri contentUri= Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}