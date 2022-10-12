package com.excalibur.funnyshader.custom_nekocode_cain_camera.camera_library.presenter;

import com.excalibur.funnyshader.custom_nekocode_cain_camera.camera_library.camera.CameraParam;
import com.excalibur.funnyshader.custom_nekocode_cain_camera.fragment.CameraPreviewFragment;

public class CameraPreviewPresenter extends PreviewPresenter<CameraPreviewFragment> {

    private static final String TAG = "CameraPreviewPresenter";

    // 渲染器
    //private final CameraRenderer mCameraRenderer;

    // 预览参数
    private CameraParam mCameraParam;

    public CameraPreviewPresenter(CameraPreviewFragment target) {
        super(target);
        //mCameraParam = CameraParam.getInstance();

        //mCameraRenderer = new CameraRenderer();

        // 视频录制器
        //mVideoParams = new VideoParams();
        //mAudioParams = new AudioParams();

        // 命令行编辑器
        //mCommandEditor = new CainCommandEditor();
    }

}
