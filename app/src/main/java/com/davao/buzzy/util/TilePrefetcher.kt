package com.davao.buzzy.util

import android.content.Context
import android.util.Log
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.MapView

object TilePrefetcher {

    /**
     * Downloads tiles for Davao City into the on-disk cache so the map
     * works offline later. Runs on a background thread.
     */
    fun prefetchDavao(context: Context, map: MapView) {
        val bounds = BoundingBox(7.55, 6.75, 125.90, 125.20)  // N, S, E, W
        val zoomMin = 12
        val zoomMax = 15  // 16+ is many GB; 12-15 covers street level

        Thread {
            try {
                val cm = CacheManager(map)
                val count = cm.possibleTilesInArea(bounds, zoomMin, zoomMax)
                Log.i("TilePrefetch", "Tiles to download: $count")

                cm.downloadAreaAsync(
                    context, bounds, zoomMin, zoomMax,
                    object : CacheManager.CacheManagerCallback {

                        override fun onTaskComplete() {
                            Log.i("TilePrefetch", "✅ Download complete")
                        }

                        override fun updateProgress(
                            progress: Int,
                            currentZoomLevel: Int,
                            minZoomLevel: Int,
                            maxZoomLevel: Int
                        ) {
                            Log.i("TilePrefetch",
                                "Progress: $progress% (zoom $currentZoomLevel)")
                        }

                        override fun downloadStarted() {
                            Log.i("TilePrefetch", "Started downloading")
                        }

                        override fun setPossibleTilesInArea(total: Int) {
                            Log.i("TilePrefetch", "Possible tiles in area: $total")
                        }

                        override fun onTaskFailed(errors: Int) {
                            Log.w("TilePrefetch", "Failed tiles: $errors")
                        }
                    }
                )
            } catch (e: Exception) {
                Log.e("TilePrefetch", "Prefetch failed", e)
            }
        }.start()
    }
}
