package com.example.taskpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity5 extends AppCompatActivity {
    private String fotoPath;
    static final int CAPTURA_IMAGEN_TAMANO_REAL = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Manejar el error al crear el archivo
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURA_IMAGEN_TAMANO_REAL);

                // Verificar que la foto se haya guardado antes de añadirla a la galería
                if (photoFile.exists()) {
                    // Añadir la imagen a la galería
                    galleryAddPic(photoFile);
                } else {
                    // Manejar el caso en el que la foto no se haya guardado correctamente
                    Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void galleryAddPic(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // File storageDir = Environment.getExternalStoragePublicDirectory(
        // Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        fotoPath = "file:" + image.getAbsolutePath();
        // Show Toast with the file path
        Toast.makeText(this, "Image saved to: " + image.getAbsolutePath(), Toast.LENGTH_LONG).show();
        return image;
    }

}