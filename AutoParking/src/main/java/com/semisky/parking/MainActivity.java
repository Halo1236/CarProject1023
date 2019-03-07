package com.semisky.parking;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.semisky.parking.R;

public class MainActivity extends Activity implements SurfaceHolder.Callback{
    private static final String TAG = "johliu_MainActivity";

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mSurfaceView = (SurfaceView)findViewById(R.id.main_surfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        intiCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
//        releaseCamera();
    }

    Camera mCamera;
    private void intiCamera(){
        if(null != mCamera){
            return;
        }
        mCamera = Camera.open(0);
        try {
            mCamera.setPreviewDisplay(this.mSurfaceHolder);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(1024,600);
            mCamera.setDisplayOrientation(0);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            Log.e(TAG,"intiCamera() success !!!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"intiCamera() fail !!!");
        }
    }

    private void releaseCamera(){
        if(null != mCamera){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    public void onclick_open(View v){
        Log.i(TAG,"onclick_open() ...");
        intiCamera();
    }

    public void onclick_close(View v){
        releaseCamera();
    }
}
