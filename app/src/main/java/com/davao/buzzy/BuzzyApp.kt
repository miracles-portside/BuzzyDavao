package com.davao.buzzy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.davao.buzzy.ui.components.FloatingNavPill
import com.davao.buzzy.ui.components.NavItem
import com.davao.buzzy.ui.screens.*
import com.davao.buzzy.ui.theme.BuzzyTheme
import com.davao.buzzy.ui.theme.Canvas
import com.davao.buzzy.ui.theme.Dimens
import com.davao.buzzy.ui.theme.rememberTapHaptics

private val NAV_ITEMS = listOf(
    NavItem("home",   "Home"),
    NavItem("routes", "Routes"),
    NavItem("stops",  "Stops"),
    NavItem("map",    "Map"),
)

@Composable
fun BuzzyApp() {
    BuzzyTheme {
        var activeKey by remember { mutableStateOf("home") }
        val tap = rememberTapHaptics()

        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ─── Content ─────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                when (activeKey) {
                    "home" -> HomeScreen(
                        onOpenRoutes = { tap(); activeKey = "routes" },
                        onOpenMap = { tap(); activeKey = "map" },
                        onOpenStops = { tap(); activeKey = "stops" },
                        onOpenRoute = { _, _ -> tap() }
                    )
                    "routes" -> RoutesScreen()
                    "stops"  -> StopsScreen()
                    "map"    -> MapScreen()
                }
            }

            // ─── Bottom haze (iOS-style fade above the pill) ─────────
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.30f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Canvas.copy(alpha = 0f),
                                Canvas.copy(alpha = 0.45f),
                                Canvas.copy(alpha = 0.78f),
                                Canvas.copy(alpha = 0.94f)
                            )
                        )
                    )
            )

            // ─── Floating pill ────────────────────────────────────────
            FloatingNavPill(
                items = NAV_ITEMS,
                activeKey = activeKey,
                onPick = { key ->
                    tap()
                    activeKey = key
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = Dimens.NavBottomMargin)
                    .padding(horizontal = Dimens.NavHorizontalMargin)
            )
        }
    }
}
