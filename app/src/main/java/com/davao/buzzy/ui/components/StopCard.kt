package com.davao.buzzy.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davao.buzzy.data.Stop
import com.davao.buzzy.ui.theme.Dimens
import com.davao.buzzy.ui.theme.Ink
import com.davao.buzzy.ui.theme.MintDark
import com.davao.buzzy.ui.theme.MintSoft
import com.davao.buzzy.ui.theme.Motion
import com.davao.buzzy.ui.theme.Muted
import com.davao.buzzy.ui.theme.SurfaceCard
import com.davao.buzzy.ui.theme.rememberTapHaptics

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun StopCard(
    stop: Stop,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = Motion.Press,
        label = "stopCardScale"
    )
    val tap = rememberTapHaptics()

    // Distinct base route ids (strip AM/PM, dedupe by [R###] code, keep cleanest name)
    val routeIds = stop.routes
        .map { it.routeName.removeSuffix("AM").removeSuffix("PM") }
        .groupBy { name ->
            val end = name.indexOf(']')
            if (end >= 0) name.substring(0, end + 1) else name
        }
        .values
        .map { group ->
            group.maxByOrNull { it.count { c -> c == '.' } } ?: group.first()
        }
        .take(4)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(Dimens.RadiusCard)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = {
            tap()
            onClick()
        },
        interactionSource = interaction
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceMd)
        ) {
            Text(
                stop.name,
                color = Ink,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (stop.location.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    stop.location,
                    color = Muted,
                    fontSize = 13.sp,
                    maxLines = 2
                )
            }
            if (routeIds.isNotEmpty()) {
                Spacer(Modifier.height(Dimens.SpaceSm))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    routeIds.forEach { id ->
                        RouteChip(id = id)
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteChip(id: String) {
    Box(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MintSoft)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            id,
            color = MintDark,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 240.dp)
        )
    }
}
