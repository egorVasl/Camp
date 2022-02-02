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
import com.example.singupactivity.ui.main.Fragment.*
import com.example.singupactivity.ui.main.Fragment.BottomSheet.DailyScheduleBottomSheetDialog
import com.example.singupactivity.ui.main.Fragment.BottomSheet.RATES_BOTTOM_REQUEST_KEY
import com.example.singupactivity.ui.main.Fragment.BottomSheet.RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSdataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDS
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class DailyScheduleFragment : Fragment() {
    lateinit var adapter: DailyScheduleAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView

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
        val eventTimeList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_TIME_EVENT)
                }.await()
            }
        val eventNameList =
            runBlocking {
                async {
                    getData(COLUMN_NAME_NAME_EVENT)
                }.await()
            }

        val eventDateList = runBlocking {
            async {
                getData(COLUMN_NAME_DATE_EVENT)
            }.await()
        }

        for ((i, _) in eventTimeList.withIndex()) {
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

        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        fabDailySchedule.setOnClickListener {

            DailyScheduleBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogDS")

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
        if (ArgumentsDSFlag.isUpdate) {
            adapter.let {
                for ((i, _) in it.dailyScheduleList.withIndex()) {
                    if (it.dailyScheduleList[i] == response)
                        it.updateDailySchedule(i, responseUpdate)
                }
            }
        } else {
            adapter.let {
                for ((i, _) in it.dailyScheduleList.withIndex()) {
                    if (it.dailyScheduleList[i] == response)
                        it.removeDailySchedule(i)
                }
            }
        }
    }

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
                    for ((i, _) in it.dailyScheduleList.withIndex()) {
                        stream.write("День:${i + 1}\n".toByteArray())
                        stream.write("Дата: ${it.dailyScheduleList[i].dateEvent}\n".toByteArray())
                        stream.write("Название мероприятия: ${it.dailyScheduleList[i].nameEvent}\n".toByteArray())
                        stream.write("Время: ${it.dailyScheduleList[i].timeEvent}\n\n".toByteArray())

                    }
                }
                stream.close()
                alert(
                    "Файл успешно создан!",
                    "Путь: $path/Daily schedule/Daily schedule $currentDate.txt"
                )
            }
        } catch (exe: IOException) {
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
            .setPositiveButton(
                if (isUpdate) "Обновить" else "Сохранить"
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

        runBlocking {
            async {
                campDbManager.deleteRawToTableDailySchedule(const)
            }.await()
        }


        adapter.removeDailySchedule(position)

    }

    private fun updateDailySchedule(
        timeEventUpdate: String,
        nameEventUpdate: String,
        dateEventUpdate: String,
        nameEventUpdatePosition: String,
        position: Int
    ) {

        runBlocking {
            async {
                campDbManager.updateRawToTableDailySchedule(
                    nameEvent = nameEventUpdate,
                    dateEvent = dateEventUpdate,
                    timeEvent = timeEventUpdate,
                    nameEventUpdatePosition = nameEventUpdatePosition
                )
            }.await()
        }


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
        runBlocking {
            async {
                campDbManager.insertToTableDailySchedule(
                    nameEvent = nameEventCreate,
                    dateEvent = dateEventCreate,
                    timeEvent = timeEventCreate
                )
            }.await()
        }


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

    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableDailySchedule(
            const
        )
    }
}