package com.example.singupactivity.ui.main.Fragment.BottomSheet

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.singupactivity.R
import com.example.singupactivity.databinding.CounselorBottomHeetDialogBinding
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.Fragment.ProgressBarDialog
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Counselor.ArgumentsCounselorItem
import com.example.singupactivity.ui.main.Objects.NavigationActviy.ArgumentsNAlogin
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

const val COUNSELOR_BOTTOM_REQUEST_KEY = "COUNSELOR_BOTTOM_REQUEST_KEY"
const val COUNSELOR_BOTTOM_BUNDLE_KEY = "COUNSELOR_BOTTOM_BUNDLE_KEY"


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
    lateinit var loginList: ArrayList<String>
    lateinit var counselorName: ArrayList<String>
    lateinit var counselorSurname: ArrayList<String>
    lateinit var counselorPatronymic: ArrayList<String>
    lateinit var counselorBirthday: ArrayList<String>
    lateinit var counselorPhoneNumber: ArrayList<String>
    lateinit var counselorLoginCounselor: ArrayList<String>

    override lateinit var progressDialog: Dialog

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

        setDataToTextInput()

        binding.btnInsertData.setOnClickListener {

            //tiName
            if (binding.tiName.editText!!.text.toString().isEmpty()) {
                binding.tiName.error = getString(R.string.error_empty_field)
                binding.tiName.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.nameCounselor =
                    binding.tiName.editText!!.text.toString()
            }

            //tiSurname
            if (binding.tiSurname.editText!!.text.toString().isEmpty()) {
                binding.tiSurname.error = getString(R.string.error_empty_field)
                binding.tiSurname.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.surnameCounselor =
                    binding.tiSurname.editText!!.text.toString()
            }
            //tiPatronymic
            if (binding.tiPatronymic.editText!!.text.toString().isEmpty()) {
                binding.tiPatronymic.error = getString(R.string.error_empty_field)
                binding.tiPatronymic.defaultHintTextColor =
                    ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.patronymicCounselor =
                    binding.tiPatronymic.editText!!.text.toString()
            }

            //tiBirthday
            if (binding.tiBirthday.editText!!.text.toString().isEmpty()) {
                binding.tiBirthday.error = getString(R.string.error_empty_field)
                binding.tiBirthday.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.birthdayCounselor =
                    binding.tiBirthday.editText!!.text.toString()
            }

            //tiPhoneNumber
            if (binding.tiPhoneNumber.editText!!.text.toString().isEmpty()) {
                binding.tiPhoneNumber.error = getString(R.string.error_empty_field)
                binding.tiPhoneNumber.defaultHintTextColor =
                    ctx.getColorStateList(R.color.errorColor)
            } else {
                ArgumentsCounselorItem.numberPhoneCounselor =
                    binding.tiPhoneNumber.editText!!.text.toString()
            }

            if (ArgumentsCounselorItem.nameCounselor.isNotEmpty() &&
                ArgumentsCounselorItem.surnameCounselor.isNotEmpty() &&
                ArgumentsCounselorItem.patronymicCounselor.isNotEmpty() &&
                ArgumentsCounselorItem.birthdayCounselor.isNotEmpty() &&
                ArgumentsCounselorItem.numberPhoneCounselor.isNotEmpty()

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

                setFragmentResult(COUNSELOR_BOTTOM_REQUEST_KEY, bundleOf(COUNSELOR_BOTTOM_BUNDLE_KEY to true))

                alert(getString(R.string.notification), getString(R.string.add_counselor_correct))
                dismiss()
            }
        }
    }
    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableCounselor(const)

    }
    @SuppressLint("SetTextI18n")
    fun setDataToTextInput(){
        progressDialog.show()
        counselorName =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_COUNSELOR_NAME)
                }.await()
            }
        counselorSurname =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_COUNSELOR_SURNAME)
                }.await()
            }
        counselorPatronymic =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_COUNSELOR_PATRONYMIC)
                }.await()
            }
        counselorBirthday =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_COUNSELOR_BIRTHDAY)
                }.await()
            }
        counselorPhoneNumber =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_COUNSELOR_NUMBER)
                }.await()
            }
        counselorLoginCounselor =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_LOGIN_COUNSELOR)
                }.await()
            }

        for ((i, _) in counselorLoginCounselor.withIndex()) {
            if (counselorLoginCounselor[i] == ArgumentsNAlogin.login) {
                binding.tiName.editText?.setText(counselorName[i])
                binding.tiSurname.editText?.setText(counselorSurname[i])
                binding.tiPatronymic.editText?.setText(counselorPatronymic[i])
                binding.tiBirthday.editText?.setText(counselorBirthday[i])
                binding.tiPhoneNumber.editText?.setText(counselorPhoneNumber[i])

            }
        }

        progressDialog.dismiss()
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