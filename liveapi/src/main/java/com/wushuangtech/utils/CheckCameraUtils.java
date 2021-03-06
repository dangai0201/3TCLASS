package com.wushuangtech.utils;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;

/**
 * Created by redbean
 */

public class CheckCameraUtils {
    /**
     * 判断是否有后置摄像头
     */
    public static boolean hasBackFacingCamera() {
        return checkCameraFacing(CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * 判断是否有前置摄像头
     */
    public static boolean hasFrontFacingCamera() {
        return checkCameraFacing(CameraInfo.CAMERA_FACING_FRONT);
    }

    private static boolean checkCameraFacing(final int facing) {
        if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        CameraInfo info = new CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    private static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }
}