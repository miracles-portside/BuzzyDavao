package com.davao.buzzy.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Simple in-memory state holder. Loads cache first, then refreshes from network.
 * Exposed as StateFlow so Compose screens can collect reactively.
 */
object DataState {

    private val _routes = MutableStateFlow<List<RouteItem>>(emptyList())
    val routes: StateFlow<List<RouteItem>> = _routes.asStateFlow()

    private val _stops = MutableStateFlow<List<Stop>>(emptyList())
    val stops: StateFlow<List<Stop>> = _stops.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _loaded = MutableStateFlow(false)

    /** Call once per app launch. Loads cache, then refreshes from network. */
    suspend fun bootstrap(ctx: Context) {
        if (_loaded.value) return
        _loading.value = true

        // 1. Cache instant
        _routes.value = Cache.readRoutes(ctx)
        _stops.value = Cache.readStops(ctx).merged()

        // 2. Fresh in background
        val freshRoutes = ApiClient.fetchRoutes()
        if (freshRoutes.isNotEmpty()) _routes.value = freshRoutes

        val freshStops = ApiClient.fetchStops().merged()
        if (freshStops.isNotEmpty()) _stops.value = freshStops

        _loading.value = false
        _loaded.value = true
    }

    suspend fun refresh(ctx: Context) {
        _loading.value = true
        val freshRoutes = ApiClient.fetchRoutes()
        if (freshRoutes.isNotEmpty()) _routes.value = freshRoutes

        val freshStops = ApiClient.fetchStops().merged()
        if (freshStops.isNotEmpty()) _stops.value = freshStops

        _loading.value = false
    }
}
