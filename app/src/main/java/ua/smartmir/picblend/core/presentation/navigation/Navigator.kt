package ua.smartmir.picblend.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ua.smartmir.picblend.core.collectAsEffect

interface Navigator {
    fun getCurrentDestinationId(): Int?
    fun navigateTo(route: String)
    fun popBackStackById(destinationId: Int)
    fun popBackStackByRoute(route: String)
    fun popBackStack()
    fun canNavigationBack(): Boolean
    fun <T> saveDataToBackStackEntry(key: String, data: T)
    @Composable fun <T> getDataFromBackStackEntryFlow(key: String, defaultValue: T, onGetData: (value: T) -> Unit)

    class Base(
        val navHostController: NavHostController
    ) : Navigator {
        override fun getCurrentDestinationId(): Int? = navHostController.currentDestination?.id

        override fun navigateTo(route: String) {
            navHostController.navigate(route) {
                restoreState = true
            }
        }

        override fun popBackStackById(destinationId: Int) {
            navHostController.popBackStack(
                destinationId,
                inclusive = false,
                saveState = false
            )
        }

        override fun popBackStackByRoute(route: String) {
            navHostController.popBackStack(
                route,
                inclusive = false,
                saveState = false
            )
        }

        override fun popBackStack() {
            navHostController.navigateUp()
        }

        override fun canNavigationBack(): Boolean {
            return navHostController.previousBackStackEntry != null
        }

        override fun <T> saveDataToBackStackEntry(key: String, data: T) {
            navHostController.previousBackStackEntry?.savedStateHandle?.set(key, data)
        }

        @Composable
        override fun <T> getDataFromBackStackEntryFlow(
            key: String,
            defaultValue: T,
            onGetData: (T) -> Unit
        ) {
            navHostController.currentBackStackEntry?.savedStateHandle?.getStateFlow(
                key,
                defaultValue
            )?.collectAsEffect { value ->
                value.takeIf {
                    value != defaultValue
                }?.also { result ->
                    onGetData(result)
                    navHostController.currentBackStackEntry?.savedStateHandle?.remove<T>(key)
                }
            }
        }
    }
}

@Composable
fun rememberNavigator(
    navHostController: NavHostController = rememberNavController()
): Navigator {
    return remember {
        Navigator.Base(navHostController)
    }
}