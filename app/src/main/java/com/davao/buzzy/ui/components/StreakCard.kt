package com.davao.buzzy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davao.buzzy.ui.theme.*

@Composable
fun StreakCard(streak: Int, total: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.RadiusCard))
            .background(MintSoft)
            .padding(Dimens.SpaceLg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🔥", fontSize = 36.sp)
        Spacer(Modifier.width(Dimens.SpaceMd))
        Column(Modifier.weight(1f)) {
            Text(
                text = if (streak >= 3) "You're on a roll!" else "Welcome back!",
                color = Ink,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "$streak-day streak · $total total check-ins",
                color = MintDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
