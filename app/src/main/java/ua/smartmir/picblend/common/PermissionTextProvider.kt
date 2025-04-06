package ua.smartmir.picblend.common

import androidx.annotation.StringRes
import ua.smartmir.picblend.R

interface PermissionTextProvider {
    fun getDescriptionId(isPermanentlyDeclined: Boolean): Int

    abstract class Abstract(
        @StringRes private val appSettingsStringIdRes: Int,
        @StringRes private val accessRequiredStringIdRes: Int
    ) : PermissionTextProvider {
        override fun getDescriptionId(isPermanentlyDeclined: Boolean): Int {
            return if (isPermanentlyDeclined)
                appSettingsStringIdRes
            else accessRequiredStringIdRes
        }
    }

    object Camera : Abstract(
        R.string.system_dialog_text_go_to_camera_app_settings,
        R.string.system_dialog_text_camera_access_required
    )
}