package com.excalibur.funnyshader.custom_nekocode_cain_camera.camera_library.presenter;

import androidx.fragment.app.Fragment;

public abstract class PreviewPresenter<T extends Fragment> extends IPresenter<T> {

    PreviewPresenter(T target) {
        super(target);
    }

}
