package ua.smartmir.picblend.features.remote_images.presentation

import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.distinctUntilChanged
import ua.smartmir.picblend.R
import ua.smartmir.picblend.core.base.CollectEffects
import ua.smartmir.picblend.core.base.RemoteImagesEffect.CachedImage
import ua.smartmir.picblend.core.base.RemoteImagesEffect.Loading
import ua.smartmir.picblend.core.base.RemoteImagesEffect.ShowToast
import ua.smartmir.picblend.core.toast
import ua.smartmir.picblend.features.remote_images.presentation.RemoteImagesViewModel.Companion.LOAD_MORE_THRESHOLD_INDEX
import ua.smartmir.picblend.features.remote_images.presentation.RemoteImagesViewModel.Companion.PLACEHOLDER_LIST_SIZE
import ua.smartmir.picblend.features.remote_images.presentation.model.PhotoUi
import kotlin.random.Random


@Composable
fun RemoteImagesScreen(
    modifier: Modifier = Modifier,
    viewModel: RemoteImagesViewModel,
    onImagePicked: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val networkDialogIsVisible by viewModel.networkDialogIsVisible.collectAsStateWithLifecycle()

    var isLoading by remember { mutableStateOf(false) }

    CollectEffects(viewModel.effect, lifecycleOwner) { effect ->
        when (effect) {
            is Loading -> isLoading = effect.isLoading
            is ShowToast -> context.toast(effect.message)
            is CachedImage -> onImagePicked(effect.uri)
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        RemoteImagesUI(
            modifier = modifier.align(Alignment.Center),
            isLoading = isLoading,
            images = uiState.images,
            onScrollToEnd = viewModel::getMoreImages,
            onImageSelected = viewModel::cacheImage
        )
        NetworkInfo(
            modifier = modifier.align(Alignment.TopCenter),
            showDialog = networkDialogIsVisible
        )
    }
}

@Composable
fun NetworkInfo(
    modifier: Modifier = Modifier,
    showDialog: Boolean
) {
    if (showDialog) {
        Box(
            modifier = modifier
                .wrapContentHeight()
                .shadow(elevation = 8.dp)
                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onError)
                    .padding(8.dp),
                text = stringResource(R.string.no_internet),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemoteImagesUI(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    images: List<PhotoUi>,
    onScrollToEnd: () -> Unit,
    onImageSelected: (PhotoUi) -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { index ->
                val totalItemsCount = gridState.layoutInfo.totalItemsCount
                if (index != null && totalItemsCount > 0 && index >= totalItemsCount - LOAD_MORE_THRESHOLD_INDEX) {
                    onScrollToEnd()
                }
            }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (images.isEmpty()) {
            SkeletonList(modifier = modifier, listSize = PLACEHOLDER_LIST_SIZE)
        } else {
            LazyVerticalStaggeredGrid(
                state = gridState,
                columns = StaggeredGridCells.Fixed(2),
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(images, key = { it.id }) { imageUi ->
                    RemoteImage(
                        modifier = Modifier,
                        photo = imageUi,
                        onClick = onImageSelected
                    )
                }

                if (isLoading) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        LoadingIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun RemoteImage(
    modifier: Modifier = Modifier,
    photo: PhotoUi,
    onClick: (PhotoUi) -> Unit
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photo.urls.small)
            .crossfade(true)
            .build(),
        contentDescription = photo.description,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(photo.ratio)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(photo) },
        loading = {
            SkeletonImageItem(modifier.aspectRatio(photo.ratio))
        },
        error = {
            SkeletonImageItem(modifier.aspectRatio(photo.ratio))
        }
    )
}

@Composable
fun SkeletonImageItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .shimmerEffect()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}


@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
    return this.graphicsLayer { this.alpha = alpha }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SkeletonList(
    modifier: Modifier = Modifier,
    listSize: Int
) {
    val random = remember { Random(System.currentTimeMillis()) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(listSize) {
            val randomRatio = remember { random.nextFloat().coerceIn(0.75f, 1.5f) }
            SkeletonImageItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(randomRatio)
            )
        }

        item(span = StaggeredGridItemSpan.FullLine) {
            LoadingIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}
