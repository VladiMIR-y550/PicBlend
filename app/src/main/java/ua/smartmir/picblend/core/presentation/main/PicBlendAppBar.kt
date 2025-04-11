package ua.smartmir.picblend.core.presentation.main

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.smartmir.picblend.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PicBlendAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleId: Int,
    canNavigationBack: Boolean,
    showToolbar: Boolean,
    navigateUp: () -> Unit,
    barIcons: List<BarIconState> = emptyList()
) {
    if (showToolbar) {
        TopAppBar(
            title = {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    text = stringResource(titleId),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = modifier,
            navigationIcon = {
                if (canNavigationBack) {
                    IconButton(
                        onClick = navigateUp
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            },
            actions = {
                if (barIcons.isNotEmpty()) {
                    barIcons.forEach {
                        IconButton(onClick = it.onClick) {
                            Icon(
                                imageVector = it.imageVector,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun PicBlendAppBarPreview() {
    PicBlendAppBar(
        titleId = R.string.app_name,
        canNavigationBack = true,
        showToolbar = true,
        navigateUp = {},
        barIcons = listOf(
            BarIconState(
                imageVector = Icons.Default.Save,
                onClick = {}
            ),
            BarIconState(
                imageVector = Icons.Default.Share,
                onClick = {}
            )
        )
    )
}