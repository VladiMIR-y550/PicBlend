package ua.smartmir.picblend.navigation

import androidx.annotation.StringRes
import ua.smartmir.picblend.R

interface Screens {
    companion object {
        private const val HOME_SCREEN = "HOME_SCREEN"
        private const val CAMERA_SCREEN = "CAMERA_SCREEN"
    }

    fun getRouteWithParams(vararg values: String): String

    abstract class BaseScreens(
        val route: String,
        private val params: List<String>,
        @get:StringRes val titleStringId: Int
    ) : Screens {
        override fun getRouteWithParams(vararg values: String): String {
            var parameters = ""
            params.forEachIndexed { index, s ->
                val separator = if (index == 0) "" else "?"
                val value =
                    if (values.getOrNull(index).isNullOrEmpty()) {
                        "{$s}"
                    } else {
                        values[index]
                    }
                parameters += "$separator$s=$value"
            }
            return route + parameters
        }
    }

    object Home : BaseScreens(HOME_SCREEN, emptyList(), R.string.home)
    object Camera : BaseScreens(CAMERA_SCREEN, emptyList(), R.string.camera)
}