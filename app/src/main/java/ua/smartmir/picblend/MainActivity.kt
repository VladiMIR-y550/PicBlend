package ua.smartmir.picblend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ua.smartmir.picblend.ui.main.MainViewModel
import ua.smartmir.picblend.ui.main.NavigationScreen
import ua.smartmir.picblend.ui.theme.PicBlendTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicBlendTheme {
                NavigationScreen(
                    mainViewModel = hiltViewModel<MainViewModel>(),
                    onExitApp = { finish() },
                )
            }
        }
    }
}