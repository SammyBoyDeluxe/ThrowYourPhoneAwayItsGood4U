package com.example.throwyourphoneawayitsgood4u.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**This abstract class represents a sensor in an android-application context (where it either exists or not).
 * Each sensor associated with an android device can extend the AndroidSensor class.
 *
 * An AndroidSensor is instanced with the following attributes :
 *                               val context: Context,
 *                               val sensorFeature : String,
 *                              sensorType : Int
 *
 * The AndroidSensor in itself is a type of (see : Subclass) MeasureableSensor, that represents an entity (sensor)
 * capable of connection and measurement (listening)
 */

abstract class AndroidSensor(private val context: Context,
                             private val sensorFeature : String,
                             sensorType : Int
    ) : MeasurableSensor(sensorType), SensorEventListener {

    /*We check the application context for the confirmation of sensorexistence and update the sensorExists-boolean
     value depending to true or false depending on this outcome - This then gives us exception-handling for non-availiability of the wanted sensor*/
    override val sensorExists: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)
    /*The sensor-manager should always be initialized since this is our gateway to check for
    the existence of a given sensor and register/unregister listeners at that sensor */
    private lateinit var sensorManager : SensorManager
    /*Any android sensor could have access to a android.hardware.Sensor
    but it is only given by the availiability of the sensor on the device ; Hence, nullable */
    private var sensor : Sensor? = null

    override fun startListening() {
        /*if a given sensor exists we want to register the given listener*/
        if(!sensorExists){
            System.out.println("Sensor not available at current device")
            return
        }


        if(!::sensorManager.isInitialized && sensor == null){
            /*This condition represents the initial state of any AndroidSensor-object and is where we should
           * initialize both the sensormanager and sensor*/
            sensorManager = context.getSystemService(SensorManager::class.java) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        /*If the sensor is initalized we register the listener to this sensor for listening*/
        sensor?.let {

            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)
        }


    }

    override fun stopListening() {
        if(!::sensorManager.isInitialized || sensor == null){
            /*This condition represents if there even is a sensor to measure;
            if not we should just return
             */
            return
        }
        else{
            sensorManager.unregisterListener(this)

        }


    }

    override fun onSensorChanged(event: SensorEvent?) {
    if(sensorExists) {
        if (event != null) {
            /*We specify the sensor type and match this to make sure we only activate on relevant sensorchanges
        * - Since we implement SensorEventActionListener that means any sensorevents will trigger the class*/
            if (event.sensor.type == sensorType) {


                onSensorValuesChanged?.invoke(event.values.toList())


            }
        }
    }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}