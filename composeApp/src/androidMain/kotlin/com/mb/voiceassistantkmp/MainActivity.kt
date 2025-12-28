package com.mb.voiceassistantkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mb.voiceassistantkmp.presentation.navigation.AppNavHost
import com.mb.voiceassistantkmp.presentation.utils.LocalVoiceHandler
import com.mb.voiceassistantkmp.presentation.utils.AndroidVoiceHandler
import com.mb.voiceassistantkmp.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            AndroidVoiceHandler { handler ->
                CompositionLocalProvider(LocalVoiceHandler provides handler) {
                    AppTheme {
                        Surface(color = MaterialTheme.colorScheme.background) {
                            AppNavHost()
                        }
                    }
                }
            }
        }
    }
}
