package com.example.maisonflowers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.maisonflowers.ui.navigation.NavGraph
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme

class MaisonFlowersApp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaisonFlowersTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
