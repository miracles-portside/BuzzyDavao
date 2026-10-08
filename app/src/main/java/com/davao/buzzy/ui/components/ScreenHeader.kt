package com.davao.buzzy.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davao.buzzy.ui.theme.*

@Composable
fun ScreenHeader(
    title: String,
    subtitle: String? = null,
    loading: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpaceLg),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                title,
                color = Ink,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            )
            if (subtitle != null) {
                Spacer(Modifier.height(Dimens.SpaceXs))
                Text(
                    subtitle,
                    color = InkMid,
                    fontSize = 14.sp
                )
            }
        }
        if (onRefresh != null) {
            IconButton(onClick = onRefresh) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Mint,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Rounded.Refresh,
                        contentDescription = "Refresh",
                        tint = Mint
                    )
                }
            }
        }
    }
}
