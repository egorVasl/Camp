package com.example.singupactivity.ui.main.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.ui.main.Adapter.DailyScheduleAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast

import android.text.TextUtils

import android.content.DialogInterface

import com.example.singupactivity.R
import android.annotation.SuppressLint
import android.app.AlertDialog

import android.widget.EditText

import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass


class DailyScheduleFragment : Fragment() {

    lateinit var adapter : DailyScheduleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DailyScheduleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_daily_schedule, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcDailySchedule)
        val fabDailySchedule = view.findViewById<FloatingActionButton>(R.id.fabDailySchedule)

        rv.layoutManager = GridLayoutManager(requireContext(), 3)
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter

        fabDailySchedule.setOnClickListener{
            addAndEditCars(false, null, -1)
        }

        return view
    }

    @SuppressLint("InflateParams")
    fun addAndEditCars(isUpdate: Boolean, dailyScheduleDataClass: DailyScheduleDataClass?, position: Int) {
//        val layoutInflaterAndroid =
//            LayoutInflater.from(getApplicationContext())
//
//        val view: View = layoutInflaterAndroid.inflate(R.layout.add_daily_schedule, null)

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.daily_scedule_list_item, null)


        val alertDialogBuilderUserInput: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialogBuilderUserInput.setView(view)

        var newDayTitle = view.findViewById<TextView>(R.id.newDayTitle)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etData = view.findViewById<EditText>(R.id.etData)
        val etTime = view.findViewById<EditText>(R.id.etTime)


        newDayTitle.text = if (isUpdate) "Добавить" else "Редактировать"

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
//                        deleteCar(dailyScheduleDataClass, position) delite
                    } else {
                        dialogBox.cancel()
                    }
                })

        val alertDialog: AlertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(etName.text.toString())) {
                Toast.makeText(activity, R.string.enter_name_event, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(etData.text.toString())) {
                Toast.makeText(activity,  R.string.enter_data_event, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else if (TextUtils.isEmpty(etTime.text.toString())) {
                Toast.makeText(activity, R.string.enter_time_event, Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
            }
            if (isUpdate && dailyScheduleDataClass != null) {
               // updateCar(nameEditText.text.toString(), priceEditText.text.toString(), position)
            } else {
               // createCar(nameEditText.text.toString(), priceEditText.text.toString())
            }
        })
    }


}