package com.davao.buzzy.data

/**
 * DIBS returns separate entries per AM/PM shift. Merge them so each stop
 * appears once, with all its routes combined.
 */
fun List<Stop>.merged(): List<Stop> {
    val byName = LinkedHashMap<String, Stop>()
    for (stop in this) {
        val key = stop.name.trim().lowercase()
        val existing = byName[key]
        if (existing == null) {
            byName[key] = stop
        } else {
            // Merge: keep first location/lat/lng, combine routes, dedupe
            val combinedRoutes = (existing.routes + stop.routes)
                .distinctBy { "${it.shift}-${it.routeName}" }
            byName[key] = existing.copy(routes = combinedRoutes)
        }
    }
    return byName.values.toList()
}
