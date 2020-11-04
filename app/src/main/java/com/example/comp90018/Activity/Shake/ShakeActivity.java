package com.example.comp90018.Activity.Shake;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.comp90018.R;
import java.lang.ref.WeakReference;

public class ShakeActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "ShakeActivity";
    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效

    private Button mButton;
    private ImageView shakePic;
    private boolean isShake = false;

    private LinearLayout mLayout;

    private MyHandler mHandler;
    private int mWeiChatAudio;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_shake);
        fm=getFragmentManager();

        shakePic = findViewById(R.id.shakepicture);
        mLayout=findViewById(R.id.main_linear);
        mHandler = new MyHandler(this);
        mButton=findViewById(R.id.shakeresultbutton);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(ShakeActivity.this, ShakeResultActivity.class);
                startActivity(intent);
            }
        });
        mButton.setVisibility(View.GONE);

        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(this, R.raw.weichat_audio, 1);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    protected void onPause() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                    .abs(z) > 17) && !isShake) {
                isShake = true;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Log.d(TAG, "onSensorChanged: shake");

                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    private class MyHandler extends Handler {
        private WeakReference<ShakeActivity> mReference;
        private ShakeActivity mActivity;
        public MyHandler(ShakeActivity activity) {
            mReference = new WeakReference<ShakeActivity>(activity);
            if (mReference != null) {
                mActivity = mReference.get();
            }
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    mActivity.mVibrator.vibrate(300);
                    mActivity.mSoundPool.play(mActivity.mWeiChatAudio, 1, 1, 0, 0, 1);
                    mActivity.startAnimation(false);
                    break;
                case AGAIN_SHAKE:
                    mActivity.mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    mActivity.isShake = false;
                    mActivity.startAnimation(true);
                    shakePic.setVisibility(View.GONE);
                    mButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


    private void startAnimation(boolean isBack) {
        int type = Animation.RELATIVE_TO_SELF;

        float fromY;
        float toY;
        if (isBack) {
            fromY = -0.5f;
            toY = 0;
        } else {
            fromY = 0;
            toY = -0.5f;
        }

        TranslateAnimation anim = new TranslateAnimation(
                type, 0, type, 0, type, fromY, type, toY
        );
        anim.setDuration(200);
        anim.setFillAfter(true);
        mLayout.startAnimation(anim);

    }

}