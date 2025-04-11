package ua.smartmir.picblend.features.camera.presentation

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import ua.smartmir.picblend.core.presentation.StableBitmap

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController?,
    processedBitmap: StableBitmap?
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = modifier) {
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    this.controller = controller
                    controller?.bindToLifecycle(lifecycleOwner)
                    scaleType = PreviewView.ScaleType.FIT_CENTER
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        processedBitmap?.let { stableBitmap ->
            Image(
                bitmap = stableBitmap.bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
}