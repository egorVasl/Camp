package com.example.singupactivity.ui.main.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.example.singupactivity.R

class ProgressBarDialogTransparentBg(val activity: Activity) : AlertDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        setContentView(R.layout.progress_dialog)
        setCanceledOnTouchOutside(false)
        setOnCancelListener { activity.onBackPressed() }
    }
}
