package ua.smartmir.picblend.common.filters.domain.model

import android.graphics.ColorMatrix

interface FilterType {
    fun colorMatrix(): ColorMatrix

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
}