package com.example.singupactivity.ui.main.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Activity.NavigationActivity
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_BIRTHDAY
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_NUMBER
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_PATRONYMIC
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_COUNSELOR_SURNAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_ID_AUTHORIZATION_COUNSELOR
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CounselorFragment : Fragment() {

    lateinit var campDbManager: CampDbManager

    val nameCounselor: String = ""
    val surnameCounselor: String = ""
    val patronamycCounselor: String = ""
    val birthdayCounselor: String = ""
    val phoneNumberCounselor: String = ""
    val idAuthorization: String = ""

    var result: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_counselor, container, false)
        val tvNameCounselor = view.findViewById<TextView>(R.id.tvNameCounselor)
        val tvSurnameCounselor = view.findViewById<TextView>(R.id.tvSurnameCounselor)
        val tvPatronamycCounselor = view.findViewById<TextView>(R.id.tvPatronamycCounselor)
        val tvBirthdayCounselor = view.findViewById<TextView>(R.id.tvBirthdayCounselor)
        val tvPhoneNumberCounselor = view.findViewById<TextView>(R.id.tvPhoneNumberCounselor)
        val fabCounselor = view.findViewById<FloatingActionButton>(R.id.fabCounselor)


        val activity: NavigationActivity? = activity as NavigationActivity?
        result = activity?.getMyData()

        val nameCounselorList =
            result?.let {
                campDbManager.selectToTableCounselor(
                    const = COLUMN_NAME_COUNSELOR_NAME, title = it
                )
            }
        if (nameCounselorList != null) {
            for ((i, item) in nameCounselorList.withIndex()) {
                nameCounselorList[i] = nameCounselor
            }
        }
        val surnameCounselorList =
            result?.let {
                campDbManager.selectToTableCounselor(
                    const = COLUMN_NAME_COUNSELOR_SURNAME, title = it
                )
            }
        if (surnameCounselorList != null) {
            for ((i, item) in surnameCounselorList.withIndex()) {
                surnameCounselorList[i] = surnameCounselor
            }
        }
        val patronamycCounselorList =
            result?.let {
                campDbManager.selectToTableCounselor(
                    const = COLUMN_NAME_COUNSELOR_PATRONYMIC, title = it
                )
            }
        if (patronamycCounselorList != null) {
            for ((i, item) in patronamycCounselorList.withIndex()) {
                patronamycCounselorList[i] = patronamycCounselor
            }
        }
        val birthdayCounselorList =
            result?.let {
                campDbManager.selectToTableCounselor(
                    const = COLUMN_NAME_COUNSELOR_BIRTHDAY, title = it
                )
            }
        if (birthdayCounselorList != null) {
            for ((i, item) in birthdayCounselorList.withIndex()) {
                birthdayCounselorList[i] = birthdayCounselor
            }
        }
        val phoneNumberCounselorList =
            result?.let {
                campDbManager.selectToTableCounselor(
                    const = COLUMN_NAME_COUNSELOR_NUMBER, title = it
                )
            }
        if (phoneNumberCounselorList != null) {
            for ((i, item) in phoneNumberCounselorList.withIndex()) {
                phoneNumberCounselorList[i] = phoneNumberCounselor
            }
        }
        val idAuthorizationList =
            result?.let {
                campDbManager.selectToTableCounselor(
                    const = COLUMN_NAME_ID_AUTHORIZATION_COUNSELOR, title = it
                )
            }
        if (idAuthorizationList != null) {
            for ((i, item) in idAuthorizationList.withIndex()) {
                idAuthorizationList[i] = idAuthorization
            }
        }


        tvNameCounselor.text = nameCounselor
        tvSurnameCounselor.text = surnameCounselor
        tvPatronamycCounselor.text = patronamycCounselor
        tvBirthdayCounselor.text = birthdayCounselor
        tvPhoneNumberCounselor.text = phoneNumberCounselor

        fabCounselor.setOnClickListener {
            addAndEditCounselor(true)
        }

        return view

    }

    @SuppressLint("InflateParams")
    private fun addAndEditCounselor(isUpdate: Boolean) {

        val view = LayoutInflater.from(context).inflate(R.layout.add_counselor, null)

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val etNameCounselor = view.findViewById<TextView>(R.id.etNameCounselor)
        val etSurnameCounselor = view.findViewById<TextView>(R.id.etSurnameCounselor)
        val etPatronamycCounselor = view.findViewById<TextView>(R.id.etPatronamycCounselor)
        val etBirthdayCounselor = view.findViewById<TextView>(R.id.etBirthdayCounselor)
        val etPhoneNumberCounselor = view.findViewById<TextView>(R.id.etPhoneNumberCounselor)

        etNameCounselor.text = nameCounselor
        etSurnameCounselor.text = surnameCounselor
        etPatronamycCounselor.text = patronamycCounselor
        etBirthdayCounselor.text = birthdayCounselor
        etPhoneNumberCounselor.text = phoneNumberCounselor

        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "",
                DialogInterface.OnClickListener { dialogBox, id -> })
            .setNegativeButton(if (isUpdate) "Закрыть" else "",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (true) {

                        campDbManager.updateRawToTableCounselor(
                            counselorName = etNameCounselor.text.toString(),
                            counselorSurname = etSurnameCounselor.text.toString(),
                            counselorPatronymic = etPatronamycCounselor.text.toString(),
                            counselorBirthday = etBirthdayCounselor.text.toString(),
                            counselorNumber = etPhoneNumberCounselor.text.toString(),
                            id = idAuthorization
                        )
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        with(alertDialog) {
            show()
            getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
                if (TextUtils.isEmpty(etNameCounselor.text.toString())) {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                } else if (TextUtils.isEmpty(etSurnameCounselor.text.toString())) {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                } else if (TextUtils.isEmpty(etPatronamycCounselor.text.toString())) {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                } else if (TextUtils.isEmpty(etBirthdayCounselor.text.toString())) {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                } else if (TextUtils.isEmpty(etPhoneNumberCounselor.text.toString())) {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                } else {
                    dismiss()
                }
            })
        }

    }
}