package com.example.tamoor.DarkLight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int delay = 500                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       ;
    final int time = 5;
    static Camera camera = null;
    boolean isFlashOn = true;
    private static int seek_progress = 0;
    private static boolean blinkZero = true;
    Context context;
    Camera.Parameters params;
    private static boolean cameraService = false;

    public static String FACEBOOK_URL = "https://www.facebook.com/darklightapp";
    public static String FACEBOOK_PAGE_ID = "darklightapp";

    ImageView white_light, blink_light, off_light, on_light;
    RelativeLayout off_layout, on_layout;
    Button rate_btn, exit_btn, close_btn, fb_button, gif_buttons;
    AdView adView;
    SeekBar seekBar;
    RecyclerView recyclerView;
    Dialog yourDialog;
    private List<ImageModel> images;
    ImagesAdapter imagesAdapter;
    Dialog dialog;
    static boolean flashLightStatus = true;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getBaseContext();

        yourDialog = new Dialog(this);
        yourDialog.setContentView(R.layout.dialog);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.gifs_layout);

        builder = new AlertDialog.Builder(MainActivity.this);

        initialization();
        setUpAdapter();
        onClickListener();
        adMobTask();
    }

    private void initialization() {
        off_layout = findViewById(R.id.frame_off);
        on_layout = findViewById(R.id.frame_on);
        off_light = findViewById(R.id.off_image);
        on_light = findViewById(R.id.off_image_on);
        rate_btn = findViewById(R.id.rate);
        exit_btn = findViewById(R.id.exit_btn);
        adView = findViewById(R.id.adView);
        white_light = findViewById(R.id.white_light);
        blink_light = findViewById(R.id.blink_light);
        seekBar = yourDialog.findViewById(R.id.your_dialog_seekbar);
        close_btn = yourDialog.findViewById(R.id.dialog_button);
        fb_button = findViewById(R.id.fb_button);
        gif_buttons = findViewById(R.id.gifs_button);
        recyclerView = dialog.findViewById(R.id.recycler);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        } else {
            if(!cameraService) {
                initializeCamera();
            }
        }
    }

    private void onClickListener() {

        final boolean hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        off_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    onFlashLight();
                } else {
                    Toast.makeText(MainActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }
                on_layout.setVisibility(View.VISIBLE);
                off_layout.setVisibility(View.GONE);
            }
        });

        on_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    offFlashLight();
                } else {
                    Toast.makeText(MainActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }
                off_layout.setVisibility(View.VISIBLE);
                on_layout.setVisibility(View.GONE);
            }
        });

        white_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WhiteLightActivity.class);
                startActivity(intent);
            }
        });

        blink_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashLightStatus = true;
                yourDialog.show();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek_progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seek_progress == 0){
                    blinkZero = false;
                } else if (seek_progress == 1) {
                    blinkZero = true;
                    blink(5);
                } else if (seek_progress == 2) {
                    blinkZero = true;
                    blink(4);
                } else if (seek_progress == 3) {
                    blinkZero = true;
                    blink(3);
                } else if (seek_progress == 4) {
                    blinkZero = true;
                    blink(2);
                } else if (seek_progress == 5) {
                    blinkZero = true;
                    blink(1);
                }
            }
        });

        fb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });

        gif_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGifImages();
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashLightStatus = false;
                if(isFlashOn){
                    offFlashLight();
                }
                yourDialog.dismiss();
            }
        });

        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = "com.lohit.flashapp";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public void blink(final int delay) {

            Thread t = new Thread() {
                public void run() {
                    try {

                        while (flashLightStatus && blinkZero) {
                            onFlashLight();
                            sleep(delay*250);
                            offFlashLight();
                            sleep(delay*250);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
    }

    private void confirm() {

        builder.setMessage("Do you want to visit Facebook Page of Flash Light App")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String fbURL = getFacebookPageURL(context);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbURL));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create()
                .show();
    }

    private void initializeCamera() {
        if(getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();
            isFlashOn = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            params = camera.getParameters();
            cameraService = true;
        }
    }

    private void onFlashLight() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager cameraManager = null;
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            String cameraId = null;
            try {
                if (cameraManager != null) {
                    cameraId = cameraManager.getCameraIdList()[0];
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            try {
                if (cameraManager != null) {
                    cameraManager.setTorchMode(cameraId, true);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

//            isFlashOn = false;
        } else {
            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
//            isFlashOn = false;
        }
    }

    private void offFlashLight() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager cameraManager = null;
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            String cameraId = null;
            try {
                if (cameraManager != null) {
                    cameraId = cameraManager.getCameraIdList()[0];
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            try {
                if (cameraManager != null) {
                    cameraManager.setTorchMode(cameraId, false);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
//            isFlashOn = true;
        }
        else {

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
//            isFlashOn = true;
        }
    }
    
    private void loadGifImages() {

        images.clear();
        images.addAll(loadImages());
        imagesAdapter.notifyDataSetChanged();
        dialog.show();
    }

    private List<ImageModel> loadImages() {
        List<ImageModel> data = new ArrayList<>();
        data.add(new ImageModel( R.mipmap.blue_red));
        data.add(new ImageModel( R.mipmap.grey_effects));
        data.add(new ImageModel( R.mipmap.red_blue));
        data.add(new ImageModel( R.mipmap.red_light));
        data.add(new ImageModel( R.mipmap.red_mag_blue));
        data.add(new ImageModel( R.mipmap.yellow));

        return data;
    }

    private void setUpAdapter() {
        images = new ArrayList<>();
        imagesAdapter = new ImagesAdapter(images, MainActivity.this);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setAdapter(imagesAdapter);
    }

    private void adMobTask() {
        AdRequest adRequest = new AdRequest.Builder() .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
    }

    private void exit() {
        finish();
    }
}
