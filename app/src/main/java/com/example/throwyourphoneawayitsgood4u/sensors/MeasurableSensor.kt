package com.example.throwyourphoneawayitsgood4u.sensors

/**The MeasureableSensor is a sensor that can be used for measuring
 * i.e it exists on the current device
 *
 * It is instanced with an integer that represents the integer-constant associated with such a sensor.
 *
 */
abstract class MeasurableSensor(protected var sensorType : Int ) {
    /**Represents the values returned by onSensorValuesChangedCallback
     *
     */
    protected var onSensorValuesChanged : ((List<Float>)->Unit)? = null

    /**Represents if a given sensor exists on the current device, updated on check
     *
     */
    abstract val sensorExists : Boolean

    /**Start listening, should be called onResume(). This will prevent energy leakage to the sensor
     * since that can radically reduce battery time.
     */
    abstract fun startListening()

    /**Stops the associated listener to prevent energy leaking and increase expected battery life.
     * Should always be called onStop()
     */
    abstract fun stopListening()

    public fun setOnSensorValuesChangedListener(listener : (List<Float>)->Unit){

        onSensorValuesChanged = listener

    }





}