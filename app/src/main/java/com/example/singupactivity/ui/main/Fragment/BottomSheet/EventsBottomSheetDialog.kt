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


const val RATES_BOTTOM_REQUEST_KEY_EVENTS = "RATES_BOTTOM_REQUEST_KEY_EVENTS"
const val RATES_BOTTOM_BUNDLE_KEY_EVENTS = "RATES_BOTTOM_BUNDLE_KEY_EVENTS"

const val RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_EVENTS = "RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_EVENTS"
const val RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_EVENTS = "RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_EVENTS"

class EventsBottomSheetDialog :
    BaseBottomSheetDialog<DailyScheduleBottomHeetDialogBinding>(
        R.layout.daily_schedule_bottom_heet_dialog
    ) {
    companion object {
        fun newInstance(): EventsBottomSheetDialog {
            return EventsBottomSheetDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

    }

//    private val type by lazy { arguments?.getString(TYPE, "") ?: "" }


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        binding.tvInsertEvent.setOnClickListener {
            setFragmentResult(RATES_BOTTOM_REQUEST_KEY_EVENTS, bundleOf(RATES_BOTTOM_BUNDLE_KEY_EVENTS to true))
            dismiss()
        }

        binding.tvSearch.setOnClickListener {
            SearchActivity.start(act, "Events")
            dismiss()
        }

        binding.tvImportPDF.setOnClickListener {
            setFragmentResult(
                RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_EVENTS,
                bundleOf(RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_EVENTS to true)
            )
            dismiss()

        }
    }
}