package com.example.collectdata;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collectdata.bean.Recognition;
import com.example.collectdata.ml.faceemotion.PredictFaceEmotion;

import java.io.IOException;
import java.util.List;

public class ActivityTakeSelfie extends AppCompatActivity {

    private ImageView imageView;
    private Context context;
    private Bitmap imageBitmap;
    private PredictFaceEmotion predictFaceEmotion;
    private List<Recognition> predictions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_selfie);

        context = ActivityTakeSelfie.this;

        // showing the back button in action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Widget
        imageView = findViewById(R.id.imageView);

        btnTakeSelfie();
    }

    public void btnSave(View view) {
        // TODO :: Save the Picture
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = null;
//        try {
//            image = File.createTempFile(
//                    imageFileName,  /* prefix */
//                    ".jpg",         /* suffix */
//                    storageDir      /* directory */
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Save a file: path for use with ACTION_VIEW intents
//        String currentPhotoPath = image.getAbsolutePath();
//        return image;

        try {
            predictFaceEmotion = new PredictFaceEmotion(this);
            predictions = predictFaceEmotion.recognizeImage(imageBitmap, 0);
            Log.i("ollo", "==================================");
            for(Recognition r : predictions) {
                Log.i("ollo", "Prediction :: " + r.getName() + " = " + r.getConfidence());
            }
            Log.i("ollo", "==================================");
        } catch (IOException e) {
            Log.i("ollo", "Error :: " + e.getMessage());
        }


        // TODO :: Update DB
        Toast.makeText(context, "Image Saved...", Toast.LENGTH_SHORT).show();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        finish();
    }

    public void btnRetake(View view) {
        imageBitmap = null;
        btnTakeSelfie();
    }

    /**
     * Take selfie
     */
    public void btnTakeSelfie() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Error in taking picture : ", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}