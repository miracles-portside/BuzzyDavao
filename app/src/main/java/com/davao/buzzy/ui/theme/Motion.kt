package com.davao.buzzy.ui.theme

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.unit.Dp

object Motion {
    /** iOS-like bouncy spring for floats and offsets. */
    val Bouncy = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    /** Same spring, typed for Dp (used with animateDpAsState). */
    val BouncyDp = spring<Dp>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    /** Springy entry — a bit slower, softer. */
    val Soft = spring<Float>(
        dampingRatio = 0.7f,
        stiffness = 400f
    )

    /** Snappy press feedback. */
    val Press = spring<Float>(
        dampingRatio = 0.7f,
        stiffness = 500f
    )
}
