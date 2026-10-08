package com.davao.buzzy.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutesResponse(
    val success: Boolean,
    val message: String = "",
    val data: List<RouteItem> = emptyList()
)

@Serializable
data class RouteItem(
    val id: String,
    val name: String
)

@Serializable
data class StopsResponse(
    val success: Boolean,
    val message: String = "",
    val data: StopsData? = null
)

@Serializable
data class StopsData(
    val stops: List<Stop> = emptyList()
)

@Serializable
data class Stop(
    val name: String,
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val routes: List<StopRoute> = emptyList()
)

@Serializable
data class StopRoute(
    @SerialName("shift") val shift: String = "",
    @SerialName("route_name") val routeName: String = ""
)
