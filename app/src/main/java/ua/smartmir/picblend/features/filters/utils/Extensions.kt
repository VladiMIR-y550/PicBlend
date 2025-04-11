package ua.smartmir.picblend.features.filters.utils

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import ua.smartmir.picblend.features.filters.domain.model.Filter

internal suspend fun <T> processFilteredBitmapSingle(
    bitmap: Bitmap,
    filters: List<Filter>,
    transform: suspend (Bitmap, List<Filter>) -> T
): T {
    return withContext(Dispatchers.IO) { transform(bitmap, filters) }
}

internal fun <T> processFilteredBitmap(
    bitmapFlow: Flow<Bitmap?>,
    filtersFlow: Flow<List<Filter>>,
    transform: suspend (Bitmap, List<Filter>) -> T
): Flow<T> {
    return combine(bitmapFlow.filterNotNull(), filtersFlow) { bitmap, filters ->
        withContext(Dispatchers.Default) {
            transform(bitmap, filters)
        }
    }
}