package com.hfad.myflashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.security.Policy;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements SoundPool.OnLoadCompleteListener {

    private int sound;
    private SoundPool soundPool;
    private Camera camera;
    private CameraManager cameraManager;
    Policy.Parameters parameters;
    private SwitchCompat switchCompat;
    private Context context;
    private boolean flash_status = true;

    private ImageView imageViewChange;
    private ImageView strobe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        imageViewChange = findViewById(R.id.imageView4);
        strobe = findViewById(R.id.strobe);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createSoundPoolWithBuilder();
        } else {
            createSoundPoolWithConstructor();
        }

        soundPool.setOnLoadCompleteListener(this);
        sound = soundPool.load(this, R.raw.click, 1);

        boolean isCameraFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        switchCompat = findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean b) {
                        if (isCameraFlash) {
                            if (switchCompat.isChecked())
                                setFlashLigthOn();
                            else
                                setFlashLightOff();
                        } else {
                            Toast.makeText(MainActivity.this, "No flash available on your device",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createSoundPoolWithBuilder() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(1).build();
    }

    @SuppressWarnings("deprecation")
    protected void createSoundPoolWithConstructor() {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFlashLightOff() {
        imageViewChange.setImageResource(R.drawable.off);
        soundPool.play(sound, 1, 1, 0, 0, 1);
        strobe.setImageResource(R.drawable.clear);
        new Thread(new Runnable() {
            @Override
            public void run() {
                cameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
                if (cameraManager != null) {
                    String cameraId = null;
                    try {
                        cameraId = cameraManager.getCameraIdList()[0];
                        flash_status = false;
                        cameraManager.setTorchMode(cameraId, flash_status);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setFlashLigthOn() {
        imageViewChange.setImageResource(R.drawable.on);
        soundPool.play(sound, 1, 1, 0, 0, 1);
        strobe.setImageResource(R.drawable.ic_str);
        new Thread(new Runnable() {
            @Override
            public void run() {
                cameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
                if (cameraManager != null) {
                    String cameraId = null;
                    try {
                        cameraId = cameraManager.getCameraIdList()[0];
                        flash_status = true;
                        cameraManager.setTorchMode(cameraId, flash_status);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}