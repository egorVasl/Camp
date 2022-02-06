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

const val RATES_BOTTOM_REQUEST_KEY_ACHIEVEMENTS = "RATES_BOTTOM_REQUEST_KEY_ACHIEVEMENTS"
const val RATES_BOTTOM_BUNDLE_KEY_ACHIEVEMENTS = "RATES_BOTTOM_BUNDLE_KEY_ACHIEVEMENTS"

const val RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_ACHIEVEMENTS = "RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_ACHIEVEMENTS"
const val RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_ACHIEVEMENTS = "RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_ACHIEVEMENTS"

class AchievementsBottomSheetDialog :
    BaseBottomSheetDialog<DailyScheduleBottomHeetDialogBinding>(
        R.layout.daily_schedule_bottom_heet_dialog
    ) {
    companion object {
        fun newInstance(): AchievementsBottomSheetDialog {
            return AchievementsBottomSheetDialog()
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

        binding.tvInsertEvent.text = getString(R.string.add_achievements)

        binding.tvInsertEvent.setOnClickListener {
            setFragmentResult(RATES_BOTTOM_REQUEST_KEY_ACHIEVEMENTS, bundleOf(RATES_BOTTOM_BUNDLE_KEY_ACHIEVEMENTS to true))
            dismiss()
        }

        binding.tvSearch.setOnClickListener {
            SearchActivity.start(act, "Achievements")
            dismiss()
        }

        binding.tvImportPDF.setOnClickListener {
            setFragmentResult(
                RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_ACHIEVEMENTS,
                bundleOf(RATES_BOTTOM_BUNDLE_KEY_IMPORT_PDF_ACHIEVEMENTS to true)
            )
            dismiss()

        }
    }
}