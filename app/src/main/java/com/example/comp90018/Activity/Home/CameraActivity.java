package com.example.comp90018.Activity.Home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.camera2.*;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.comp90018.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class CameraActivity extends Activity {

    //private Camera camera = null;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private SurfaceView mSurfaceView;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mSession;
    private SurfaceHolder mSurfaceViewHolder;
    //private SurfaceView cameraSurfaceView = null;
    //private SurfaceHolder cameraSurfaceHolder = null;
    private Handler mHandler;
    private String mCameraId;
    private ImageReader mImageReader;
    private Handler mainHandler;
    private boolean previewing = false;
    RelativeLayout relativeLayout;

    private ImageButton btnCapture = null;
    private ToggleButton btnFlash = null;
    private ImageButton btnGallery = null;
    private ImageView img_show;
    private byte[] bytes;

    // On create, first initialize the view elements
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Button to Capture:
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        relativeLayout=(RelativeLayout) findViewById(R.id.containerImg);
        relativeLayout.setDrawingCacheEnabled(true);

        mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        initSurfaceView();

        btnCapture = (ImageButton) findViewById(R.id.button1);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        // Button to Control FlashLight
        btnFlash = (ToggleButton) findViewById(R.id.button_flash);
        // BugFixed - when return with isChecked state, there is a error message on screen
        // saying that camera isn't working properly, but does not affect the functioning

        // Button to Enter Gallery
        // Bug-fixed need to access all the pictures
        btnGallery = (ImageButton) findViewById(R.id.button_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 0;
                startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
            }
        });
    }

    public void initSurfaceView(){
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mSurfaceViewHolder = mSurfaceView.getHolder();
        //通过SurfaceViewHolder可以对SurfaceView进行管理
        mSurfaceViewHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCameraAndPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //释放camera
                if (mCameraDevice != null) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                if(previewing) {
//                    mCameraDevice.close();
//                    previewing = false;
//                }
                //parameters.setPictureSize(640, 480);
            }
        });
    }
    @TargetApi(19)
    public void initCameraAndPreview() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());//用来处理ui线程的handler，即ui线程
        try {
            mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
            mImageReader = ImageReader.newInstance(mSurfaceView.getWidth(), mSurfaceView.getHeight(), ImageFormat.JPEG,1);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mainHandler);//这里必须传入mainHandler，因为涉及到了Ui操作
            mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }else {
                mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler);
            }
        } catch (CameraAccessException e) {
            Toast.makeText(getApplicationContext(), "Device Camera is " +
                    "not working properly, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                try {
                    mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } else {
                // Permission Denied
                Toast.makeText(this, "Please accept camera request!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(CameraActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //进行相片存储
            mCameraDevice.close();
            Image image = reader.acquireNextImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);//将image对象转化为byte，再转化为bitmap
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                img_show.setImageBitmap(bitmap);
            }
        }
    };

    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            try {
                takePreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(getApplicationContext(), "Camera open error", Toast.LENGTH_SHORT).show();
        }
    };

    public void takePreview() throws CameraAccessException {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        mPreviewBuilder.addTarget(mSurfaceViewHolder.getSurface());
        mCameraDevice.createCaptureSession(Arrays.asList(mSurfaceViewHolder.getSurface(), mImageReader.getSurface()), mSessionPreviewStateCallback, mHandler);
    }

    private CameraCaptureSession.StateCallback mSessionPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mSession = session;
            //配置完毕开始预览
            try {
                /**
                 * 设置你需要配置的参数
                 */
                // set camera and preview size
                mPreviewBuilder.set(CaptureRequest.JPEG_THUMBNAIL_SIZE, new Size(640, 480));

                btnFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            // The toggle is enabled
                            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.FLASH_MODE_TORCH);
                            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        } else {
                            // The toggle is disabled
                            mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.FLASH_MODE_OFF);
                            mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
                        }
                    }
                });
                //无限次的重复获取图像
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
                previewing = true;
            } catch (CameraAccessException e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(getApplicationContext(), "Error configuration", Toast.LENGTH_SHORT).show();
        }
    };

    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            mSession = session;
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            mSession = session;
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
        }
    };

    public void takePicture() {
        try {
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);//用来设置拍照请求的request
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            //captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(cameraCharacteristics, rotation));//使图片做顺时针旋转
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mSession.capture(mCaptureRequest, null, mHandler);
            savePicture(bytes);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    //获取图片应该旋转的角度，使图片竖直
    public int getOrientation(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0:
                return 90;
            case Surface.ROTATION_90:
                return 0;
            case Surface.ROTATION_180:
                return 270;
            case Surface.ROTATION_270:
                return 180;
            default:
                return 0;
        }
    }
    //获取图片应该旋转的角度，使图片竖直
    private int getJpegOrientation(CameraCharacteristics c, int deviceOrientation) {
        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0;
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        // LENS_FACING相对于设备屏幕的方向,LENS_FACING_FRONT相机设备面向与设备屏幕相同的方向
        boolean facingFront = c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;

        return jpegOrientation;
    }

    public void savePicture(byte[] data){
        Bitmap cameraBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        File storagePath = new File(Environment.getExternalStorageDirectory()
                + "/DCIM/100ANDRO/");
        storagePath.mkdirs();

        File myImage = new File(storagePath, Long.toString(System.currentTimeMillis()) + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(myImage);
            cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch(FileNotFoundException e) {
            Log.d("In Saving File", e + "");
        } catch(IOException e) {
            Log.d("In Saving File", e + "");
        }
        // Send the image file to the gallery
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myImage)));

        // Pass the new image to the next edit view
        BitmapStore.setBitmap(cameraBitmap);
        Intent intent = new Intent();
        intent.putExtra("post_img", myImage.toString());
        intent.setClass(CameraActivity.this, Post.class);
        startActivity(intent);
    }

    // Bug-fixed: out of memory error when go back and select photo 2nd time
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();

            try{
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(selectedImage));
                BitmapStore.setBitmap(yourSelectedImage);
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }
            // Pass the new image to the next edit view
            Intent intent = new Intent();
            intent.setClass(CameraActivity.this, Post.class);
            startActivity(intent);
        }
    }
}