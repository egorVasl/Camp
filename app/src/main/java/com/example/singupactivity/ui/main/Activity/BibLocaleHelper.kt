package com.example.singupactivity.ui.main.Activity

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.*

private const val SELECTED_LANGUAGE = "selectedLanguage"

object BibLocaleHelper {

    fun onAttach(context: Context?): Context? {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang, null)
    }

    fun getLanguage(context: Context?): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context?, language: String?, callback: OnCallback?): Context? {
        persist(context, language)
        return updateResources(context, language, callback)
    }

    private fun getPersistedData(context: Context?, defaultLanguage: String): String? {
        val preferences = context?.getSharedPreferences("ender", Context.MODE_PRIVATE)
        return preferences?.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context?, language: String?) {
        val preferences = context?.getSharedPreferences("ender", Context.MODE_PRIVATE)
        preferences?.edit {
            putString(SELECTED_LANGUAGE, language)
        }
    }

    private fun updateResources(context: Context?, language: String?, callback: OnCallback?): Context? {
        val locale = Locale(language!!)
        Locale.setDefault(locale)

        val resources = context?.resources

        val configuration = resources?.configuration
        if (configuration != null) {
            configuration.locale = locale
            context.createConfigurationContext(configuration)
        }

        resources?.updateConfiguration(configuration, resources.displayMetrics)
        callback?.updateConfiguration(configuration)

        return context
    }

    interface OnCallback {
        fun updateConfiguration(conf: Configuration?)
    }
}