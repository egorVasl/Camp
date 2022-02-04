package com.example.singupactivity.ui.main.Fragment.Search

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddDailyScheduleBinding
import com.example.singupactivity.ui.main.Adapter.EventsAdapter
import com.example.singupactivity.ui.main.Adapter.SearchDSAdapter
import com.example.singupactivity.ui.main.Adapter.SearchEventsAdapter
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSdataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDS
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class SearchFragmentEvents : Fragment() {

    lateinit var adapter: SearchEventsAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText



    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragmentEvents()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!

    }

    private fun getData(const: String, searchText: String): ArrayList<String> {
        return  campDbManager.selectToTableWeekEvent(
            const,
            searchText,
            ArgumentsDS.arg
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
        adapter = SearchEventsAdapter(this@SearchFragmentEvents)
        rv.adapter = adapter
        val ibSearch = view.findViewById<ImageButton>(R.id.imageButtonSearch)
        etCardSearch = view.findViewById(R.id.etSearchDailySchedule)

        ibSearch?.setOnClickListener {
            if (ArgumentsDS.arg.isNotBlank()) {
                val searchText = etCardSearch.text.toString()
                if (searchText.isNotEmpty()) {
                    adapter.eventsList.clear()
                    val eventTimeList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_TIME, searchText)
                            }.await()
                        }

                    val eventNameList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_EVENT_NAME, searchText)
                            }.await()
                        }

                    val eventDateList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_DATE, searchText)
                            }.await()
                        }

                    for ((i, _) in eventTimeList.withIndex()) {
                        adapter.addEvents(
                            EventsDataClass(eventDateList[i],
                                eventTimeList[i],
                                eventNameList[i]

                            )
                        )

                    }
                    rv.adapter = adapter
                    if (adapter.eventsList.isEmpty()) {
                        alert(
                            getString(R.string.no_data_after_search_title),
                            getString(R.string.no_data_after_search)
                        )
                        etCardSearch.text.clear()
                    }
                    ArgumentsDS.arg = ""

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
    fun addAndEditEvents(
        isUpdate: Boolean,
        eventsDataClass: EventsDataClass?,
        position: Int
    ) {
        val binding: AddDailyScheduleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.add_daily_schedule, null, false
        )
        val etNameUpdate: String? = eventsDataClass?.eventName

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(act)
        alertDialogBuilderUserInput.setView(binding.root)

        with(binding) {

            newDayTitle.text = if (!isUpdate) getString(R.string.add) else getString(R.string.edit)

            if (isUpdate && eventsDataClass != null) {
                tiName.editText?.setText(eventsDataClass.eventName)
                tiDate.editText?.setText(eventsDataClass.date)
                tiTime.editText?.setText(eventsDataClass.time)
            }
            alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(
                    if (isUpdate) getString(R.string.update) else getString(R.string.save)
                ) { _, _ -> }
                .setNegativeButton(if (isUpdate) getString(R.string.delete) else getString(R.string.close)
                ) { dialogBox, _ ->
                    if (isUpdate) {
                        ArgumentsDSFlag.isUpdate = false
                        ArgumentDSdataClass.nameEvent = adapter.eventsList[position].eventName
                        ArgumentDSdataClass.timeEvent = adapter.eventsList[position].time
                        ArgumentDSdataClass.dateEvent = adapter.eventsList[position].date
                        deleteEvents(
                            position = position,
                            const = tiName.editText?.text.toString()
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
                        TextUtils.isEmpty(tiName.editText?.text.toString()) -> {
                            binding.tiName.error = getString(R.string.enter_name_event)
                            binding.tiName.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiDate.editText?.text.toString()) -> {
                            binding.tiDate.error = getString(R.string.enter_data_event)
                            binding.tiDate.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiTime.editText?.text.toString()) -> {
                            binding.tiTime.error = getString(R.string.enter_time_event)
                            binding.tiTime.defaultHintTextColor = ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && eventsDataClass != null) {
                        if (etNameUpdate != null) {
                            ArgumentsDSFlag.isUpdate = true
                            ArgumentDSdataClass.nameEvent = adapter.eventsList[position].eventName
                            ArgumentDSdataClass.timeEvent = adapter.eventsList[position].time
                            ArgumentDSdataClass.dateEvent = adapter.eventsList[position].date
                            ArgumentDSdataClass.nameEventUpdate = tiName.editText?.text.toString()
                            ArgumentDSdataClass.timeEventUpdate = tiTime.editText?.text.toString()
                            ArgumentDSdataClass.dateEventUpdate = tiDate.editText?.text.toString()
                            updateEvents(
                                eventNameUpdate = tiName.editText?.text.toString(),
                                dateUpdate = tiDate.editText?.text.toString(),
                                timeUpdate = tiTime.editText?.text.toString(),
                                eventNameUpdatePosition = etNameUpdate,
                                position = position
                            )
                        }
                    }
                })

    }
    }
        @SuppressLint("NotifyDataSetChanged")
        private fun deleteEvents(const: String, position: Int) {

            runBlocking {
                async {
                    campDbManager.deleteRawToTableWeekEvents(const)
                }.await()
            }

            adapter.removeEvents(position)

        }

        private fun updateEvents(
            timeUpdate: String,
            eventNameUpdate: String,
            dateUpdate: String,
            eventNameUpdatePosition: String,
            position: Int
        ) {
            runBlocking {
                async {
                    campDbManager.updateRawToTableWeekEvents(
                        nameEvent = eventNameUpdate,
                        dateEvent = dateUpdate,
                        timeEvent = timeUpdate,
                        nameEventUpdatePosition = eventNameUpdatePosition
                    )
                }.await()
            }


            val eventsDataClassUpdate = EventsDataClass(
                time = timeUpdate,
                eventName = eventNameUpdate,
                date = dateUpdate
            )

            adapter.updateEvents(position,eventsDataClassUpdate)

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