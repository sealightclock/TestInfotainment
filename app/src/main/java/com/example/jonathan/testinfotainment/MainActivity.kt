package com.example.jonathan.testinfotainment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.jonathan.testinfotainment.ui.MainScreen
import com.example.jonathan.testinfotainment.ui.theme.TestInfotainmentTheme

private const val TAG = "TIF: MainActivity"

/**
 * The main entry point activity for the application.
 * Sets up the Compose UI and enables edge-to-edge display.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")

        super.onCreate(savedInstanceState)

        // Enable full-screen edge-to-edge drawing.
        enableEdgeToEdge()

        setContent {
            TestInfotainmentTheme {
                MainScreen()
            }
        }
    }
}

