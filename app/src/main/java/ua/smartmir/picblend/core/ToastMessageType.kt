package ua.smartmir.picblend.core

import androidx.annotation.StringRes

sealed class ToastMessageType {
    class IntValue(@StringRes val value: Int) : ToastMessageType()

    class StringValue(val value: String) : ToastMessageType()

    companion object {
        fun getType(value: Any): ToastMessageType {
            return when (value) {
                is Int -> IntValue(value)
                is String -> StringValue(value)
                else -> StringValue("")
            }
        }
    }
}