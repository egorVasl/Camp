package com.example.singupactivity.ui.main.Fragment

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager

val Context.keyboard: InputMethodManager
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getSystemService(InputMethodManager::class.java)
    } else {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

fun Activity.showKeyboard() = keyboard.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

fun Activity.hideKeyboard() = keyboard.hideSoftInputFromWindow(window.decorView.windowToken, 0)
