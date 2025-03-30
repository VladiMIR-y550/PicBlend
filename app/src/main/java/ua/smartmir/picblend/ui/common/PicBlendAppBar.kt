package ua.smartmir.picblend.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    navigateUp: () -> Unit,
    barIcons: List<BarIconState> = emptyList()
) {
    TopAppBar(
        title = {
            Text(
                modifier = modifier.fillMaxWidth()
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
                            painter = painterResource(it.imageVectorId),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PicBlendAppBarPreview() {
    PicBlendAppBar(
        titleId = R.string.home,
        canNavigationBack = true,
        navigateUp = {},
        barIcons = listOf(
            BarIconState(
                imageVectorId = R.drawable.share_line,
                onClick = {}
            )
        )
    )
}