package com.example.comp90018.Activity.Shake;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by gl152 on 2017/11/20.
 */

//public class ShakeListenerUtils implements SensorEventListener {
//    private static final String TAG = "ShakeManager";
//    private Context context;
//    private SensorManager sensorManager;
//    private Sensor sensor;
//    private int miniValue = 15;
//    /**
//     * 检测的时间间隔
//     */
//    private static final int UPDATE_INTERVAL = 1000;
//    /**
//     * 上一次检测的时间
//     */
//    private long mLastUpdateTime;
//
//    private onShakeListener onShakeListener;
//    private boolean isShake = false;
//
//    public ShakeListenerUtils(Context context) {
//        this.context = context;
//        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        /**
//         * 传感器类型列表：
//         Sensor.TYPE_ACCELEROMETER:
//         加速计传感器
//         Sensor.TYPE_GYROSCOPE:
//         回转仪传感器
//         Sensor.TYPE_LIGHT:
//         光传感器，动态控制屏幕亮度
//         Sensor.TYPE_MAGNETIC_FIELD:
//         磁场传感器
//         Sensor.TYPE_ORIENTATION:
//         方向传感器
//         Sensor.TYPE_PRESSURE:
//         压力传感器
//         Sensor.TYPE_PROXIMIY:
//         邻近距离传感器
//         Sensor.TYPE_TEMPERATURE:
//         温度传感器
//         */
//        if (sensorManager != null) {
//            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        }
//        registerListener();
//    }
//
//    public void registerListener() {
//
//        /**
//         传感器更新速率：
//         SensorManager.SENSOR_DELAY_FASTEST:
//         指定可能最快的传感器更新速率
//         SensorManager.SENSOR_DELAY_GAME:
//         指定适合在游戏中使用的更新速率
//         SensorManager.SENSOR_DELAY_NORMAL:
//         指定默认的更新速率
//         SensorManager.SENSOR_DELAY_UI:
//         指定适合于更新UI的更新速率
//         */
//        if (sensor != null && sensorManager != null)
//            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
//    }
//
//    public void unregisterListener() {
//        if (sensor != null && sensorManager != null)
//            sensorManager.unregisterListener(this, sensor);
//    }
//
//    //传感器的值改变调用此方法
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float x = event.values[0];//x轴变化的值
//        float y = event.values[1];//y轴变化的值
//        float z = event.values[2];//z轴变化的值,记得减去9.8重力加速度
//
//        if (Math.abs(x) > miniValue || Math.abs(y) > miniValue || Math.abs(z - 9.8) > miniValue) {
//            long currentTime = System.currentTimeMillis();
//            long diffTime = currentTime - mLastUpdateTime;
//            if (diffTime < UPDATE_INTERVAL) {
//                return;
//            }
//            //传感器太灵敏，防止摇一摇触发多次,UPDATE_INTERVAL效果执行多久就设置多久
//            mLastUpdateTime = currentTime;
//            Log.i(TAG, "onSensorChanged: ---x" + x + "--y" + y + "--z" + z);
//            if (onShakeListener != null) {
//                onShakeListener.shake();
//            }
//        }
//
//    }
//
//    //传感器的精确度改变调用此方法
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int i) {
//
//    }
//
//    public void IsShake(boolean isShake) {
//        this.isShake = isShake;
//    }
//
//
//    public void setonShakeListener(onShakeListener onShakeListener) {
//        this.onShakeListener = onShakeListener;
//    }
//
//    interface onShakeListener {
//        void shake();
//    }
//
//}
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;

//public class ShakeListenerUtils implements SensorEventListener
//{
//    public ShakeListenerUtils()
//    {
//        super();
//    }
//
//    @SuppressLint("ShowToast")
//    @Override
//    public void onSensorChanged(SensorEvent event)
//    {
//        int sensorType = event.sensor.getType();
//
//        if (sensorType == Sensor.TYPE_ACCELEROMETER)
//        {
//
//            //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
//            float[] values = event.values;
//
//                /*正常情况下，任意轴数值最大就在9.8~10之间，只有在突然摇动手机
//                  的时候，瞬时加速度才会突然增大或减少。监听任一轴的加速度大于19即可
//                */
//
//            if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15 || Math
//                    .abs(values[2]) > 15))
//            {
//                startAnim ();   //摇了以后干什么
//
//            }
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy)
//    {
//        //当传感器精度改变时回调该方法，Do nothing.
//    }
//    public void startAnim () {   //定义摇一摇动画动画
//        AnimationSet animup = new AnimationSet(true);
//        CustomerAnimation customerAnimation=new CustomerAnimation();
//        customerAnimation.setDuration(1000);
//        customerAnimation.setAnimationListener(this);
//        animup.addAnimation(customerAnimation);
//
//        handImageView.startAnimation(animup);
//
//    }
//
//    class CustomerAnimation extends Animation {
//        private int mWaveTimes=5;//摇摆次数
//        private int mWaveRange=50;//摇摆幅度
//        public CustomerAnimation(){
//        }
//        public CustomerAnimation(int waveTimes,int waveRange){
//            mWaveTimes = waveTimes;
//            mWaveRange = waveRange;
//        }
//
//        @Override
//        protected void applyTransformation(float interpolatedTime,
//                                           Transformation t) {
//            //运用周期性函数，实现左右摇摆
//            t.getMatrix().setTranslate((int)(Math.sin(interpolatedTime*Math.PI*mWaveTimes)*mWaveRange),0);
//        }
//    }
//
//
//}
