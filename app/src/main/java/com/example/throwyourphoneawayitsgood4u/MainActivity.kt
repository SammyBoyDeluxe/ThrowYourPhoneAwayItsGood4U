package com.example.throwyourphoneawayitsgood4u

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.throwyourphoneawayitsgood4u.ViewModel.IndexViewModel
import com.example.throwyourphoneawayitsgood4u.sensors.LightSensor
import com.example.throwyourphoneawayitsgood4u.sensors.LinearAccelerationSensor
import com.example.throwyourphoneawayitsgood4u.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val lightSensor : LightSensor = LightSensor(this.applicationContext)
        val linearAccelerationSensor : LinearAccelerationSensor = LinearAccelerationSensor(this.applicationContext)
        val indexViewModel : IndexViewModel = IndexViewModel(lightSensor,linearAccelerationSensor)


        super.onCreate(savedInstanceState)
        /*On create we want to initialize the ViewModel*/
        enableEdgeToEdge()
        setContent {
            MyAppTheme(darkTheme = indexViewModel.darkMode) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorTable(
                        lightReadingString = indexViewModel.lightReadingString,
                        accelerationReadingString = indexViewModel.accelerationReadingString,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        lightBoundColor = indexViewModel.lightBoundColor,
                        accelerationBoundColor = indexViewModel.accelerationBoundColor
                    )                }
            }
        }
    }
}

/**Draws up a SensorTable, which contains the readings from the sensors
 * as well as two paired circles that change color from red to blue depending on reference values for
 * light and acceleration respectively.
 * The value strings as well as the colors are obtained from the ViewModel
 */
@Composable
fun SensorTable(lightReadingString: String, accelerationReadingString : String ,modifier: Modifier = Modifier,lightBoundColor : androidx.compose.ui.graphics.Color, accelerationBoundColor: androidx.compose.ui.graphics.Color) {
    Column(modifier = Modifier.fillMaxWidth(0.8f)) {
        /*This row contains the two headers*/
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Text(
                    text = lightReadingString,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium

                )
            Text(
                text = accelerationReadingString,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium

            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
        ColorBoxesComposable(lightBoundColor, accelerationBoundColor, modifier)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

/**This composable creates two color boxes with color input from the application
 * (the ViewModel)
 *
 */
@Composable
fun ColorBoxesComposable(lightBoundColor : androidx.compose.ui.graphics.Color, accelerationBoundColor: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = lightBoundColor, shape = CircleShape)
    )
    Spacer(Modifier.width(16.dp)) // Space between the boxes
    // Acceleration Bound Color Box
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = accelerationBoundColor, shape = CircleShape)
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyAppTheme(darkTheme = true) {
        Greeting("Android")
    }
}