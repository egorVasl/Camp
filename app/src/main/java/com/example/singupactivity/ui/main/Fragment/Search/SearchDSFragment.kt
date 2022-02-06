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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddDailyScheduleBinding
import com.example.singupactivity.ui.main.Adapter.SearchAdapters.SearchDSAdapter
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_NAME_EVENT
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME_EVENT
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.Arguments
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class SearchDSFragment : Fragment() {

    lateinit var adapter: SearchDSAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText



    companion object {
        @JvmStatic
        fun newInstance() =
            SearchDSFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        campDbManager = CampDbManager(act)
        adapter = SearchDSAdapter(this@SearchDSFragment)

    }

    private fun getData(const: String, searchText: String): ArrayList<String> {
        return  campDbManager.selectToTableDailySchedule(
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
                    adapter.searchList.clear()
                    val eventTimeList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_TIME_EVENT, searchText)
                            }.await()
                        }

                    val eventNameList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_NAME_EVENT, searchText)
                            }.await()
                        }

                    val eventDateList =
                        runBlocking {
                            async {
                                getData(COLUMN_NAME_DATE_EVENT, searchText)
                            }.await()
                        }

                    for ((i, _) in eventTimeList.withIndex()) {
                        adapter.addDailySchedule(
                            DailyScheduleDataClass(
                                eventTimeList[i],
                                eventNameList[i],
                                eventDateList[i]
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
    fun addAndEditSchedule(
        isUpdate: Boolean,
        dailyScheduleDataClass: DailyScheduleDataClass?,
        position: Int
    ) {
        val binding: AddDailyScheduleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.add_daily_schedule, null, false
        )
        val etNameUpdate: String? = dailyScheduleDataClass?.nameEvent

        val alertDialogBuilderUserInput: AlertDialog.Builder =
            AlertDialog.Builder(act)
        alertDialogBuilderUserInput.setView(binding.root)

        with(binding) {

            newDayTitle.text = if (!isUpdate) getString(R.string.add) else getString(R.string.edit)

            if (isUpdate && dailyScheduleDataClass != null) {
                tiName.editText?.setText(dailyScheduleDataClass.nameEvent)
                tiDate.editText?.setText(dailyScheduleDataClass.dateEvent)
                tiTime.editText?.setText(dailyScheduleDataClass.timeEvent)
            }
            alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(
                    if (isUpdate) getString(R.string.update) else getString(R.string.save)
                ) { _, _ -> }
                .setNegativeButton(if (isUpdate) getString(R.string.delete) else getString(R.string.close)
                ) { dialogBox, id ->
                    if (isUpdate) {
                        ArgumentsDSFlag.isUpdate = false
                        ArgumentDSDataClass.nameEvent = adapter.searchList[position].nameEvent
                        ArgumentDSDataClass.timeEvent = adapter.searchList[position].timeEvent
                        ArgumentDSDataClass.dateEvent = adapter.searchList[position].dateEvent
                        deleteDailySchedule(
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
                    if (isUpdate && dailyScheduleDataClass != null) {
                        if (etNameUpdate != null) {
                            ArgumentsDSFlag.isUpdate = true
                            ArgumentDSDataClass.nameEvent = adapter.searchList[position].nameEvent
                            ArgumentDSDataClass.timeEvent = adapter.searchList[position].timeEvent
                            ArgumentDSDataClass.dateEvent = adapter.searchList[position].dateEvent
                            ArgumentDSDataClass.nameEventUpdate = tiName.editText?.text.toString()
                            ArgumentDSDataClass.timeEventUpdate = tiTime.editText?.text.toString()
                            ArgumentDSDataClass.dateEventUpdate = tiDate.editText?.text.toString()
                            updateDailySchedule(
                                nameEventUpdate = tiName.editText?.text.toString(),
                                dateEventUpdate = tiDate.editText?.text.toString(),
                                timeEventUpdate = tiTime.editText?.text.toString(),
                                nameEventUpdatePosition = etNameUpdate,
                                position = position
                            )
                        }
                    }
                })
        }
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