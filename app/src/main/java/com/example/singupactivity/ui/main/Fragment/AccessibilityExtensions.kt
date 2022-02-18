package com.example.singupactivity.ui.main.Fragment

import android.app.Fragment
import android.content.Context
import android.content.Context.ACCESSIBILITY_SERVICE
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.TextView
import com.example.singupactivity.R


val Context.isTalkBackEnabled: Boolean
    get() = (getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager).isTouchExplorationEnabled


fun TextView.setNumberDescription(): String {
    val numberDescription =  this.text.toList().joinToString("") {
        when {
            it.isDigit() -> "$it "
            else -> it.toString()
        }
    }
    this.contentDescription = numberDescription
    return numberDescription
}

fun String.setNumberDescription(): String =
        this.toList().joinToString("") {
            when {
                it.isDigit() -> "$it "
                else -> it.toString()
            }
        }


val Context.isInversionModeEnabled: Boolean
    get() = if (Build.VERSION.SDK_INT >= 21) {
        val accessibilityEnabled: Int = try {
            Settings.Secure.getInt(contentResolver,
                    Settings.Secure.ACCESSIBILITY_DISPLAY_INVERSION_ENABLED)
        } catch (e: SettingNotFoundException) {
            Log.d(getString(R.string.app_name), "Error finding setting ACCESSIBILITY_DISPLAY_INVERSION_ENABLED: " + e.message)
            Log.d(getString(R.string.app_name), "Checking negative color enabled status")
            val semHighContrast = "high_contrast"
            Settings.System.getInt(contentResolver, semHighContrast, 0)
        }

        accessibilityEnabled == 1
    } else false


val Context.isNightModeEnabled: Boolean
    get() = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

