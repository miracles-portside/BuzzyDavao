package com.davao.buzzy.data

import android.content.Context
import java.time.LocalDate

object StreakStore {

    private const val PREFS = "buzzy_streaks"
    private const val KEY_TOTAL = "total_checkins"
    private const val KEY_FIRST_DAY = "first_day"

    /** Record that the user opened the app today. Returns current streak. */
    fun checkToday(ctx: Context): Int {
        val p = ctx.getSharedPreferences(PREFS, 0)
        val today = LocalDate.now().toString()
        val last = p.getString("last_day", null)

        var streak = p.getInt("streak", 0)

        if (last == today) return streak

        streak = if (last == LocalDate.now().minusDays(1).toString()) streak + 1 else 1

        val total = p.getInt(KEY_TOTAL, 0) + 1
        val firstDay = p.getString(KEY_FIRST_DAY, null) ?: today

        p.edit()
            .putString("last_day", today)
            .putInt("streak", streak)
            .putInt(KEY_TOTAL, total)
            .putString(KEY_FIRST_DAY, firstDay)
            .apply()

        return streak
    }

    fun current(ctx: Context): Int =
        ctx.getSharedPreferences(PREFS, 0).getInt("streak", 0)

    fun total(ctx: Context): Int =
        ctx.getSharedPreferences(PREFS, 0).getInt(KEY_TOTAL, 0)
}
