package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddEditChildBinding
import com.example.singupactivity.ui.main.Adapter.ChildListAdapter
import com.example.singupactivity.ui.main.Data.ChildListDataClass
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.Fragment.BottomSheet.*
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.Child.ArgumentsChildDataClass
import com.example.singupactivity.ui.main.Objects.Child.ArgumentsChildFlag
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class
ChildListFragment : Fragment() {

    lateinit var adapter: ChildListAdapter
    lateinit var campDbManager: CampDbManager


    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableChild(
            const
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_CHILD) { _, _ ->
            addAndEditChildList(false, null, -1)
        }

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_CHILD) { _, _ ->
            if (adapter.childList.isEmpty())
                alert(
                    getString(R.string.no_data_to_save_title),
                    getString(R.string.no_data_to_save)
                )
            else
                importTextFile()
        }

        campDbManager = CampDbManager(act)
        adapter = ChildListAdapter(this@ChildListFragment)
        val nameChildList =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_CHILD_NAME)
                }.await()
            }

        val surnameChildList =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_CHILD_SURNAME)
                }.await()
            }

        val patronymicChildList =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_CHILD_PATRONYMIC)
                }.await()
            }
        val birthdayChildList =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_CHILD_BIRTHDAY)
                }.await()
            }
        val parentsPhoneNumberList =
            runBlocking {
                async {
                    getData(CampDbNameClass.COLUMN_NAME_PARENTS_NUMBER)
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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        val response = ChildListDataClass(
            nameChild = ArgumentsChildDataClass.nameChild,
            surnameChild = ArgumentsChildDataClass.surnameChild,
            patronamycChild = ArgumentsChildDataClass.patronymicChild,
            birthdayChild = ArgumentsChildDataClass.birthdayChild,
            parentsNumberChild = ArgumentsChildDataClass.parentsNumberChild
        )
        val responseUpdate =ChildListDataClass(
            nameChild = ArgumentsChildDataClass.nameChildUpdate,
            surnameChild = ArgumentsChildDataClass.surnameChildUpdate,
            patronamycChild = ArgumentsChildDataClass.patronymicChildUpdate,
            birthdayChild = ArgumentsChildDataClass.birthdayChildUpdate,
            parentsNumberChild = ArgumentsChildDataClass.parentsNumberChildUpdate
        )
        if (ArgumentsChildFlag.isUpdate) {
            adapter.let {
                for ((i, _) in it.childList.withIndex()) {
                    if (it.childList[i] == response)
                        it.updateChildList(i, responseUpdate)
                    it.notifyDataSetChanged()
                }
            }
        } else {
            adapter.let {
                for ((i, _) in it.childList.withIndex()) {
                    if (it.childList[i] == response)
                        it.removeChildList(i)
                    it.notifyDataSetChanged()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun importTextFile() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        val path = context?.getExternalFilesDir(null)

        val letDirectory = File(path, "Child")
        letDirectory.mkdirs()
        val file = File(letDirectory, "Child $currentDate.txt")
        try {
            FileOutputStream(file).use { stream ->
                adapter.let {
                    for ((i, _) in it.childList.withIndex()) {
                        stream.write("Ребёнок:${i + 1}\n".toByteArray())
                        stream.write("Имя: ${it.childList[i].nameChild}\n".toByteArray())
                        stream.write("Фамилия: ${it.childList[i].surnameChild}\n".toByteArray())
                        stream.write("Отчество: ${it.childList[i].patronamycChild}\n\n".toByteArray())
                        stream.write("Дата рождения: ${it.childList[i].birthdayChild}\n\n".toByteArray())
                        stream.write("Телефон родителей: ${it.childList[i].parentsNumberChild}\n\n".toByteArray())

                    }
                }
                stream.close()
                alert(
                    "Файл успешно создан!",
                    "Путь: $path/Child/Child $currentDate.txt"
                )
            }
        } catch (exe: IOException) {
            Toast.makeText(ctx, "Ошибка создания файла: $exe", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_child_list, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcChildList)
        val fabChildList = view.findViewById<FloatingActionButton>(R.id.fabChildList)

        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        fabChildList.setOnClickListener {
            ChildBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogChild")
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

            tvTitleChildList.text =
                if (!isUpdate) getString(R.string.add) else getString(R.string.edit)


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
                .setNegativeButton(
                    if (isUpdate) getString(R.string.delete) else getString(R.string.close)
                ) { dialogBox, _ ->
                    if (isUpdate) {
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
                            binding.tiNameChild.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiSurnameChild.editText?.text.toString()) -> {
                            binding.tiSurnameChild.error = getString(R.string.enter_child_surname)
                            binding.tiSurnameChild.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiPatronymicChild.editText?.text.toString()) -> {
                            binding.tiPatronymicChild.error =
                                getString(R.string.enter_child_patronymic)
                            binding.tiPatronymicChild.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiBirthdayChild.editText?.text.toString()) -> {
                            binding.tiBirthdayChild.error = getString(R.string.enter_child_birthday)
                            binding.tiBirthdayChild.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiParentsPhoneNumber.editText?.text.toString()) -> {
                            binding.tiParentsPhoneNumber.error =
                                getString(R.string.enter_parent_phone_number)
                            binding.tiParentsPhoneNumber.defaultHintTextColor =
                                ctx.getColorStateList(
                                    R.color.errorColor
                                )
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && childListDataClass != null) {
                        if (etNameUpdate != null) {

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

                    } else {
                        createChildList(
                            nameChild = tiNameChild.editText?.text.toString(),
                            surnameChild = tiSurnameChild.editText?.text.toString(),
                            patronymicChild = tiPatronymicChild.editText?.text.toString(),
                            birthdayChild = tiBirthdayChild.editText?.text.toString(),
                            parentsPhoneNumber = tiParentsPhoneNumber.editText?.text.toString()
                        )
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

    private fun createChildList(
        nameChild: String,
        surnameChild: String,
        patronymicChild: String,
        parentsPhoneNumber: String,
        birthdayChild: String
    ) {

        runBlocking {
            async {
                campDbManager.insertToTableChild(
                    childName = nameChild,
                    childSurname = surnameChild,
                    childPatronymic = patronymicChild,
                    childBirthday = birthdayChild,
                    parentsNumber = parentsPhoneNumber
                )
            }.await()
        }

        val childListDataClass = ChildListDataClass(
            nameChild = nameChild,
            surnameChild = surnameChild,
            patronamycChild = patronymicChild,
            birthdayChild = birthdayChild,
            parentsNumberChild = parentsPhoneNumber
        )

        adapter.addChildList(childListDataClass)

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