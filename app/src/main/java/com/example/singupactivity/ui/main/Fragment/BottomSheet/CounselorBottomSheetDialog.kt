package com.example.singupactivity.ui.main.Fragment.BottomSheet

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.singupactivity.R
import com.example.singupactivity.databinding.CounselorBottomHeetDialogBinding
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Counselor.ArgumentsCounselorItem
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class CounselorBottomSheetDialog :
    BaseBottomSheetDialog<CounselorBottomHeetDialogBinding>(
        R.layout.counselor_bottom_heet_dialog
    ) {
    companion object {
        fun newInstance(): CounselorBottomSheetDialog {
            return CounselorBottomSheetDialog()
        }
    }
    lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
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

        binding.btnInsertData.setOnClickListener {

            //tiName
            if (binding.tiName.editText!!.text.toString().isBlank()) {
                binding.tiName.error = getString(R.string.error_empty_field)
                binding.tiName.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.nameCounselor =
                    binding.tiName.editText!!.text.toString()
            }

            //tiSurname
            if (binding.tiSurname.editText!!.text.toString().isBlank()) {
                binding.tiSurname.error = getString(R.string.error_empty_field)
                binding.tiSurname.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.surnameCounselor =
                    binding.tiSurname.editText!!.text.toString()
            }
            //tiPatronymic
            if (binding.tiPatronymic.editText!!.text.toString().isBlank()) {
                binding.tiPatronymic.error = getString(R.string.error_empty_field)
                binding.tiPatronymic.defaultHintTextColor =
                    ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.patronymicCounselor =
                    binding.tiPatronymic.editText!!.text.toString()
            }

            //tiBirthday
            if (binding.tiBirthday.editText!!.text.toString().isBlank()) {
                binding.tiBirthday.error = getString(R.string.error_empty_field)
                binding.tiBirthday.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.birthdayCounselor =
                    binding.tiBirthday.editText!!.text.toString()
            }

            //tiPhoneNumber
            if (binding.tiPhoneNumber.editText!!.text.toString().isBlank()) {
                binding.tiPhoneNumber.error = getString(R.string.error_empty_field)
                binding.tiPhoneNumber.defaultHintTextColor =
                    ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.numberPhoneCounselor =
                    binding.tiPhoneNumber.editText!!.text.toString()
            }

            if (ArgumentsCounselorItem.nameCounselor.isNotBlank() &&
                ArgumentsCounselorItem.surnameCounselor.isNotBlank() &&
                ArgumentsCounselorItem.patronymicCounselor.isNotBlank() &&
                ArgumentsCounselorItem.birthdayCounselor.isNotBlank() &&
                ArgumentsCounselorItem.numberPhoneCounselor.isNotBlank()

            ) {

                runBlocking {
                    async {
                        campDbManager.updateRawToTableCounselor(
                            ArgumentsCounselorItem.nameCounselor,
                            ArgumentsCounselorItem.surnameCounselor,
                            ArgumentsCounselorItem.patronymicCounselor,
                            ArgumentsCounselorItem.birthdayCounselor,
                            ArgumentsCounselorItem.numberPhoneCounselor,
                            ArgumentsNAlogin.login
                        )
                    }.await()
                }

                alert(getString(R.string.notification), getString(R.string.add_counselor_correct))
                dismiss()
            }
        }
    }

    private fun alert(title: String, massage: String) {
        val builder = AlertDialog.Builder(act)
        builder.setTitle(title)
            .setMessage(massage)
            .setCancelable(false)
            .setPositiveButton(R.string.contin) { dialog, _ ->
                dialog.dismiss()

            }

        val alert = builder.create()
        alert.show()
    }
}