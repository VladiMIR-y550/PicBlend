package ua.smartmir.picblend.core.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun CollectEffects(
    effectFlow: SharedFlow<UiEffect>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEffect: (UiEffect) -> Unit
) {
    LaunchedEffect(effectFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            effectFlow.collect { effect ->
                onEffect(effect)
            }
        }
    }
}
