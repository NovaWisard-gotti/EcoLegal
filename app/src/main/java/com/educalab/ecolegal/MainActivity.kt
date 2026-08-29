package com.educalab.ecolegal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.educalab.ecolegal.ui.navigation.EcoLegalNavGraph
import com.educalab.ecolegal.ui.theme.EcoLegalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoLegalTheme {
                EcoLegalNavGraph()
            }
        }
    }
}
