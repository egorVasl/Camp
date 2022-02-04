package com.example.singupactivity.ui.main.Fragment.BottomSheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.singupactivity.R
import com.example.singupactivity.databinding.DailyScheduleBottomHeetDialogBinding
import com.example.singupactivity.ui.main.Activity.SearchActivity
import com.example.singupactivity.ui.main.Fragment.act

const val RATES_BOTTOM_REQUEST_KEY_SQUADS = "RATES_BOTTOM_REQUEST_KEY_SQUADS"
const val RATES_BOTTOM_BUNDLE_KEY_SQUADS = "RATES_BOTTOM_BUNDLE_KEY_SQUADS"

const val RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_SQUADS = "RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_SQUADS"
const val RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_SQUADS = "RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_SQUADS"

class SquadsBottomSheet :
    BaseBottomSheetDialog<DailyScheduleBottomHeetDialogBinding>(
        R.layout.daily_schedule_bottom_heet_dialog
    ) {
    companion object {
        fun newInstance(): SquadsBottomSheet {
            return SquadsBottomSheet()
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
            setFragmentResult(RATES_BOTTOM_REQUEST_KEY_SQUADS, bundleOf(RATES_BOTTOM_BUNDLE_KEY_SQUADS to true))
            dismiss()
        }

        binding.tvSearch.setOnClickListener {
            SearchActivity.start(act, "Squads")
            dismiss()
        }

        binding.tvImportPDF.setOnClickListener {
            setFragmentResult(
                RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_SQUADS,
                bundleOf(RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_SQUADS to true)
            )
            dismiss()

        }
    }
}