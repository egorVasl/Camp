package com.example.singupactivity.ui.main.Activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import com.example.singupactivity.R
import com.google.android.material.appbar.AppBarLayout



const val TOOLBAR_ELEVATION = 8f

abstract class BaseActivity : AppCompatActivity() {

    val TAKE_SCREENSHOT = "screenshot"

    protected abstract val layout: Int
    abstract val appBarLayout: AppBarLayout
    abstract val toolbar: Toolbar

    val Context.defaultSharedPreferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(this)

    var SharedPreferences.canTakeScreenshot: Boolean
        get() = getBoolean(TAKE_SCREENSHOT, true)
        set(value) {
            edit {
                putBoolean(TAKE_SCREENSHOT, value)
            }
        }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BibLocaleHelper.onAttach(baseContext)
        super.onCreate(savedInstanceState)

        if (defaultSharedPreferences.canTakeScreenshot) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE)
        }

        setContentView(layout)
    }

    override fun onStart() {
        super.onStart()
        setSupportActionBar(toolbar)
        setupToolbar()
    }

    protected open fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            try {
                onBackPressed()
            } catch (e: Exception) {
            }
        }
        supportActionBar?.setHomeActionContentDescription(R.string.back)
    }

    override fun onDestroy() {
        with(toolbar) {
            setOnMenuItemClickListener(null)
            setNavigationOnClickListener(null)
        }

        super.onDestroy()
    }
}
