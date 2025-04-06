package ua.smartmir.picblend.navigation

interface Screens {
    companion object {
        private const val HOME_SCREEN = "HOME_SCREEN"
        private const val CAMERA_SCREEN = "CAMERA_SCREEN"
        private const val GALLERY_SCREEN = "GALLERY_SCREEN"

        const val KEY_RETURNED_IMAGE = "KEY_GALLERY_IMAGE"
    }

    fun getRouteWithParams(vararg values: String): String

    abstract class BaseScreens(
        val route: String,
        private val params: List<String>,
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

    object Home : BaseScreens(HOME_SCREEN, emptyList())
    object Camera : BaseScreens(CAMERA_SCREEN, emptyList())
    object Gallery : BaseScreens(GALLERY_SCREEN, emptyList())
}