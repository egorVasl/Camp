package com.example.singupactivity.ui.main.Fragment

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.singupactivity.R
import com.example.singupactivity.databinding.BottomDialogRatesBinding



class DailyScheduleBottomSheetDialog :
    BaseBottomSheetDialog<BottomDialogRatesBinding>(
        R.layout.bottom_dialog_rates
    ) {
companion object{
    fun newInstance(): DailyScheduleBottomSheetDialog {
        return DailyScheduleBottomSheetDialog()
    }
}



    override fun onStart() {
        super.onStart()
        val window: Window? = dialog?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        binding.tvInsertEvent.setOnClickListener {

        }

        binding.tvSearch.setOnClickListener {


        }
    }
}