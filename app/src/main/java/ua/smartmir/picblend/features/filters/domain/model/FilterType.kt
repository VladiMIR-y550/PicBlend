package ua.smartmir.picblend.features.filters.domain.model

import android.graphics.ColorMatrix

sealed interface FilterType {
    fun colorMatrix(): ColorMatrix

    companion object {
        val filters = listOf(
            None,
            GRAYSCALE,
            SEPIA,
            INVERT,
            SATURATE,
            DESATURATE,
            BRIGHTNESS,
            CONTRAST,
            TINT_BLUE,
            TINT_RED
        )
    }

    abstract class Abstract() : FilterType {
        protected val matrix: ColorMatrix = ColorMatrix()
    }

    object None : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return matrix
        }
    }

    object GRAYSCALE : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return ColorMatrix().apply { setSaturation(0f) }
        }
    }

    object SEPIA : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return matrix.apply {
                setSaturation(0f)
                set(
                    ColorMatrix(
                        floatArrayOf(
                            0.393f, 0.769f, 0.189f, 0f, 0f,
                            0.349f, 0.686f, 0.168f, 0f, 0f,
                            0.272f, 0.534f, 0.131f, 0f, 0f,
                            0f, 0f, 0f, 1f, 0f
                        )
                    )
                )
            }
        }
    }

    object INVERT : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return matrix.apply {
                set(
                    floatArrayOf(
                        -1f, 0f, 0f, 0f, 250f,
                        0f, -1f, 0f, 0f, 250f,
                        0f, 0f, -1f, 0f, 250f,
                        0f, 0f, 0f, 1f, 0f
                    )
                )
            }
        }
    }

    object SATURATE : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return ColorMatrix().apply { setSaturation(1.5f) }
        }
    }

    object DESATURATE : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return ColorMatrix().apply { setSaturation(0.3f) }
        }
    }

    object BRIGHTNESS : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            val brightness = 50f
            return ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, brightness,
                    0f, 1f, 0f, 0f, brightness,
                    0f, 0f, 1f, 0f, brightness,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
    }

    object CONTRAST : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            val contrast = 1.5f
            val translate = (-0.5f * contrast + 0.5f) * 255f
            return ColorMatrix(
                floatArrayOf(
                    contrast, 0f, 0f, 0f, translate,
                    0f, contrast, 0f, 0f, translate,
                    0f, 0f, contrast, 0f, translate,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
    }

    object TINT_BLUE : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return ColorMatrix(
                floatArrayOf(
                    0.9f, 0f, 0f, 0f, 0f,
                    0f, 0.9f, 0f, 0f, 0f,
                    0f, 0f, 1.2f, 0f, 30f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
    }

    object TINT_RED : Abstract() {
        override fun colorMatrix(): ColorMatrix {
            return ColorMatrix(
                floatArrayOf(
                    1.2f, 0f, 0f, 0f, 30f,
                    0f, 0.9f, 0f, 0f, 0f,
                    0f, 0f, 0.9f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
    }
}