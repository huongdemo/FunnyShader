package com.excalibur.funnyshader.custom_nekocode_cain_camera.camera_library.presenter;

public abstract class IPresenter<T> {

    private T mTarget;

    public IPresenter(T target) {
        mTarget = target;
    }

}
