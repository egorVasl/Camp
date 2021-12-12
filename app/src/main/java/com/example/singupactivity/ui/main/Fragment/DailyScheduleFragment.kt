package com.example.singupactivity.ui.main.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.ui.main.Adapter.ChildListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast

import android.text.TextUtils

import android.content.DialogInterface

import com.example.singupactivity.R
import android.app.AlertDialog

import android.widget.EditText

import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.singupactivity.ui.main.Adapter.DailyScheduleAdapter

import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT


class DailyScheduleFragment : Fragment() {

    lateinit var adapter: DailyScheduleAdapter
    lateinit var campDbManager: CampDbManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val rv = view.findViewById<RecyclerView>(R.id.rcDailySchedule)
        val fabDailySchedule = view.findViewById<FloatingActionButton>(R.id.fabDailySchedule)

        rv.layoutManager = GridLayoutManager(activity, 2)
        rv.itemAnimator = DefaultItemAnimator()

        fabDailySchedule.setOnClickListener {
            addAndEditSchedule(false, null, -1)
        }

        rv.adapter = adapter
        return view
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
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить",
                DialogInterface.OnClickListener { dialogBox, id -> })
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
            if (TextUtils.isEmpty(etName.text.toString())) {
                Toast.makeText(requireActivity(), R.string.enter_name_event, Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(etData.text.toString())) {
                Toast.makeText(requireActivity(), R.string.enter_data_event, Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(etTime.text.toString())) {
                Toast.makeText(requireActivity(), R.string.enter_time_event, Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
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

        adapter.updateDailySchedule(position,dailyScheduleDataClassUpdate)

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

}