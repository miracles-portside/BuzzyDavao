package com.davao.buzzy.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davao.buzzy.ui.theme.*
import com.davao.buzzy.ui.theme.rememberTapHaptics

data class NavItem(val key: String, val label: String)

@Composable
fun FloatingNavPill(
    items: List<NavItem>,
    activeKey: String,
    onPick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val idx = items.indexOfFirst { it.key == activeKey }.coerceAtLeast(0)
    val tap = rememberTapHaptics()

    Box(
        modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(999.dp), clip = false)
            .clip(RoundedCornerShape(999.dp))
            .background(PillTrack.copy(alpha = 0.94f))
            .padding(4.dp)
    ) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val itemWidth = maxWidth / items.size
            val indicatorOffset by animateDpAsState(
                targetValue = itemWidth * idx,
                animationSpec = Motion.BouncyDp,
                label = "navPillOffset"
            )

            // Sliding white indicator
            Box(
                Modifier
                    .offset(x = indicatorOffset)
                    .width(itemWidth)
                    .fillMaxHeight()
                    .shadow(elevation = 3.dp, shape = RoundedCornerShape(999.dp), clip = false)
                    .clip(RoundedCornerShape(999.dp))
                    .background(SurfaceCard)
            )

            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                items.forEachIndexed { i, item ->
                    val on = i == idx
                    val interaction = remember { MutableInteractionSource() }
                    Box(
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(999.dp))
                            .clickable(
                                interactionSource = interaction,
                                indication = null
                            ) {
                                tap()
                                onPick(item.key)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            item.label,
                            color = if (on) Ink else Muted,
                            fontSize = 12.5.sp,
                            fontWeight = if (on) FontWeight.SemiBold else FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
