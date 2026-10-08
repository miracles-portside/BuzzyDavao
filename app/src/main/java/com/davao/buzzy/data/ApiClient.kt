package com.davao.buzzy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val ENDPOINT = "https://davaobus.davaocity.gov.ph/dibs/application/api/bus.php"
    private const val REFERER = "https://davaobus.davaocity.gov.ph/dibs/public/bus/stops"
    private const val ORIGIN = "https://davaobus.davaocity.gov.ph"
    private const val UA = "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun fetchRoutes(): List<RouteItem> = withContext(Dispatchers.IO) {
        val body = FormBody.Builder().add("trans", "GETROUTES").build()
        val text = post(body) ?: return@withContext emptyList()
        runCatching { json.decodeFromString<RoutesResponse>(text).data }
            .getOrDefault(emptyList())
    }

    suspend fun fetchStops(): List<Stop> = withContext(Dispatchers.IO) {
        val body = FormBody.Builder()
            .add("trans", "GETBUSSTOPLOCATIONS")
            .add("id", "ALL")
            .build()
        val text = post(body) ?: return@withContext emptyList()
        runCatching {
            json.decodeFromString<StopsResponse>(text).data?.stops ?: emptyList()
        }.getOrDefault(emptyList())
    }

    private fun post(body: FormBody): String? {
        val req = Request.Builder()
            .url(ENDPOINT)
            .header("User-Agent", UA)
            .header("X-Requested-With", "XMLHttpRequest")
            .header("Origin", ORIGIN)
            .header("Referer", REFERER)
            .post(body)
            .build()
        return try {
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) null else resp.body?.string()
            }
        } catch (e: Exception) {
            null
        }
    }
}
