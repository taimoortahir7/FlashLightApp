package com.example.tamoor.flashlightapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.droidsonroids.gif.GifImageView;

public class ImageActivity extends AppCompatActivity {

    Context context;
    GifImageView imageView;
    int gifDrawableResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        context = getBaseContext();
        gifDrawableResource = getIntent().getIntExtra("image", 0);
        initialization();
        setImage();
    }

    private void initialization() {
        imageView = findViewById(R.id.image_view_item);
    }

    private void setImage() {
        imageView.setImageResource(gifDrawableResource);
    }
}
