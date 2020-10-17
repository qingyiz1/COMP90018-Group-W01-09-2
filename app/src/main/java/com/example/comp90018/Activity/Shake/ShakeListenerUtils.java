package com.example.comp90018.Activity.Shake;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ShakeListenerUtils implements SensorEventListener
{
    public ShakeListenerUtils()
    {
        super();
    }

    @SuppressLint("ShowToast")
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        int sensorType = event.sensor.getType();

        if (sensorType == Sensor.TYPE_ACCELEROMETER)
        {

            //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = event.values;

                /*正常情况下，任意轴数值最大就在9.8~10之间，只有在突然摇动手机
                  的时候，瞬时加速度才会突然增大或减少。监听任一轴的加速度大于19即可
                */

            if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15 || Math
                    .abs(values[2]) > 15))
            {
               /// startAnim ();   //摇了以后干什么

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        //当传感器精度改变时回调该方法，Do nothing.
    }

}
