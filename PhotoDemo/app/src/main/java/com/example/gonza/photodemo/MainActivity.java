package com.example.gonza.photodemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private boolean option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void detectFaces(Uri imageUri) {
        InputStream inputStream = null; // Reading image
        try {
            inputStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            Log.e("Error",e.toString());
        }
        Bitmap bitMap = BitmapFactory.decodeStream(inputStream); // Converting to bitMap the image

        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setTrackingEnabled(false)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitMap).build();

        SparseArray<Face> faces = detector.detect(frame);
        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies is not available");
            return;
        }

        MyView myView = (MyView)findViewById(R.id.my_view);
        myView.setContent(bitMap,faces);
        detector.release();
    }

    private void detectFaces(Bitmap bMap) {

        Bitmap bitMap = bMap; // Converting to bitMap the image

        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setTrackingEnabled(false)
                .setMode(FaceDetector.FAST_MODE)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitMap).build();

        SparseArray<Face> faces = detector.detect(frame);
        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies is not available");
            return;
        }

        MyView myView = (MyView)findViewById(R.id.my_view);
        myView.setContent(bitMap,faces);
        detector.release();

    }

    private void openGallery(){
        option = true;
        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    public void addImageAction(View view) {
        openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(option == true && resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            detectFaces(imageUri);
        }
        else if (option == false && data != null){
            Bitmap bitMap = (Bitmap) data.getExtras().get("data");
            detectFaces(bitMap);
        }
        else
            Toast.makeText(this,"No hay Imagen",Toast.LENGTH_LONG).show();
    }

    public void openCameraAction(View view) {
        option = false;
        Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(gallery,0);
    }
}
