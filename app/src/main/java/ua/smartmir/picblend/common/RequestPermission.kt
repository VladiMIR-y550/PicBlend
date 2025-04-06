package ua.smartmir.picblend.common

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import ua.smartmir.picblend.R
import ua.smartmir.picblend.core.cameraPermissionsToRequest
import ua.smartmir.picblend.core.findActivity
import ua.smartmir.picblend.core.openAppSettings

@Composable
fun CameraPermissionRequest(
//    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BaseMultiplePermissions(
        permissionList = cameraPermissionsToRequest,
        permissionTextProvider = PermissionTextProvider.Camera,
        onDismiss = onDismiss
    )
}

@Composable
fun BaseMultiplePermissions(
    permissionList: Array<String>,
    permissionTextProvider: PermissionTextProvider,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val visiblePermissionDialogQueue = remember { mutableStateListOf<String>() }
    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { onResultPerms ->
            permissionList.forEach { permission ->
                when {
                    onResultPerms[permission] == false && !visiblePermissionDialogQueue.contains(
                        permission
                    ) -> visiblePermissionDialogQueue.add(permission)
                }
            }
        }
    )

    LaunchedEffect(true) {
        multiplePermissionResultLauncher.launch(permissionList)
    }

    visiblePermissionDialogQueue
        .reversed()
        .forEach { permission ->
            if (visiblePermissionDialogQueue.isNotEmpty()) {
                PermissionDialog(
                    permissionTextProvider = permissionTextProvider,
                    isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                        context.findActivity(), permission
                    ),
                    onConfirm = {
                        onDismiss.invoke()
                        visiblePermissionDialogQueue.clear()
                    },
                    onDismiss = {
                        onDismiss.invoke()
                        visiblePermissionDialogQueue.clear()
                    },
                    onGoToAppSettingsClick = {
                        onDismiss.invoke()
                        context.findActivity().openAppSettings()
                        visiblePermissionDialogQueue.clear()
                    }
                )
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        content = {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id = R.string.system_dialog_title_need_access),
                    )
                    Text(
                        modifier = modifier.padding(vertical = 16.dp),
                        text = stringResource(
                            permissionTextProvider.getDescriptionId(
                                isPermanentlyDeclined
                            )
                        )
                    )
                    Column(
                        modifier = modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider()
                        TextButton(
                            modifier = modifier.fillMaxWidth(),
                            onClick = {
                                if (isPermanentlyDeclined) {
                                    onGoToAppSettingsClick()
                                } else {
                                    onConfirm()
                                }
                            },
                            content = {
                                Text(
                                    text = stringResource(
                                        id = if (isPermanentlyDeclined) {
                                            R.string.system_dialog_title_go_to_settings
                                        } else {
                                            R.string.ok
                                        }
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun PermissionDialogPreview() {
    PermissionDialog(
        permissionTextProvider = PermissionTextProvider.Camera,
        isPermanentlyDeclined = true,
        onDismiss = {},
        onConfirm = {},
        onGoToAppSettingsClick = {}
    )
}