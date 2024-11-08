package com.example.throwyourphoneawayitsgood4u.sensors

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.FEATURE_SENSOR_LIGHT
import android.hardware.Sensor
import android.hardware.SensorManager

class LightSensor(context : Context) : AndroidSensor(
    context = context,
    sensorType = Sensor.TYPE_LIGHT,
    sensorFeature = FEATURE_SENSOR_LIGHT
) {
}