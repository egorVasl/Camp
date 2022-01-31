package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.text.TextUtils
import android.content.DialogInterface
import com.example.singupactivity.R
import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import android.widget.*
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.*
import com.example.singupactivity.ui.main.Adapter.DailyScheduleAdapter
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.singupactivity.ui.main.Fragment.*
import com.example.singupactivity.ui.main.Fragment.BottomSheet.DailyScheduleBottomSheetDialog
import com.example.singupactivity.ui.main.Fragment.BottomSheet.RATES_BOTTOM_REQUEST_KEY
import com.example.singupactivity.ui.main.Fragment.BottomSheet.RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSdataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DailyScheduleFragment : Fragment() {
    lateinit var adapter: DailyScheduleAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var cardSearch: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY) { _, _ ->
            addAndEditSchedule(false, null, -1)
        }

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF) { _, _ ->
            importTextFile()
        }

        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = DailyScheduleAdapter(this@DailyScheduleFragment)
        val eventTimeList = campDbManager.selectToTableDailySchedule(COLUMN_NAME_TIME_EVENT)
        val eventNameList = campDbManager.selectToTableDailySchedule(COLUMN_NAME_NAME_EVENT)
        val eventDateList = campDbManager.selectToTableDailySchedule(COLUMN_NAME_DATE_EVENT)
        for ((i, elm) in eventTimeList.withIndex()) {
            adapter.addDailySchedule(
                DailyScheduleDataClass(
                    eventTimeList[i],
                    eventNameList[i], eventDateList[i]
                )
            )

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_daily_schedule, container, false)
        rv = view.findViewById(R.id.rcDailySchedule)
        val fabDailySchedule = view.findViewById<FloatingActionButton>(R.id.fabDailySchedule)

        rv.layoutManager = LinearLayoutManager(activity)
        rv.itemAnimator = DefaultItemAnimator()

        fabDailySchedule.setOnClickListener {

            DailyScheduleBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialog")

        }

        rv.adapter = adapter
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        val response = DailyScheduleDataClass(
            timeEvent = ArgumentDSdataClass.timeEvent,
            nameEvent = ArgumentDSdataClass.nameEvent,
            dateEvent = ArgumentDSdataClass.dateEvent
        )
        val responseUpdate = DailyScheduleDataClass(
            timeEvent = ArgumentDSdataClass.timeEventUpdate,
            nameEvent = ArgumentDSdataClass.nameEventUpdate,
            dateEvent = ArgumentDSdataClass.dateEventUpdate
        )
        if(ArgumentsDSFlag.isUpdate){
            adapter.let {
                for ((i, elm) in it.dailyScheduleList.withIndex()) {
                    if (it.dailyScheduleList[i] == response)
                        it.updateDailySchedule(i, responseUpdate)
                }
            }
        } else {
            adapter.let {
                for ((i, elm) in it.dailyScheduleList.withIndex()) {
                    if (it.dailyScheduleList[i] == response)
                        it.removeDailySchedule(i)
                }
            }
        }
    }

//    private fun searchSchedule(selectionArg: String) {
//            if (adapter.dailyScheduleList.size > 0) {
//                cardSearch = requireView().findViewById(R.id.cardSearch)
//                if (!isChecked) {
//                    cardSearch.isVisible = false
//                    val layoutParams = rv.layoutParams as ConstraintLayout.LayoutParams
//                    layoutParams.topMargin = context?.let { 0.toDp(it) }!!
//                    rv.layoutParams = layoutParams
//                } else {
//                    cardSearch.isVisible = true
//                    val layoutParams = rv.layoutParams as ConstraintLayout.LayoutParams
//                    layoutParams.topMargin = context?.let { 70.toDp(it) }!!
//                    rv.layoutParams = layoutParams
//
////                    selectionArgs()
//                }
//            } else {
//                if (isChecked){ alert(R.string.empty_list_text)
//                    setFragmentResult(IS_CHECKED_TRUE_REQUEST_KEY, bundleOf(IS_CHECKED_TRUE_BUNDLE_KEY to false))
//               }
//            }
//
//    }

//    private fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
//    ).toInt()


//    private fun selectionArgs() {
//
//        val view = LayoutInflater.from(context).inflate(R.layout.selection_arguments_layout, null)
//
//        val alertDialogBuilderUserInput: AlertDialog.Builder =
//            AlertDialog.Builder(requireActivity())
//        alertDialogBuilderUserInput.setView(view)
//
//        val checkNameEvent = view.findViewById<CheckBox>(R.id.checkNameEvent)
//        val checkTimeEvent = view.findViewById<CheckBox>(R.id.checkTimeEvent)
//        val checkDateEvent = view.findViewById<CheckBox>(R.id.checkDateEvent)
//
//        alertDialogBuilderUserInput
//            .setCancelable(false)
//            .setNegativeButton(R.string.cancellation){dialogBox,_->
//                cardSearch.isVisible = false
//                val layoutParams = rv.layoutParams as ConstraintLayout.LayoutParams
//                layoutParams.topMargin = context?.let { 0.toDp(it) }!!
//                rv.layoutParams = layoutParams
//                setFragmentResult(IS_CHECKED_TRUE_REQUEST_KEY, bundleOf(IS_CHECKED_TRUE_BUNDLE_KEY to false))
//                dialogBox.cancel()
//            }
//            .setPositiveButton(R.string.contin) { _, _ -> }
//        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
//        alertDialog.show()
//
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
//            ArgumentsDS.arg = when  {
//                checkNameEvent.isChecked -> COLUMN_NAME_NAME_EVENT
//                checkTimeEvent.isChecked -> COLUMN_NAME_TIME_EVENT
//                checkDateEvent.isChecked -> COLUMN_NAME_DATE_EVENT
//                else ->""
//            }
//             if(!checkNameEvent.isChecked && !checkTimeEvent.isChecked && !checkDateEvent.isChecked){
//                    Toast.makeText(requireActivity(), R.string.select_type_search, Toast.LENGTH_SHORT)
//                        .show()
//                    return@OnClickListener
//                }
//                else {
//                 searchSchedule()
//                    alertDialog.dismiss()
//                }
//        })
//    }



    @SuppressLint("SimpleDateFormat")
    private fun importTextFile() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        val path = context?.getExternalFilesDir(null)

        val letDirectory = File(path, "Daily schedule")
        letDirectory.mkdirs()
        val file = File(letDirectory, "Daily schedule $currentDate.txt")
        try {
            FileOutputStream(file).use { stream ->
                adapter.let {
                    for ((i, elm) in it.dailyScheduleList.withIndex()) {
                        stream.write("День:${i+1}\n".toByteArray())
                        stream.write("Дата: ${it.dailyScheduleList[i].dateEvent}\n".toByteArray())
                        stream.write("Название мероприятия: ${it.dailyScheduleList[i].nameEvent}\n".toByteArray())
                        stream.write("Время: ${it.dailyScheduleList[i].timeEvent}\n\n".toByteArray())

                    }
                }
                stream.close()
                alert("Файл успешно создан!", "Путь: $path/Daily schedule/Daily schedule $currentDate.txt")
            }
        }catch (exe: IOException){
            Toast.makeText(ctx, "Ошибка создания файла: $exe", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("InflateParams")
    fun addAndEditSchedule(
        isUpdate: Boolean,
        dailyScheduleDataClass: DailyScheduleDataClass?,
        position: Int
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.add_daily_schedule, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(requireActivity())
        alertDialogBuilderUserInput.setView(view)

        val newDayTitle = view.findViewById<TextView>(R.id.newDayTitle)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etData = view.findViewById<EditText>(R.id.etData)
        val etTime = view.findViewById<EditText>(R.id.etTime)
        val etNameUpdate: String? = dailyScheduleDataClass?.nameEvent

        newDayTitle.text = if (!isUpdate) "Добавить" else "Редактировать"

        if (isUpdate && dailyScheduleDataClass != null) {
            etName.setText(dailyScheduleDataClass.nameEvent)
            etData.setText(dailyScheduleDataClass.dateEvent)
            etTime.setText(dailyScheduleDataClass.timeEvent)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить"
            ) { _, _ -> }
            .setNegativeButton(if (isUpdate) "Удалить" else "Закрыть",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (isUpdate) {
                        deleteDailySchedule(
                            position = position,
                            const = etName.text.toString()
                        )
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            when {
                TextUtils.isEmpty(etName.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.enter_name_event, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etData.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.enter_data_event, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                TextUtils.isEmpty(etTime.text.toString()) -> {
                    Toast.makeText(requireActivity(), R.string.enter_time_event, Toast.LENGTH_SHORT)
                        .show()
                    return@OnClickListener
                }
                else -> {
                    alertDialog.dismiss()
                }
            }
            if (isUpdate && dailyScheduleDataClass != null) {
                if (etNameUpdate != null) {
                    updateDailySchedule(
                        nameEventUpdate = etName.text.toString(),
                        dateEventUpdate = etData.text.toString(),
                        timeEventUpdate = etTime.text.toString(),
                        nameEventUpdatePosition = etNameUpdate,
                        position = position
                    )
                }

            } else {
                createDailySchedule(
                    nameEventCreate = etName.text.toString(),
                    dateEventCreate = etData.text.toString(),
                    timeEventCreate = etTime.text.toString()
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteDailySchedule(const: String, position: Int) {

        campDbManager.deleteRawToTableDailySchedule(const)

        adapter.removeDailySchedule(position)

    }

    private fun updateDailySchedule(
        timeEventUpdate: String,
        nameEventUpdate: String,
        dateEventUpdate: String,
        nameEventUpdatePosition: String,
        position: Int
    ) {
        campDbManager.updateRawToTableDailySchedule(
            nameEvent = nameEventUpdate,
            dateEvent = dateEventUpdate,
            timeEvent = timeEventUpdate,
            nameEventUpdatePosition = nameEventUpdatePosition
        )

        val dailyScheduleDataClassUpdate = DailyScheduleDataClass(
            timeEvent = timeEventUpdate,
            nameEvent = nameEventUpdate,
            dateEvent = dateEventUpdate
        )

        adapter.updateDailySchedule(position, dailyScheduleDataClassUpdate)

    }

    private fun createDailySchedule(
        timeEventCreate: String, nameEventCreate: String,
        dateEventCreate: String
    ) {
        campDbManager.insertToTableDailySchedule(
            nameEvent = nameEventCreate,
            dateEvent = dateEventCreate,
            timeEvent = timeEventCreate
        )

        val dailyScheduleDataClassCreate = DailyScheduleDataClass(
            timeEvent = timeEventCreate,
            nameEvent = nameEventCreate,
            dateEvent = dateEventCreate
        )

        adapter.addDailySchedule(dailyScheduleDataClassCreate)

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