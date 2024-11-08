package com.example.throwyourphoneawayitsgood4u.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.throwyourphoneawayitsgood4u.sensors.LinearAccelerationSensor
import com.example.throwyourphoneawayitsgood4u.sensors.LightSensor
import java.text.Format
import java.util.Locale

/**The application is built according to V-VM-M, meaning that each screen has an associated ViewModel handling the
 * orchestration of the apps backend (model ) and the subsequent updating of the UI according to the business logic
 inside of the ViewModel
 */
class IndexViewModel :
    ViewModel {


    private val lightSensor : LightSensor

    private val linearAccelerometer : LinearAccelerationSensor

    constructor(
        lightSensor: LightSensor,
        linearAccelerometer: LinearAccelerationSensor,
    ) : super() {
        this.lightSensor = lightSensor
        this.linearAccelerometer = linearAccelerometer
        this._darkMode = mutableStateOf(false)
        this.startListeningLightSensor()
        this.startListeningLinearAccelerationSensor()

    }
    /*                      This section concerns variables and functions involved in colorSchemeSwitching in response to ambient light                                                   */

    /*We set a mutable value with a private setter -> Enforcing the single resposibility of the VM
            * We expose this mutablestate with a public variable that gets the value of this MutableState as getter*/
    private var _darkMode : MutableState<Boolean>

    var darkMode : Boolean
                get() {
                    return _darkMode.value
                }
                private set(value){
                _darkMode.value = value

                }

    private var _lightBoundColor : MutableState<Color> = mutableStateOf(Color.Gray)

    var lightBoundColor : Color
        get() {
           return _lightBoundColor.value
        }
        private set(value) {
            _lightBoundColor.value = value
        }

    private var _lightReadingString : MutableState<String> = mutableStateOf("")

    var lightReadingString : String
        get() {
            return _lightReadingString.value
        }
        private set(value) {
            _lightReadingString.value = value
        }

    private var _accelerationBoundColor : MutableState<Color> = mutableStateOf(Color.Gray)

    var accelerationBoundColor : Color
        get() {
            return _accelerationBoundColor.value
        }
        private set(value) {
            _accelerationBoundColor.value = value
        }

    private var _accelerationReadingString : MutableState<String> = mutableStateOf("")

    var accelerationReadingString : String
        get() {
            return _accelerationReadingString.value
        }
        private set(value) {
            _accelerationReadingString.value = value
        }


    /**Starts the lightSensorListener and sets the listener to have 200 lux as a cut-off value for dark mode
     *  and invert the darkMode variable on crossing that cut-off
     */
    fun startListeningLightSensor(){

        /*Natural Light Condition	Typical Lux
            Direct Sunlight	        32,000 to 100,000
            Ambient Daylight	    10,000 to 25,000
            Overcast Daylight	        1000
            Sunset & Sunrise	        400
            Moonlight (Full moon)	      1
            Night (No moon)	           < 0.01

            Hospital Theatre	     1,000
            Supermarket, Sports Hall	750
            Factory, Workshop	        750
            Office, Show Rooms, Laboratories, Kitchens	500
            Warehouse Loading Bays	300 to 400
            School Classroom, University Lecture Hall	250
            Lobbies, Public Corridors, Stairwells	200
            Warehouse Aisles	100 to 200
            Homes, Theatres	150
            Family Living Room	50
            */
        /*Validates lightsensor and notifies user of decreased functionality*/
        if(!lightSensor.sensorExists){ lightReadingString = "Sorry, the light-sensor was not available on your device"
            return
        }
        lightSensor.setOnSensorValuesChangedListener {
            /*This sets darkMode variable to true on low light conditions, false on hight ambient light conditions*/
            darkMode = (it[0] <= 200f)

            lightReadingString =String.format(Locale.getDefault(),"Ambient Light (Lux): %.2e", it[0])
                val normalizedValue = ((it[0] - 200f) / (50000f - 50f)).coerceIn(0f, 1f)
                 lightBoundColor = Color(
                    red = (Color.Red.red + (Color.Blue.red - Color.Red.red) * normalizedValue),
                    green = (Color.Red.green + (Color.Blue.green - Color.Red.green) * normalizedValue),
                    blue = (Color.Red.blue + (Color.Blue.blue - Color.Red.blue) * normalizedValue),
                    alpha = 1f
                )

        }

        lightSensor.startListening()

    }

    /**Starts the accelerometer listening and updates
     * the reading string  (accelerationReadingString) as well as the color (based on reference values)
     */
    fun startListeningLinearAccelerationSensor(){

        /*Validates linearaccelerometer and notifies user of decreased functionality*/
        if(!linearAccelerometer.sensorExists){ accelerationReadingString = "Sorry, the accelerometer was not available on your device"
            return
        }


        linearAccelerometer.setOnSensorValuesChangedListener {
            /*Constans based on average linear acceleration of a snail and a discus thrower*/
             val SNAIL_ACCELERATION = 0.0139f
             val DISCUS_THROWER_ACCELERATION = 15f
            accelerationReadingString = String.format(Locale.getDefault(),"Acceleration (m/s²): [X: %.2e, Y: %.2e, Z: %.2e]", it[0], it[1], it[2])

            var resultant : Float = Math.sqrt(Math.pow(it[0].toDouble(),2.0)+Math.pow(it[1].toDouble(),2.0)+Math.pow(it[2].toDouble(),2.0)).toFloat()
            val normalizedValue = ((resultant - SNAIL_ACCELERATION) / (DISCUS_THROWER_ACCELERATION - SNAIL_ACCELERATION)).coerceIn(0f, 1f)
            accelerationBoundColor = Color(
                red = (Color.Red.red + (Color.Blue.red - Color.Red.red) * normalizedValue),
                green = (Color.Red.green + (Color.Blue.green - Color.Red.green) * normalizedValue),
                blue = (Color.Red.blue + (Color.Blue.blue - Color.Red.blue) * normalizedValue),
                alpha = 1f
            )

        }

        linearAccelerometer.startListening()

    }





/*                     This section concerns variables and functions involved in colorSchemeSwitching in response to ambient light                                                   */
    /*                     This section concerns variables that handles validation checking of our sensors and registers the updated userHeight value,
    * -> Prompting us to the next screen and another ViewModel                                                 */


    /**This value shows is equal to true if all necessary sensors were available for the device
     *
     */
    val requiredSensorsExist : Boolean
        get() {
            return (linearAccelerometer.sensorExists && lightSensor.sensorExists)
        }



    /*                     This section concerns variables that handles validation checking of our sensors                                                  */

    override fun onCleared() {

        super.onCleared()
        /*To ensure we don´t ruin battery life*/
        this.lightSensor.stopListening()
        this.linearAccelerometer.stopListening()



    }


}