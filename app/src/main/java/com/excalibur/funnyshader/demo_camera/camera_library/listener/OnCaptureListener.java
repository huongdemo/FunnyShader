package com.excalibur.funnyshader.demo_camera.camera_library.listener;

import android.graphics.Bitmap;


/**
 * 截帧监听器
 * Created by cain.huang on 2017/12/27.
 */
public interface OnCaptureListener {
    // 截帧回调
    void onCapture(Bitmap bitmap);
}