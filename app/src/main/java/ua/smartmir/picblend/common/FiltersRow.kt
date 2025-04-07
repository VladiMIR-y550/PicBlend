package ua.smartmir.picblend.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import ua.smartmir.picblend.common.filters.domain.model.FilterType
import ua.smartmir.picblend.features.camera.presentation.FilterStateEntity

@Composable
fun FiltersRow(
    modifier: Modifier = Modifier,
    filters: List<FilterStateEntity>,
    onFilterSelected: (FilterType) -> Unit,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(count = filters.size) { index ->
            val currentFilter = filters[index]

            Spacer(modifier.width(8.dp))
            Card(
                modifier = modifier
                    .size(width = 80.dp, height = 80.dp)
                    .border(
                        width = 1.dp,
                        color = if (currentFilter.isSelected) Color.Green else Color.Unspecified,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                onClick = { onFilterSelected(currentFilter.filterType) }
            ) {
                Column {
                    currentFilter.filteredBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentScale = ContentScale.Crop,
                            contentDescription = currentFilter.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(text = currentFilter.name)
                }
            }
        }
    }
}