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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Adapter.EventsAdapter
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class EventsFragment : Fragment() {

    lateinit var adapter: EventsAdapter
    lateinit var campDbManager: CampDbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!
        adapter = EventsAdapter(this@EventsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_events, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcEvents)
        val fabEvents = view.findViewById<FloatingActionButton>(R.id.fabEvents)

        rv.layoutManager = GridLayoutManager(activity, 2)
        rv.itemAnimator = DefaultItemAnimator()

        fabEvents.setOnClickListener {
            addAndEditEvents(false, null, -1)
        }

        rv.adapter = adapter
        return view
    }

    fun addAndEditEvents(
        isUpdate: Boolean,
        eventsDataClass: EventsDataClass?,
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
        val etNameUpdate: String? = eventsDataClass?.eventName

        newDayTitle.text = if (!isUpdate) "Добавить" else "Редактировать"

        if (isUpdate && eventsDataClass != null) {
            etName.setText(eventsDataClass.eventName)
            etData.setText(eventsDataClass.date)
            etTime.setText(eventsDataClass.time)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) "Обновить" else "Сохранить",
                DialogInterface.OnClickListener { dialogBox, id -> })
            .setNegativeButton(if (isUpdate) "Удалить" else "Закрыть",
                DialogInterface.OnClickListener { dialogBox, id ->
                    if (isUpdate) {
                        deleteEvents(
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
            if (isUpdate && eventsDataClass != null) {
                if (etNameUpdate != null) {
                    updateEvents(
                        eventNameUpdate = etName.text.toString(),
                        dateUpdate = etData.text.toString(),
                        timeUpdate = etTime.text.toString(),
                        eventNameUpdatePosition = etNameUpdate,
                        position = position
                    )
                }

            } else {
                createEvents(
                    eventName = etName.text.toString(),
                    date = etData.text.toString(),
                    time = etTime.text.toString()
                )
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteEvents(const: String, position: Int) {

//        campDbManager.deleteRawToTableDailySchedule(const)

        adapter.removeEvents(position)

    }

    private fun updateEvents(
        timeUpdate: String,
        eventNameUpdate: String,
        dateUpdate: String,
        eventNameUpdatePosition: String,
        position: Int
    ) {
//        campDbManager.updateRawToTableDailySchedule(
//            nameEvent = eventNameUpdate,
//            dateEvent = dateUpdate,
//            timeEvent = timeUpdate,
//            nameEventUpdatePosition = eventNameUpdatePosition
//        )

        val eventsDataClassUpdate = EventsDataClass(
            time = timeUpdate,
            eventName = eventNameUpdate,
            date = dateUpdate
        )

        adapter.updateEvents(position,eventsDataClassUpdate)

    }

    private fun createEvents(
        time: String,
        eventName: String,
        date: String
    ) {
//        campDbManager.insertToTableDailySchedule(
//            nameEvent = nameEventCreate,
//            dateEvent = dateEventCreate,
//            timeEvent = timeEventCreate
//        )

        val eventsDataClassUpdate = EventsDataClass(
            time = time,
            eventName = eventName,
            date = date
        )

        adapter.addEvents(eventsDataClassUpdate)

    }



}