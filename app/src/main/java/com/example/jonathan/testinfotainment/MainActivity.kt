package com.example.jonathan.testinfotainment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.jonathan.testinfotainment.ui.MainScreen
import com.example.jonathan.testinfotainment.ui.theme.TestInfotainmentTheme

private const val TAG = "TIF: MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TestInfotainmentTheme {
                MainScreen()
            }
        }
    }
}

