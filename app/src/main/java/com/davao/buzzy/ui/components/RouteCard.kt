package com.davao.buzzy.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davao.buzzy.data.RouteItem
import com.davao.buzzy.ui.theme.Coral
import com.davao.buzzy.ui.theme.Dimens
import com.davao.buzzy.ui.theme.Ink
import com.davao.buzzy.ui.theme.Mint
import com.davao.buzzy.ui.theme.Motion
import com.davao.buzzy.ui.theme.SurfaceCard
import com.davao.buzzy.ui.theme.rememberTapHaptics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteCard(
    route: RouteItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPM = route.id.endsWith("PM")
    val baseCode = route.id.removeSuffix("AM").removeSuffix("PM")
    val tint = if (isPM) Coral else Mint

    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.96f else 1f,
        animationSpec = Motion.Press,
        label = "routeCardScale"
    )
    val tap = rememberTapHaptics()

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
        Row(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.SpaceMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .width(6.dp)
                    .height(44.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(tint)
            )

            Spacer(Modifier.width(Dimens.SpaceMd))

            Column(Modifier.weight(1f)) {
                Text(
                    text = "$baseCode \u00B7 ${if (isPM) "PM" else "AM"}",
                    color = tint,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = route.name,
                    color = Ink,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
