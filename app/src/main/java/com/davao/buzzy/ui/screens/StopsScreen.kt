package com.davao.buzzy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davao.buzzy.data.DataState
import com.davao.buzzy.ui.components.AnimatedEntry
import com.davao.buzzy.ui.components.ScreenHeader
import com.davao.buzzy.ui.components.SearchBar
import com.davao.buzzy.ui.components.StopCard
import com.davao.buzzy.ui.theme.Canvas
import com.davao.buzzy.ui.theme.Dimens
import com.davao.buzzy.ui.theme.Muted
import kotlinx.coroutines.launch

@Composable
fun StopsScreen(
    onOpenStop: (String) -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    val stops by DataState.stops.collectAsState()
    val loading by DataState.loading.collectAsState()

    var query by remember { mutableStateOf("") }
    val filtered = remember(stops, query) {
        val sorted = stops.sortedBy { it.name }
        val q = query.trim().lowercase()
        if (q.isEmpty()) {
            sorted
        } else {
            sorted.filter {
                it.name.lowercase().contains(q) || it.location.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = Dimens.SpaceSm, bottom = Dimens.SpaceSm)
        ) {
            ScreenHeader(
                title = "Stops near you \uD83D\uDE8F",
                subtitle = "Every DIBS stop in Davao",
                loading = loading,
                onRefresh = { scope.launch { DataState.refresh(ctx) } }
            )
            Spacer(Modifier.height(Dimens.SpaceMd))
            Box(Modifier.padding(horizontal = Dimens.SpaceLg)) {
                SearchBar(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = "Search stops\u2026"
                )
            }
            Spacer(Modifier.height(Dimens.SpaceSm))
            Box(Modifier.padding(horizontal = Dimens.SpaceLg)) {
                Text(
                    text = "${filtered.size} stops",
                    color = Muted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = Dimens.SpaceLg,
                end = Dimens.SpaceLg,
                top = Dimens.SpaceSm,
                bottom = 140.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filtered.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(Dimens.SpaceXl),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (query.isEmpty())
                                "Rolling up the schedule\u2026"
                            else
                                "Hmm, nothing here. Try another name?",
                            color = Muted,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                itemsIndexed(filtered, key = { _, item -> item.name }) { index, stop ->
                    AnimatedEntry(index = index) {
                        StopCard(
                            stop = stop,
                            onClick = { onOpenStop(stop.name) }
                        )
                    }
                }
            }
        }
    }
}
