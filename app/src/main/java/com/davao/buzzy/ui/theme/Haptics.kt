package com.davao.buzzy.ui.theme

import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView

object Haptics {
    @Composable
    fun tap() = LocalView.current.also {
        it.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}

@Composable
fun rememberTapHaptics(): () -> Unit {
    val view = LocalView.current
    return {
        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}
