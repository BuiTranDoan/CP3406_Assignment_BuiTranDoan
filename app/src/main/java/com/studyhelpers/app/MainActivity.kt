package com.studyhelpers.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.studyhelpers.app.ui.navigation.AppNavGraph
import com.studyhelpers.app.ui.theme.StudyHelpersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyHelpersTheme {
                StudyHelpersApp()
            }
        }
    }
}