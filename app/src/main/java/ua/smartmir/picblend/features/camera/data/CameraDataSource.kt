package ua.smartmir.picblend.features.camera.data

import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.CameraSelector.LENS_FACING_UNKNOWN
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.smartmir.picblend.R
import ua.smartmir.picblend.features.camera.data.model.CameraSettingsEntity
import ua.smartmir.picblend.features.camera.presentation.Focal
import ua.smartmir.picblend.features.camera.presentation.mapTo
import java.util.concurrent.Executor
import javax.inject.Inject

interface CameraDataSource {
    val originalBitmapFlow: Flow<Bitmap?>
    val camerasFlow: Flow<List<CameraSettingsEntity>>
    fun switchCamera(cameraId: String)
    fun launchCamera(): LifecycleCameraController
    fun switchFrontBackCamera()

    @ExperimentalCamera2Interop
    class CameraXDataSource @Inject constructor(
        private val executor: Executor,
        private val cameraController: LifecycleCameraController,
        private val cameraProvider: ProcessCameraProvider,
        private val cameraManager: CameraManager,
    ) : CameraDataSource {
        private var cameraSelectorId: Int = LENS_FACING_BACK
        private val config = Bitmap.Config.ARGB_8888
        private val _originalBitmapFlow = MutableStateFlow<Bitmap?>(null)
        override val originalBitmapFlow: Flow<Bitmap?> = _originalBitmapFlow.asStateFlow()
        private val _camerasFlow = MutableStateFlow<List<CameraSettingsEntity>>(emptyList())
        override val camerasFlow: StateFlow<List<CameraSettingsEntity>> = _camerasFlow.asStateFlow()
        private val allCameras: List<CameraSettingsEntity> = initAllCameras()

        init {
            setupCameras { filteredCameras ->
                updateCamerasFlow(filteredCameras)
            }
        }

        override fun launchCamera(): LifecycleCameraController {
            cameraController.clearImageAnalysisAnalyzer()
            cameraController.setImageAnalysisAnalyzer(executor) { image ->
                _originalBitmapFlow.update {
                    image.toBitmap().copy(config, true)
                        .rotateBitmap(image.imageInfo.rotationDegrees)
                }
                image.close()
            }
            return cameraController
        }

        override fun switchFrontBackCamera() {
            cameraSelectorId = if (cameraSelectorId == LENS_FACING_BACK) {
                LENS_FACING_FRONT
            } else {
                LENS_FACING_BACK
            }
            setupCameras()
        }

        override fun switchCamera(cameraId: String) {
            allCameras.find { it.cameraId == cameraId }?.cameraSelector?.let { selector ->
                cameraController.cameraSelector = selector
                updateSelectedCamera(cameraId)
            }
        }

        private fun defaultCameraId(cameras: List<CameraSettingsEntity>): String? {
            return cameras.find { it.focal is Focal.Standard }?.cameraId
                ?: cameras.firstOrNull()?.cameraId
        }

        private fun setupCameras(onSwitchedCamera: (List<CameraSettingsEntity>) -> Unit = {}) {
            val filteredCameras = allCameras.filterByFacing(cameraSelectorId)
            val defaultCameraId = defaultCameraId(filteredCameras)

            defaultCameraId?.let {
                switchCamera(it)
            } ?: run {
                onSwitchedCamera(filteredCameras)
            }
        }

        private fun updateSelectedCamera(cameraId: String) {
            val updated = allCameras.filterByFacing(cameraSelectorId).map {
                it.copy(isSelected = it.cameraId == cameraId)
            }
            updateCamerasFlow(updated)
        }

        private fun updateCamerasFlow(cameras: List<CameraSettingsEntity>) {
            _camerasFlow.value = cameras
        }

        private fun initAllCameras(): List<CameraSettingsEntity> {
            val cameras = mutableListOf<CameraSettingsEntity>()
            cameraProvider.availableCameraInfos.forEach { cameraInfo ->
                val cameraId = Camera2CameraInfo.from(cameraInfo).cameraId
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val (lensFacingLabel, lensFacingId) = characteristics.facingLabelWithId()
                val focal = characteristics.cameraFocal()
                val cameraSelector = CameraSelector.Builder()
                    .addCameraFilter { cameraInfos ->
                        cameraInfos.filter {
                            Camera2CameraInfo.from(it).cameraId == cameraId
                        }
                    }
                    .build()
                cameras.add(
                    CameraSettingsEntity(
                        cameraId = cameraId,
                        lensFacingLabel = lensFacingLabel,
                        lensFacingId = lensFacingId,
                        cameraSelector = cameraSelector,
                        focal = focal.mapTo(),
                        isSelected = false
                    )
                )
            }
            return cameras
        }
    }
}

private fun Bitmap.rotateBitmap(degrees: Int): Bitmap {
    if (degrees == 0) return this
    val matrix = Matrix().apply { postRotate(degrees.toFloat()) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

private fun CameraCharacteristics.facingLabelWithId(): Pair<Int, Int> {
    return when (get(CameraCharacteristics.LENS_FACING)) {
        CameraCharacteristics.LENS_FACING_BACK -> Pair(R.string.rear_camera, LENS_FACING_BACK)
        CameraCharacteristics.LENS_FACING_FRONT -> Pair(
            R.string.camera_facing_front_camera,
            LENS_FACING_FRONT
        )

        else -> Pair(R.string.camera_facing_unknown_camera, LENS_FACING_UNKNOWN)
    }
}

private fun CameraCharacteristics.cameraFocal(): Float? {
    val focalLengths = get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
    return focalLengths?.firstOrNull()
}

private fun List<CameraSettingsEntity>.filterByFacing(cameraSelectorId: Int): List<CameraSettingsEntity> =
    filter {
        it.lensFacingId == cameraSelectorId
    }