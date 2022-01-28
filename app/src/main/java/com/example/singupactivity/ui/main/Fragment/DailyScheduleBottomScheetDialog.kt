package com.example.singupactivity.ui.main.Fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.singupactivity.R
import com.example.singupactivity.databinding.BottomDialogRatesBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(IS_CHECKED_TRUE_REQUEST_KEY) { _, _ ->
            binding.checkSearchStart.isChecked = false
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        binding.tvInsertEvent.setOnClickListener {
            setFragmentResult(RATES_BOTTOM_REQUEST_KEY, bundleOf(RATES_BOTTOM_BUNDLE_KEY to true))
            dismiss()
        }

        binding.checkSearchStart.setOnCheckedChangeListener { buttonView, isChecked ->
           setFragmentResult(RATES_BOTTOM_REQUEST_KEY_SEARCH, bundleOf(RATES_BOTTOM_BUNDLE_KEY_SEARCH to isChecked))

        }

        binding.tvImportPDF.setOnClickListener{
            setFragmentResult(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF, bundleOf(RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF to true))
            dismiss()

        }
    }


}