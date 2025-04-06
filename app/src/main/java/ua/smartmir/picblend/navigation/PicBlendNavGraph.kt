package ua.smartmir.picblend.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Composable
fun PicBlendNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    homeContent: @Composable () -> Unit,
    cameraContent: @Composable () -> Unit,
    galleryContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Home.route,
        modifier = modifier
    ){
        composable(Screens.Home.route) {
            homeContent.invoke()
        }

        composable(Screens.Camera.route) {
            cameraContent.invoke()
        }

        composable(Screens.Gallery.route) {
            galleryContent.invoke()
        }
    }
}