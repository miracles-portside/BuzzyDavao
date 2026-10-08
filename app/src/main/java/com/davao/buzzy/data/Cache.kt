package com.davao.buzzy.data

import android.content.Context
import kotlinx.serialization.json.Json
import java.io.File

object Cache {

    private const val ROUTES_FILE = "routes_cache.json"
    private const val STOPS_FILE = "stops_cache.json"

    private val json = Json { ignoreUnknownKeys = true }

    fun saveRoutes(ctx: Context, jsonText: String) =
        File(ctx.filesDir, ROUTES_FILE).writeText(jsonText)

    fun saveStops(ctx: Context, jsonText: String) =
        File(ctx.filesDir, STOPS_FILE).writeText(jsonText)

    fun readRoutes(ctx: Context): List<RouteItem> {
        val f = File(ctx.filesDir, ROUTES_FILE)
        if (!f.exists()) return emptyList()
        return runCatching {
            json.decodeFromString<RoutesResponse>(f.readText()).data
        }.getOrDefault(emptyList())
    }

    fun readStops(ctx: Context): List<Stop> {
        val f = File(ctx.filesDir, STOPS_FILE)
        if (!f.exists()) return emptyList()
        return runCatching {
            json.decodeFromString<StopsResponse>(f.readText()).data?.stops ?: emptyList()
        }.getOrDefault(emptyList())
    }
}
