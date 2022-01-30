package com.example.singupactivity.ui.main.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.singupactivity.R
import com.example.singupactivity.databinding.BottomDialogRatesBinding
import com.example.singupactivity.ui.main.Activity.SearchActivity

const val RATES_BOTTOM_REQUEST_KEY = "RATES_BOTTOM_REQUEST_KEY"
const val RATES_BOTTOM_BUNDLE_KEY = "RATES_BOTTOM_BUNDLE_KEY"

const val RATES_BOTTOM_REQUEST_KEY_SEARCH = "RATES_BOTTOM_REQUEST_KEY_SEARCH"
const val RATES_BOTTOM_BUNDLE_KEY_SEARCH = "RATES_BOTTOM_BUNDLE_KEY_SEARCH"

const val RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF = "RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF"
const val RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF = "RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF"

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

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        binding.tvInsertEvent.setOnClickListener {
            setFragmentResult(RATES_BOTTOM_REQUEST_KEY, bundleOf(RATES_BOTTOM_BUNDLE_KEY to true))
            dismiss()
        }

        binding.tvSearch.setOnClickListener {
            SearchActivity.start(act)
            dismiss()
        }

        binding.tvImportPDF.setOnClickListener {
            setFragmentResult(
                RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF,
                bundleOf(RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF to true)
            )
            dismiss()

        }
    }
}