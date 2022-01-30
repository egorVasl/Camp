package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Adapter.ChildListAdapter
import com.example.singupactivity.ui.main.Data.ChildListDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.google.android.material.floatingactionbutton.FloatingActionButton


class
ChildListFragment : Fragment() {

    lateinit var adapter: ChildListAdapter
    lateinit var campDbManager: CampDbManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = ChildListAdapter(this@ChildListFragment)
        val nameChildList = campDbManager.selectToTableChild(CampDbNameClass.COLUMN_NAME_CHILD_NAME)
        val surnameChildList = campDbManager.selectToTableChild(CampDbNameClass.COLUMN_NAME_CHILD_SURNAME)
        val patronamycChildList = campDbManager.selectToTableChild(CampDbNameClass.COLUMN_NAME_CHILD_PATRONYMIC)
        val parentsPhoneNumberList = campDbManager.selectToTableChild(CampDbNameClass.COLUMN_NAME_PARENTS_NUMBER)
        val birthdayChildList = campDbManager.selectToTableChild(CampDbNameClass.COLUMN_NAME_CHILD_BIRTHDAY)

        for ((i, elm) in nameChildList.withIndex()) {
            adapter.addChildList(
                ChildListDataClass(
                    nameChild = nameChildList[i],
                    surnameChild = surnameChildList[i],
                    birthdayChild =  birthdayChildList[i],
                    patronamycChild = patronamycChildList[i],
                    parentsNumberChild = parentsPhoneNumberList[i]
                )
            )

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_child_list, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcChildList)
        val fabChildList = view.findViewById<FloatingActionButton>(R.id.fabChildList)

        rv.layoutManager = LinearLayoutManager(activity)
        rv.itemAnimator = DefaultItemAnimator()

        fabChildList.setOnClickListener {
            addAndEditChildList(false, null, -1)
        }
        rv.adapter = adapter

        return view
    }

    @SuppressLint("InflateParams")
    fun addAndEditChildList(
        isUpdate: Boolean,
        childListDataClass: ChildListDataClass?,
        position: Int
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.add_edit_child, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val tvTitleChildList = view.findViewById<TextView>(R.id.tvTitleChildList)
        val etNameChild = view.findViewById<EditText>(R.id.etNameChild)
        val etSurnameChild = view.findViewById<EditText>(R.id.etSurnameChild)
        val etPatronamycChild = view.findViewById<EditText>(R.id.etPatronamycChild)
        val etBirthdayChild = view.findViewById<EditText>(R.id.etBirthdayChild)
        val etParentsPhoneNumber = view.findViewById<EditText>(R.id.etParentsPhoneNumber)
        val etNameUpdate: String? = childListDataClass?.nameChild

        tvTitleChildList.text = if (!isUpdate) "Добавить ребёнка" else "Редактировать"

        if (isUpdate && childListDataClass != null) {
            etNameChild.setText(childListDataClass.nameChild)
            etSurnameChild.setText(childListDataClass.surnameChild)
            etPatronamycChild.setText(childListDataClass.patronamycChild)
            etBirthdayChild.setText(childListDataClass.birthdayChild)
            etParentsPhoneNumber.setText(childListDataClass.parentsNumberChild)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить",
                DialogInterface.OnClickListener { dialogBox, id -> })
            .setNegativeButton(if (isUpdate) "Удалить ребёнка" else "Закрыть",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (isUpdate) {
                        deleteChildList(
                            position = position,
                            const = etNameChild.text.toString()

                        )
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            when {
                TextUtils.isEmpty(etNameChild.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etSurnameChild.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etPatronamycChild.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etBirthdayChild.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etParentsPhoneNumber.text.toString()) -> {
                    Toast.makeText(requireActivity(),R.string.no_dat, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                else -> {
                    alertDialog.dismiss()
                }
            }
            if (isUpdate && childListDataClass != null) {
                if (etNameUpdate != null) {
                    updateChildList(
                        nameChildUpdate = etNameChild.text.toString(),
                        surnameChildUpdate = etSurnameChild.text.toString(),
                        patronamycChildUpdate = etPatronamycChild.text.toString(),
                        birthdayChildUpdate = etBirthdayChild.text.toString(),
                        parentsPhoneNumberUpdate = etParentsPhoneNumber.text.toString(),
                        nameChildUpdatePosition = etNameUpdate,
                        position = position
                    )
                }

            } else {
                createChildList(
                    nameChild = etNameChild.text.toString(),
                    surnameChild = etSurnameChild.text.toString(),
                    patronamycChild = etPatronamycChild.text.toString(),
                    birthdayChild = etBirthdayChild.text.toString(),
                    parentsPhoneNumber = etParentsPhoneNumber.text.toString()
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteChildList(const: String, position: Int) {

        campDbManager.deleteRawToTableChild(const)

        adapter.removeChildList(position)

    }

    private fun updateChildList(
        nameChildUpdate: String,
        surnameChildUpdate: String,
        patronamycChildUpdate: String,
        parentsPhoneNumberUpdate: String,
        birthdayChildUpdate: String,
        nameChildUpdatePosition: String,
        position: Int
    ) {
        campDbManager.updateRawToTableChild(
            nameChildUpdate = nameChildUpdate,
            surnameChildUpdate = surnameChildUpdate,
            patronamycChildUpdate = patronamycChildUpdate,
            parentsPhoneNumberUpdate = parentsPhoneNumberUpdate,
            birthdayChildUpdate = birthdayChildUpdate,
            nameChildUpdatePosition = nameChildUpdatePosition
        )

        val childListDataClass = ChildListDataClass(
            nameChild = nameChildUpdate,
            surnameChild = surnameChildUpdate,
            patronamycChild = patronamycChildUpdate,
            birthdayChild = birthdayChildUpdate,
            parentsNumberChild = parentsPhoneNumberUpdate

        )

        adapter.updateChildList(position, childListDataClass)

    }

    private fun createChildList(
        nameChild: String,
        surnameChild: String,
        patronamycChild: String,
        parentsPhoneNumber: String,
        birthdayChild: String
    ) {
        campDbManager.insertToTableChild(
            childName = nameChild,
            childSurname = surnameChild,
            childPatronymic = patronamycChild,
            childBirthday = birthdayChild,
            parentsNumber = parentsPhoneNumber
        )

        val childListDataClass = ChildListDataClass(
            nameChild = nameChild,
            surnameChild = surnameChild,
            patronamycChild = patronamycChild,
            birthdayChild = birthdayChild,
            parentsNumberChild = parentsPhoneNumber
        )

        adapter.addChildList(childListDataClass)

    }

}