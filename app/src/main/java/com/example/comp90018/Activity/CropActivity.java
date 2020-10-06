package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CropActivity extends AppCompatActivity {

    private Bitmap rawBitmap = null;
    private Bitmap cropBitmap = null;
    private CropView cropview = null;

    private Button btnOK = null;
    private Button btnReturn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        cropview = (CropView) findViewById(R.id.crop_view);
        Intent intent = getIntent();
        if (intent != null) {
            rawBitmap = BitmapStore.getBitmap();
            cropview.setImageBitmap(rawBitmap);
        }

        btnOK = (Button) findViewById(R.id.button_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropBitmap = cropview.getCroppedImage();
                cropview.setImageBitmap(cropBitmap);
            }
        });

        btnReturn = (Button) findViewById(R.id.button_return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cropBitmap != null) {
                    BitmapStore.setBitmap(cropBitmap);
                    Intent intent = new Intent(CropActivity.this, EditPhotoActivity.class);
                    startActivity(intent);
                } else {
                    CropActivity.this.finish();
                }
            }
        });
    }
}