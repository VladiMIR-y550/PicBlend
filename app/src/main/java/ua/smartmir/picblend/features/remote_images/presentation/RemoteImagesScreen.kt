package ua.smartmir.picblend.features.remote_images.presentation

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ua.smartmir.picblend.core.base.RemoteImagesEffect
import ua.smartmir.picblend.core.toast
import ua.smartmir.picblend.features.remote_images.presentation.model.PhotoUi

@Composable
fun RemoteImagesScreen(
    modifier: Modifier = Modifier,
    viewModel: RemoteImagesViewModel,
    onImagePicked: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RemoteImagesEffect.Loading -> isLoading = effect.isLoading
                is RemoteImagesEffect.ShowToast -> context.toast(effect.message)
                is RemoteImagesEffect.CachedImage -> onImagePicked(effect.uri)
            }
        }
    }

    RemoteImagesUI(
        modifier = modifier,
        isLoading = isLoading,
        images = uiState.images,
        onScrollToEnd = viewModel::getMoreImages,
        onImageSelected = viewModel::cacheImage
    )
}

@Composable
fun RemoteImagesUI(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    images: List<PhotoUi>,
    onScrollToEnd:() -> Unit,
    onImageSelected: (PhotoUi) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { index ->
                index?.let {
                    if (index >= images.size.div(3)) onScrollToEnd.invoke()
                }
            }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) LoadingIndicator()
        LazyColumn(state = listState, modifier = modifier.fillMaxSize()) {
            items(items = images, key = { itemId -> itemId.id }) { imageUi ->
                ImageItem(
                    photo = imageUi,
                    onClick = onImageSelected
                )
            }
        }
    }
}

@Composable
fun ImageItem(photo: PhotoUi, onClick: (PhotoUi) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(photo)
            }
            .padding(8.dp)
    ) {
        AsyncImage(
            model = photo.urls.small,
            contentDescription = photo.description,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}