package com.hfad.myflashlight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.security.Policy;

public class MainActivity extends AppCompatActivity implements SoundPool.OnLoadCompleteListener {

    private int sound;
    private SoundPool soundPool;
    private Camera camera;
    Policy.Parameters parameters;
    private SwitchCompat switchCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

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

    private void setFlashLightOff() {
        soundPool.play(sound, 1, 1, 0, 0, 1);
    }

    private void setFlashLigthOn() {
        soundPool.play(sound, 1, 1, 0, 0, 1);
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}