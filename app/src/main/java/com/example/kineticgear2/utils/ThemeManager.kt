package com.example.kineticgear2.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    // Check if dark mode is saved
    fun isDarkMode(): Boolean {
        return prefs.getBoolean("is_dark_mode", false)
    }

    // Toggle and save the preference
    fun setDarkMode(isDark: Boolean) {
        prefs.edit().putBoolean("is_dark_mode", isDark).apply()
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // Call this in Application class or Login Activity to apply saved theme on startup
    fun applySavedTheme() {
        if (isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}