package com.hfad.myflashlight;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class FlashClass {

    private final Context context;
    private boolean flash_status = false;
    private CameraManager cameraManager;
    private boolean strob_status = false;

    public FlashClass(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFlashLightOn() {
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager != null) {
            String cameraId = null;
            try {
                cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, false);
                cameraManager.setTorchMode(cameraId, true);
                flash_status = true;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setFlashLightOff() {
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager != null) {
            String cameraId = null;
            try {
                cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, false);
                flash_status = false;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void strobeOn() {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                strob_status = true;
                cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
                if (cameraManager != null) {
                    String cameraId = null;
                    try {
                        cameraId = cameraManager.getCameraIdList()[0];
                        do {
                            cameraManager.setTorchMode(cameraId, true);
                            int freq = 5;
                            Thread.sleep(100 - freq);
                            cameraManager.setTorchMode(cameraId, false);
                            Thread.sleep(freq);
                        }
                        while (strob_status);
                    } catch (CameraAccessException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void strobeOff() {
        strob_status = false;
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager != null) {
            String cameraId = null;
            try {
                cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isStrobStatusOn() {
        return strob_status;
    }
}
