package com.example.singupactivity.ui.main.Fragment.Search

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddDailyScheduleBinding
import com.example.singupactivity.databinding.AddEditChildBinding
import com.example.singupactivity.ui.main.Adapter.SearchAdapters.SearchChildAdapter
import com.example.singupactivity.ui.main.Data.ChildListDataClass
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_BIRTHDAY
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_PATRONYMIC
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_CHILD_SURNAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_PARENTS_NUMBER
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Arguments
import com.example.singupactivity.ui.main.Objects.Child.ArgumentsChildDataClass
import com.example.singupactivity.ui.main.Objects.Child.ArgumentsChildFlag
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class SearchChildFragment : Fragment() {
    lateinit var adapter: SearchChildAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText


    companion object {
        @JvmStatic
        fun newInstance() =
            SearchChildFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
        adapter = SearchChildAdapter(this@SearchChildFragment)


    }

    private fun getData(const: String, searchText: String): ArrayList<String> {
        return  campDbManager.selectToTableChild(
            const,
            searchText,
            Arguments.arg
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        rv = view.findViewById(R.id.rcDailyScheduleSearch)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter
        val ibSearch = view.findViewById<ImageButton>(R.id.imageButtonSearch)
        etCardSearch = view.findViewById(R.id.etSearchDailySchedule)

        ibSearch?.setOnClickListener {
            if (Arguments.arg.isNotBlank()) {
                val searchText = etCardSearch.text.toString()
                if (searchText.isNotEmpty()) {
                    adapter.childList.clear()
                    val nameChildList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_CHILD_NAME, searchText)
                            }.await()
                        }

                    val surnameChildList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_CHILD_SURNAME, searchText)
                            }.await()
                        }

                    val patronymicChildList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_CHILD_PATRONYMIC, searchText)
                            }.await()
                        }
                    val birthdayChildList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_CHILD_BIRTHDAY, searchText)
                            }.await()
                        }
                    val parentsPhoneNumberList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_PARENTS_NUMBER, searchText)
                            }.await()
                        }

                    for ((i, _) in nameChildList.withIndex()) {
                        adapter.addChildList(
                            ChildListDataClass(
                                nameChild = nameChildList[i],
                                surnameChild = surnameChildList[i],
                                birthdayChild =  birthdayChildList[i],
                                patronamycChild = patronymicChildList[i],
                                parentsNumberChild = parentsPhoneNumberList[i]
                            )
                        )

                    }
                    rv.adapter = adapter
                    if (adapter.childList.isEmpty()) {
                        alert(
                            getString(R.string.no_data_after_search_title),
                            getString(R.string.no_data_after_search)
                        )
                        etCardSearch.text.clear()
                    }
                    Arguments.arg = ""

                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.no_data_to_search,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                alert(
                    getString(R.string.no_argument_selected_title),
                    getString(R.string.no_argument_selected)
                )
            }
        }

        return view

    }

    @SuppressLint("InflateParams")
    fun addAndEditChildList(
        isUpdate: Boolean,
        childListDataClass: ChildListDataClass?,
        position: Int
    ) {
        val binding: AddEditChildBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.add_edit_child, null, false
        )
        val etNameUpdate: String? = childListDataClass?.nameChild

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(act)
        alertDialogBuilderUserInput.setView(binding.root)

        with(binding) {

            tvTitleChildList.text = if (!isUpdate) getString(R.string.add) else getString(R.string.edit)


            if (isUpdate && childListDataClass != null) {
                tiNameChild.editText?.setText(childListDataClass.nameChild)
                tiSurnameChild.editText?.setText(childListDataClass.surnameChild)
                tiPatronymicChild.editText?.setText(childListDataClass.patronamycChild)
                tiBirthdayChild.editText?.setText(childListDataClass.birthdayChild)
                tiParentsPhoneNumber.editText?.setText(childListDataClass.parentsNumberChild)
            }
            alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(
                    if (isUpdate) getString(R.string.update) else getString(R.string.save)
                ) { _, _ -> }
                .setNegativeButton(if (isUpdate) getString(R.string.delete) else getString(R.string.close)
                ) { dialogBox, _ ->
                    if (isUpdate) {
                        ArgumentsChildFlag.isUpdate = false
                        ArgumentsChildDataClass.nameChild = adapter.childList[position].nameChild
                        ArgumentsChildDataClass.surnameChild = adapter.childList[position].surnameChild
                        ArgumentsChildDataClass.patronymicChild = adapter.childList[position].patronamycChild
                        ArgumentsChildDataClass.birthdayChild = adapter.childList[position].birthdayChild
                        ArgumentsChildDataClass.parentsNumberChild = adapter.childList[position].parentsNumberChild

                        deleteChildList(
                                position = position,
                                const = tiNameChild.editText?.text.toString()

                            )
                        } else {
                            dialogBox.cancel()
                        }
                    }

            val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
            alertDialog.window?.decorView?.setBackgroundResource(R.drawable.add_dialog_shape)
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(View.OnClickListener {
                    when {
                        TextUtils.isEmpty(tiNameChild.editText?.text.toString()) -> {
                            binding.tiNameChild.error = getString(R.string.enter_child_name)
                            binding.tiNameChild.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiSurnameChild.editText?.text.toString()) -> {
                            binding.tiSurnameChild.error = getString(R.string.enter_child_surname)
                            binding.tiSurnameChild.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiPatronymicChild.editText?.text.toString()) -> {
                            binding.tiPatronymicChild.error = getString(R.string.enter_child_patronymic)
                            binding.tiPatronymicChild.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiBirthdayChild.editText?.text.toString()) -> {
                            binding.tiBirthdayChild.error = getString(R.string.enter_child_birthday)
                            binding.tiBirthdayChild.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiParentsPhoneNumber.editText?.text.toString()) -> {
                            binding.tiParentsPhoneNumber.error = getString(R.string.enter_parent_phone_number)
                            binding.tiParentsPhoneNumber.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && childListDataClass != null) {
                        if (etNameUpdate != null) {
                            ArgumentsChildFlag.isUpdate = true
                            ArgumentsChildDataClass.nameChild = adapter.childList[position].nameChild
                            ArgumentsChildDataClass.surnameChild = adapter.childList[position].surnameChild
                            ArgumentsChildDataClass.patronymicChild = adapter.childList[position].patronamycChild
                            ArgumentsChildDataClass.birthdayChild = adapter.childList[position].birthdayChild
                            ArgumentsChildDataClass.parentsNumberChild = adapter.childList[position].parentsNumberChild
                            ArgumentsChildDataClass.nameChildUpdate = tiNameChild.editText?.text.toString()
                            ArgumentsChildDataClass.surnameChildUpdate = tiSurnameChild.editText?.text.toString()
                            ArgumentsChildDataClass.patronymicChildUpdate = tiPatronymicChild.editText?.text.toString()
                            ArgumentsChildDataClass.birthdayChildUpdate = tiBirthdayChild.editText?.text.toString()
                            ArgumentsChildDataClass.parentsNumberChildUpdate = tiParentsPhoneNumber.editText?.text.toString()

                            updateChildList(
                                nameChildUpdate = tiNameChild.editText?.text.toString(),
                                surnameChildUpdate = tiSurnameChild.editText?.text.toString(),
                                patronymicChildUpdate = tiPatronymicChild.editText?.text.toString(),
                                birthdayChildUpdate = tiBirthdayChild.editText?.text.toString(),
                                parentsPhoneNumberUpdate = tiParentsPhoneNumber.editText?.text.toString(),
                                nameChildUpdatePosition = etNameUpdate,
                                position = position
                            )
                        }

                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteChildList(const: String, position: Int) {

        runBlocking {
            async {
                campDbManager.deleteRawToTableChild(const)
            }.await()
        }

        adapter.removeChildList(position)

    }

    private fun updateChildList(
        nameChildUpdate: String,
        surnameChildUpdate: String,
        patronymicChildUpdate: String,
        parentsPhoneNumberUpdate: String,
        birthdayChildUpdate: String,
        nameChildUpdatePosition: String,
        position: Int
    ) {
        runBlocking {
            async {
                campDbManager.updateRawToTableChild(
                    nameChildUpdate = nameChildUpdate,
                    surnameChildUpdate = surnameChildUpdate,
                    patronamycChildUpdate = patronymicChildUpdate,
                    parentsPhoneNumberUpdate = parentsPhoneNumberUpdate,
                    birthdayChildUpdate = birthdayChildUpdate,
                    nameChildUpdatePosition = nameChildUpdatePosition
                )
            }.await()
        }

        val childListDataClass = ChildListDataClass(
            nameChild = nameChildUpdate,
            surnameChild = surnameChildUpdate,
            patronamycChild = patronymicChildUpdate,
            birthdayChild = birthdayChildUpdate,
            parentsNumberChild = parentsPhoneNumberUpdate

        )

        adapter.updateChildList(position, childListDataClass)

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