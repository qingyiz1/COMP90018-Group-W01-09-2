package com.example.comp90018.Activity.Home;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.camera2.*;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    private static final int CHOOSE_PHOTO = 0;
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
    private String filePath;
    private int pictureId =0;

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
                openAlbum();
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//                final int ACTIVITY_SELECT_IMAGE = 0;
//                startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);
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
            Image image = reader.acquireLatestImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);//将image对象转化为byte，再转化为bitmap
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            savePhoto(bytes);
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
                mSession.setRepeatingRequest(mPreviewBuilder.build(), mSessionCaptureCallback, mHandler);
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
        //回调做图片的保存工作
        //拍摄完全完成并且成功，拍摄图像数据可用时回调
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            mSession = session;
        }
        //拍摄进行中，但拍摄图像数据部分可用时回调
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            mSession = session;
        }
        //拍摄失败时回调
        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            Toast.makeText(getApplicationContext(), "Fail to take photo", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(CameraActivity.this, PostActivity.class);
            startActivity(intent);

        }
    };

    public void takePicture() {
        try {
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            //用来设置拍照请求的request
            captureRequestBuilder.addTarget(mImageReader.getSurface());
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(cameraCharacteristics, rotation));//使图片做顺时针旋转
            CaptureRequest mCaptureRequest = captureRequestBuilder.build();
            mSession.capture(mCaptureRequest, mSessionCaptureCallback, mHandler);
            //savePicture(bytes);
        } catch (CameraAccessException e) {
            e.printStackTrace();
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
    public void savePhoto(byte[] imgBytes) {
        pictureId++;
        String imgPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/WordRecognition_picture" + pictureId + ".jpg";
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);//图像数据被转化为bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, 640, false);
        File outputImage = new File(imgPath);
        FileOutputStream outputStream = null;
        try {
            if (outputImage.exists()) {
                outputImage.delete();//存在就删除
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream = new FileOutputStream(outputImage);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);//第二个参数为压缩质量
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //及时更新到系统相册
        MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory() + "//DCIM//Camera//WordRecogniton_picture" + pictureId + ".jpg"}, null, null);//"//"可以用File.separator代替
        // Pass the new image to the next edit view
        BitmapStore.setBitmap(scaledBitmap);
        Intent intent = new Intent();
        intent.putExtra("post_img", outputImage.toString());
        intent.setClass(CameraActivity.this, PostActivity.class);
        startActivity(intent);
    }

    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");//选择照片后毁掉onActivityResult方法
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                String imgPath=null;
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT > 19) {
                        imgPath = handlerImgOnNewVersion(data);
                    } else {
                        imgPath = handlerImgOnOldVersion(data);
                    }
                    //以上获取了选择的图片的路径，在这里可以应用这个路径，做一些想要做的东西
                }
                break;
            default:
        }
    }
    private String handlerImgOnOldVersion(Intent data) {
        Uri uri = data.getData();
        String imgPath = getImagePath(uri, null);
        return imgPath;
    }
    private String handlerImgOnNewVersion(Intent data) {
        String imgPath = null;//选择的图片的路径
        Uri uri = data.getData();//选择图片的结果,即图片地址的封装，接下来对其进行解析
        if (DocumentsContract.isDocumentUri(this, uri)) {//判断是否是document类型
            String docId = DocumentsContract.getDocumentId(uri);
            switch (uri.getAuthority())//就是获取uri的最开头部分
            {
                case "com.android.providers.media.documents":
                    String id = docId.split(":")[1];//解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imgPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    break;
                case "com.android.providers.downloads.documents":
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imgPath = getImagePath(contentUri, null);
                    break;
                default:
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imgPath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imgPath = uri.getPath();
        }
        return imgPath;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}