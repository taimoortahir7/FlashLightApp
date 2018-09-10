package com.example.tamoor.flashlightapp;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pl.droidsonroids.gif.GifImageView;

public class ImageActivity extends AppCompatActivity {

    Context context;
    GifImageView imageView;
    int gifDrawableResource;
    private static int brightness_value = 0;
    private static float bright = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        context = getBaseContext();
        setBrightness(255);
        gifDrawableResource = getIntent().getIntExtra("image", 0);
        initialization();
        setImage();
    }

    public void setBrightness(int brightness){

        //constrain the value of brightness
        if(brightness < 0) {
            brightness = 0;
        }
        else if(brightness > 255) {
            brightness = 255;
        }
        bright = brightness;
        getPreviousBrightness();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            boolean settingsCanWrite = Settings.System.canWrite(getBaseContext());
            if(!settingsCanWrite) {
                // If do not have write settings permission then open the Can modify system settings panel.
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                startActivity(intent);
            }
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);

        } else {
            android.provider.Settings.System.putInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    brightness);
        }
    }

    private void getPreviousBrightness() {
        try {
            brightness_value = Settings.System.getInt(getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initialization() {
        imageView = findViewById(R.id.image_view_item);
    }

    private void setImage() {
        imageView.setImageResource(gifDrawableResource);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness_value);
        } else {
            android.provider.Settings.System.putInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                    brightness_value);
        }

    }
}
