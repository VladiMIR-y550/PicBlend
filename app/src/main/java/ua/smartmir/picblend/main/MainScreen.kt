package ua.smartmir.picblend.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.smartmir.picblend.common.PicBlendAppBar
import ua.smartmir.picblend.features.camera.presentation.CameraScreen
import ua.smartmir.picblend.features.camera.presentation.CameraViewModel
import ua.smartmir.picblend.features.home.HomeScreen
import ua.smartmir.picblend.features.home.HomeViewModel
import ua.smartmir.picblend.navigation.Navigator
import ua.smartmir.picblend.navigation.PicBlendNavGraph
import ua.smartmir.picblend.navigation.Screens
import ua.smartmir.picblend.navigation.rememberNavigator

@Composable
fun MainScreen(
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
                    onCameraClick = {
                        navigator.navigateTo(Screens.Camera.getRouteWithParams())
                    },
                    onExitClick = onExitApp,
                    updateBarIconsState = mainViewModel::updateBarIcons
                )
            },
            cameraContent = {
                mainViewModel.updateCurrentScreen(Screens.Camera)
                CameraScreen(
                    modifier = modifier,
                    viewModel = hiltViewModel<CameraViewModel>()
                )
            }
        )
    }
}