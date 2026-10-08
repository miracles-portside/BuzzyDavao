@file:OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.davao.buzzy.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davao.buzzy.data.DataState
import com.davao.buzzy.data.StreakStore
import com.davao.buzzy.ui.components.PillButton
import com.davao.buzzy.ui.components.RouteCard
import com.davao.buzzy.ui.components.StreakCard
import com.davao.buzzy.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Calendar
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenRoutes: () -> Unit,
    onOpenMap: () -> Unit,
    onOpenStops: (String) -> Unit,
    onOpenRoute: (String, String) -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current

    val routes by DataState.routes.collectAsState()

    var streak by remember { mutableStateOf(0) }
    var total by remember { mutableStateOf(0) }
    var query by remember { mutableStateOf("") }
    var appeared by remember { mutableStateOf(false) }

    // Load streak + bootstrap
    LaunchedEffect(Unit) {
        streak = StreakStore.checkToday(ctx)
        total = StreakStore.total(ctx)
        DataState.bootstrap(ctx)
        appeared = true
    }

    // Time-based greeting
    val greeting = remember {
        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Good morning 👋"
            in 12..17 -> "Good afternoon 👋"
            in 18..21 -> "Good evening 👋"
            else -> "Still up? 👋"
        }
    }

    val enterAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(300),
        label = "enterAlpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
            .padding(horizontal = Dimens.SpaceLg),
        contentPadding = PaddingValues(
            top = Dimens.SpaceXl,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpaceMd)
    ) {
        // Greeting
        item {
            Column(Modifier.alpha(enterAlpha)) {
                Text(
                    text = greeting,
                    color = Ink,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 38.sp
                )
                Spacer(Modifier.height(Dimens.SpaceXs))
                Text(
                    text = "Where are you headed today?",
                    color = InkMid,
                    fontSize = 15.sp
                )
            }
        }

        // Streak
        item {
            StreakCard(streak = streak, total = total)
        }

        // Search
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimens.RadiusCard)),
                placeholder = { Text("Search stops or routes…", color = Muted) },
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = Mint)
                },
                singleLine = true,
                shape = RoundedCornerShape(Dimens.RadiusCard),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Mint,
                    unfocusedBorderColor = Line,
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboard?.hide()
                        onOpenStops(query)
                    }
                )
            )
        }

        // Quick pill buttons
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PillButton(
                    text = "See the city",
                    background = MintSoft,
                    textColor = MintDark,
                    onClick = onOpenMap,
                    modifier = Modifier.weight(1f)
                )
                PillButton(
                    text = "Pick your ride",
                    background = Mint,
                    textColor = SurfaceCard,
                    onClick = onOpenRoutes,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section title
        item {
            Spacer(Modifier.height(Dimens.SpaceSm))
            Text(
                text = "POPULAR ROUTES TODAY",
                color = Muted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        // Top 3 routes
        items(routes.take(3), key = { it.id }) { route ->
            RouteCard(
                route = route,
                onClick = { onOpenRoute(route.id, route.name) }
            )
        }

        // Hint if no data yet
        if (routes.isEmpty()) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(Dimens.SpaceXl),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Rolling up the schedule…",
                        color = Muted,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}