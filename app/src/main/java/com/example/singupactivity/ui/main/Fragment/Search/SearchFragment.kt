package com.example.singupactivity.ui.main.Fragment.Search

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.ui.main.Adapter.SearchDSAdapter
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSdataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDS
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag


class SearchFragment : Fragment() {

    lateinit var adapter: SearchDSAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = activity?.let { CampDbManager(it) }!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        rv = view.findViewById(R.id.rcDailyScheduleSearch)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.itemAnimator = DefaultItemAnimator()
        adapter = SearchDSAdapter(this@SearchFragment)
        rv.adapter = adapter
        val ibSearch = view.findViewById<ImageButton>(R.id.imageButtonSearch)
        etCardSearch = view.findViewById(R.id.etSearchDailySchedule)

        ibSearch?.setOnClickListener {
            if (ArgumentsDS.arg.isNotBlank()) {
                val searchText = etCardSearch.text.toString()
                if (searchText.isNotEmpty()) {
                    campDbManager = activity?.let { CampDbManager(it) }!!
                    adapter.searchList.clear()
                    val eventTimeList = campDbManager.selectToTableDailySchedule(
                        CampDbNameClass.COLUMN_NAME_TIME_EVENT,
                        searchText,
                        ArgumentsDS.arg
                    )
                    val eventNameList = campDbManager.selectToTableDailySchedule(
                        CampDbNameClass.COLUMN_NAME_NAME_EVENT,
                        searchText,
                        ArgumentsDS.arg
                    )
                    val eventDateList = campDbManager.selectToTableDailySchedule(
                        CampDbNameClass.COLUMN_NAME_DATE_EVENT,
                        searchText,
                        ArgumentsDS.arg
                    )
                    for ((i, elm) in eventTimeList.withIndex()) {
                        adapter.addDailySchedule(
                            DailyScheduleDataClass(
                                eventTimeList[i],
                                eventNameList[i], eventDateList[i]
                            )
                        )

                    }
                    rv.adapter = adapter
                    if (adapter.searchList.isEmpty()) {
                        alert(
                            getString(R.string.no_data_after_search_title),
                            getString(R.string.no_data_after_search)
                        )
                        etCardSearch.text.clear()
                    }
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
    fun addAndEditSchedule(
        isUpdate: Boolean,
        dailyScheduleDataClass: DailyScheduleDataClass?,
        position: Int
    ) {
        val view = LayoutInflater.from(ctx).inflate(R.layout.add_daily_schedule, null)


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
            .setNegativeButton(if (isUpdate) "Удалить" else "Закрыть"
            ) { dialogBox, _ ->
                if (isUpdate) {
                    ArgumentsDSFlag.isUpdate = false
                    ArgumentDSdataClass.nameEvent = adapter.searchList[position].nameEvent
                    ArgumentDSdataClass.timeEvent = adapter.searchList[position].timeEvent
                    ArgumentDSdataClass.dateEvent = adapter.searchList[position].dateEvent
                    deleteDailySchedule(
                        position = position,
                        const = etName.text.toString()
                    )
                    etCardSearch.text.clear()
                } else {
                    dialogBox.cancel()
                }
            }

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
                    ArgumentsDSFlag.isUpdate = true
                    ArgumentDSdataClass.nameEvent = adapter.searchList[position].nameEvent
                    ArgumentDSdataClass.timeEvent = adapter.searchList[position].timeEvent
                    ArgumentDSdataClass.dateEvent = adapter.searchList[position].dateEvent
                    ArgumentDSdataClass.nameEventUpdate = etName.text.toString()
                    ArgumentDSdataClass.timeEventUpdate = etTime.text.toString()
                    ArgumentDSdataClass.dateEventUpdate = etData.text.toString()
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