package ua.smartmir.picblend.ui.main

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.smartmir.picblend.navigation.Navigator
import ua.smartmir.picblend.navigation.PicBlendNavGraph
import ua.smartmir.picblend.navigation.Screens
import ua.smartmir.picblend.navigation.rememberNavigator
import ua.smartmir.picblend.ui.common.PicBlendAppBar
import ua.smartmir.picblend.ui.home.HomeScreen
import ua.smartmir.picblend.ui.home.HomeViewModel

@Composable
fun NavigationScreen(
    mainViewModel: MainViewModel,
    onExitApp: () -> Unit,
    modifier: Modifier = Modifier,
    navigator: Navigator = rememberNavigator(),
) {
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        mainViewModel.onBackPressure(navigator)
    }

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            PicBlendAppBar(
                modifier = modifier,
                titleId = uiState.title,
                canNavigationBack = navigator.canNavigationBack(),
                navigateUp = { mainViewModel.onBackPressure(navigator) },
                barIcons = uiState.barIcons
            )
        }
    ) { innerPadding ->
        PicBlendNavGraph(
            modifier = modifier.padding(innerPadding),
            navHostController = (navigator as Navigator.Base).navHostController,
            homeContent = {
                mainViewModel.updateCurrentScreen(Screens.Home)
                HomeScreen(
                    modifier = modifier,
                    viewModel = hiltViewModel<HomeViewModel>(),
                    onCameraClick = { Log.d("TAG_MY_TEST_CAMERA", "NavigationScreen: onCameraClick")}, //todo
                    onExitClick = onExitApp,
                    updateBarIconsState = mainViewModel::updateBarIcons
                )
            }
        )
    }
}