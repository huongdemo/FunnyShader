package com.excalibur.funnyshader.custom_nekocode_cain_camera;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.excalibur.funnyshader.R;
import com.excalibur.funnyshader.custom_nekocode_cain_camera.fragment.CameraPreviewFragment;
import com.excalibur.funnyshader.custom_nekocode_cain_camera.utils.NotchUtils;

public class CameraActivity extends AppCompatActivity {

    private static final String FRAGMENT_CAMERA = "fragment_camera";

    private CameraPreviewFragment mPreviewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState && mPreviewFragment == null) {
            mPreviewFragment = new CameraPreviewFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mPreviewFragment, FRAGMENT_CAMERA)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleFullScreen();
    }

    private void handleFullScreen() {
        // Whether it is full screen
        if (NotchUtils.hasNotchScreen(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                getWindow().setAttributes(lp);
            }
        }
    }

}


