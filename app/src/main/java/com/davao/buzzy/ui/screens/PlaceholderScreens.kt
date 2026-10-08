package com.davao.buzzy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.davao.buzzy.ui.theme.Canvas

@Composable
fun HomeScreen() = Placeholder("Home 👋")

@Composable
fun RoutesScreen() = Placeholder("Routes 🚌")

@Composable
fun StopsScreen() = Placeholder("Stops 🚏")

@Composable
fun MapScreen() = Placeholder("Map 🗺️")

@Composable
private fun Placeholder(text: String) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Canvas),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1F))
    }
}
