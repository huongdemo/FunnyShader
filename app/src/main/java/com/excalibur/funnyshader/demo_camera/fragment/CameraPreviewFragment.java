package com.excalibur.funnyshader.demo_camera.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.excalibur.funnyshader.R;
import com.excalibur.funnyshader.demo_camera.camera_library.camera.CameraParam;
import com.excalibur.funnyshader.demo_camera.camera_library.camera.CameraXController;
import com.excalibur.funnyshader.demo_camera.camera_library.camera.ICameraController;
import com.excalibur.funnyshader.demo_camera.camera_library.camera.OnFrameAvailableListener;
import com.excalibur.funnyshader.demo_camera.camera_library.camera.OnSurfaceTextureListener;
import com.excalibur.funnyshader.demo_camera.camera_library.camera.PreviewCallback;
import com.excalibur.funnyshader.demo_camera.camera_library.listener.OnCaptureListener;
import com.excalibur.funnyshader.demo_camera.camera_library.listener.OnFpsListener;
import com.excalibur.funnyshader.demo_camera.camera_library.listener.OnPreviewCaptureListener;
import com.excalibur.funnyshader.demo_camera.camera_library.presenter.CameraPreviewPresenter;
import com.excalibur.funnyshader.demo_camera.camera_library.render.CameraRenderer;
import com.excalibur.funnyshader.demo_camera.camera_library.utils.PathConstraints;
import com.excalibur.funnyshader.demo_camera.camera_library.widget.CameraMeasureFrameLayout;
import com.excalibur.funnyshader.demo_camera.camera_library.widget.CameraTextureView;
import com.excalibur.funnyshader.demo_camera.camera_library.widget.PreviewMeasureListener;
import com.excalibur.funnyshader.demo_camera.utils.BitmapUtils;
import com.excalibur.funnyshader.demo_camera.utils.PermissionUtils;
import com.excalibur.funnyshader.demo_camera.utils.widget.RoundOutlineProvider;

public class CameraPreviewFragment extends Fragment
        implements PreviewCallback, OnCaptureListener, OnFpsListener, OnSurfaceTextureListener, OnFrameAvailableListener {

    private static final String TAG = "CameraPreviewFragment";

    //Fragment main page
    private View mContentView;

    private FragmentActivity mActivity;

    private final Handler mMainHandler;

    private CameraParam mCameraParam;

    private final CameraRenderer mCameraRenderer;

    private CameraMeasureFrameLayout mPreviewLayout;
    private CameraTextureView mCameraTextureView;


    private TextView mFpsView;

    private ICameraController mCameraController;

    public CameraPreviewFragment() {
        mCameraParam = CameraParam.getInstance();
        mMainHandler = new Handler(Looper.getMainLooper());
        mCameraRenderer = new CameraRenderer(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        mContentView = inflater.inflate(R.layout.fragment_camera_preview, container, false);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isCameraEnable()) {
            initView(mContentView);
        } else {
            PermissionUtils.requestCameraPermission(this);
        }

        if (PermissionUtils.permissionChecking(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //mLocalImageLoader = LoaderManager.getInstance(this);
            //mLocalImageLoader.initLoader(ALBUM_LOADER_ID, null, this);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (FragmentActivity) context;
        } else {
            mActivity = getActivity();
        }

        mCameraRenderer.initRenderer();

        mCameraController = new CameraXController(mActivity);

        mCameraController.setPreviewCallback(this);
        mCameraController.setOnFrameAvailableListener(this);
        mCameraController.setOnSurfaceTextureListener(this);

        //mCameraParam.brightness = -1;

        Log.d(TAG, "onAttach: ");
    }


    private void initView(View view) {
        initPreviewSurface();
        //initPreviewTopbar();
        //initBottomLayout(view);
        //initCameraTabView();
    }

    private void initPreviewSurface() {
        mFpsView = mContentView.findViewById(R.id.tv_fps);
        mPreviewLayout = mContentView.findViewById(R.id.layout_camera_preview);
        mCameraTextureView = new CameraTextureView(mActivity);
        mCameraTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        mPreviewLayout.addView(mCameraTextureView);

        //mCameraTextureView.setOutlineProvider(new RoundOutlineProvider(getResources().getDimension(R.dimen.dp7)));
        //mCameraTextureView.setClipToOutline(true);
        //mPreviewLayout.setOnMeasureListener(new PreviewMeasureListener(mPreviewLayout));
        mContentView.findViewById(R.id.btn_switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraController.switchCamera();
            }
        });
    }

    private boolean isCameraEnable() {
        return PermissionUtils.permissionChecking(mActivity, Manifest.permission.CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();
        openCamera();

        mCameraParam.captureCallback = this;
        mCameraParam.fpsCallback = this;
    }

    @Override
    public void onPause() {
        super.onPause();
        mCameraRenderer.onPause();
        closeCamera();
        mCameraParam.captureCallback = null;
        mCameraParam.fpsCallback = null;
    }

    @Override
    public void onDestroy() {

        mMainHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        // 销毁人脸检测器
        //FaceTracker.getInstance().destroyTracker();
        // 清理关键点
        //LandmarkEngine.getInstance().clearAll();
        //if (mHWMediaRecorder != null) {
        //    mHWMediaRecorder.release();
        //    mHWMediaRecorder = null;
        //}
        //if (mCommandEditor != null) {
        //    mCommandEditor.release();
        //    mCommandEditor = null;
        //}
    }

    @Override
    public void onDetach() {
        mActivity = null;
        mCameraRenderer.destroyRenderer();
        super.onDetach();
    }

    @Override
    public void onCapture(Bitmap bitmap) {
        String filePath = PathConstraints.getImageCachePath(mActivity);
        BitmapUtils.saveBitmap(filePath, bitmap);
        if (mCameraParam.captureListener != null) {
            mCameraParam.captureListener.onMediaSelectedListener(filePath, OnPreviewCaptureListener.MediaTypePicture);
        }
    }

    @Override
    public void onFpsCallback(float fps) {
        mMainHandler.post(() -> {
            if (mCameraParam.showFps) {
                mFpsView.setText("fps = " + fps);
                mFpsView.setVisibility(View.VISIBLE);
            } else {
                mFpsView.setVisibility(View.GONE);
            }
        });
    }

    private void openCamera() {
        mCameraController.openCamera();
        calculateImageSize();
    }

    private void calculateImageSize() {
        int width;
        int height;
        if (mCameraController.getOrientation() == 90 || mCameraController.getOrientation() == 270) {
            width = mCameraController.getPreviewHeight();
            height = mCameraController.getPreviewWidth();
        } else {
            width = mCameraController.getPreviewWidth();
            height = mCameraController.getPreviewHeight();
        }
        //mVideoParams.setVideoSize(width, height);
        mCameraRenderer.setTextureSize(width, height);
        Log.e("HVV1312","calculateImageSize: "+ width + " x "+ height);
    }

    private void closeCamera() {
        mCameraController.closeCamera();
    }

    // ---------------------------- TextureView SurfaceTexture监听 ---------------------------------
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mCameraRenderer.onSurfaceCreated(surface);
            mCameraRenderer.onSurfaceChanged(width, height);
            Log.d(TAG, "onSurfaceTextureAvailable: ");
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            mCameraRenderer.onSurfaceChanged(width, height);
            Log.d(TAG, "onSurfaceTextureSizeChanged: ");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mCameraRenderer.onSurfaceDestroyed();
            Log.d(TAG, "onSurfaceTextureDestroyed: ");
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.e("HVV1312","onFrameAvailable");
        mCameraRenderer.requestRender();
    }

    @Override
    public void onSurfaceTexturePrepared(@NonNull SurfaceTexture surfaceTexture) {
        //onCameraOpened();
        mCameraRenderer.bindInputSurfaceTexture(surfaceTexture);
    }

    @Override
    public void onPreviewFrame(byte[] data) {
        Log.d(TAG, "onPreviewFrame: width - " + mCameraController.getPreviewWidth()
                + ", height - " + mCameraController.getPreviewHeight());
        //mCameraRenderer.requestRender();
    }
}
