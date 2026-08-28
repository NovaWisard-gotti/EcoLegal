package com.educalab.verdelegal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.educalab.verdelegal.ui.navigation.VerdeLegalNavGraph
import com.educalab.verdelegal.ui.theme.VerdeLegalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VerdeLegalTheme {
                VerdeLegalNavGraph()
            }
        }
    }
}
