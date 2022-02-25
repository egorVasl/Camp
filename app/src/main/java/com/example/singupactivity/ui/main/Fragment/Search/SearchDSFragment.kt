package com.example.singupactivity.ui.main.Fragment.Search

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.example.singupactivity.ui.main.Notification.AlarmReceiver
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.Arguments
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList


class SearchDSFragment : Fragment() {

    lateinit var adapter: SearchDSAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var rv: RecyclerView
    lateinit var etCardSearch: EditText

    lateinit var picker: MaterialTimePicker
    lateinit var calendar: Calendar
    lateinit var datePickerDialog: DatePickerDialog

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

            imageTime.setOnClickListener {
                showTimePicker(binding)
            }
            tiTimeEditText.setOnClickListener {
                showTimePicker(binding)
            }


            tiDateEditText.setOnClickListener {
                showDatePicker(binding)
            }
            imageDate.setOnClickListener {
                showDatePicker(binding)
            }

            newDayTitle.text = if (!isUpdate) getString(R.string.add) else getString(R.string.edit)

            if (isUpdate && dailyScheduleDataClass != null) {
                tiName.editText?.setText(dailyScheduleDataClass.nameEvent)
//                tiDate.editText?.setText(dailyScheduleDataClass.dateEvent)
//                tiTime.editText?.setText(dailyScheduleDataClass.timeEvent)
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
                            createNotificationChannel()
                            setNotification(binding)
                        }
                    }
                })
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setNotification(binding: AddDailyScheduleBinding) {
        val intent = Intent(act.applicationContext, AlarmReceiver::class.java)
        with(binding) {
            intent.putExtra(AlarmReceiver.DATE_EVENT_EXTRA, tiDate.editText?.text.toString())
            intent.putExtra(AlarmReceiver.TIME_EVENT_EXTRA, tiTime.editText?.text.toString())
            intent.putExtra(AlarmReceiver.NAME_EVENT_EXTRA, tiName.editText?.text.toString())
            intent.putExtra(AlarmReceiver.IS_EVENT, false)

            val pendingIntent = PendingIntent.getBroadcast(
                act.applicationContext,
                AlarmReceiver.NOTIFICATION_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                getTime(),
                pendingIntent
            )
        }
    }

    private fun getTime(): Long {
        val minute = picker.minute
        val hour = picker.hour
        val day = datePickerDialog.datePicker.dayOfMonth
        val month = datePickerDialog.datePicker.month
        val year = datePickerDialog.datePicker.year

        val calendarSave = Calendar.getInstance()
        calendarSave.set(year, month, day, hour-1, minute)
        return calendarSave.timeInMillis
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker(binding: AddDailyScheduleBinding) {
        calendar = Calendar.getInstance()
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)
        val mMonth = calendar.get(Calendar.MONTH)
        val mYear = calendar.get(Calendar.YEAR)
        datePickerDialog = DatePickerDialog(
            ctx,
            { _, year, month, dayOfMonth ->
                binding.tiDate.editText?.setText(
                    "$dayOfMonth.${String.format("%02d", (month + 1))}.$year"
                )
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month + 1)
                calendar.set(Calendar.YEAR, year)
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun showTimePicker(binding: AddDailyScheduleBinding) {

        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText(R.string.select_time)
            .build()
        picker.show(act.supportFragmentManager, AlarmReceiver.CHANNEL_ID)
        picker.addOnPositiveButtonClickListener {
            if (picker.hour >= 12) {
                binding.tiTime.editText?.setText(
                    "${
                        String.format(
                            "%02d",
                            (picker.hour)
                        )
                    } : ${String.format("%02d", picker.minute)} ПП"
                )
            } else {
                binding.tiTime.editText?.setText(
                    "${
                        String.format(
                            "%02d",
                            (picker.hour)
                        )
                    } : ${String.format("%02d", picker.minute)} ДП"
                )
            }


            calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, picker.hour)
            calendar.set(Calendar.MINUTE, picker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channelIDRemainderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AlarmReceiver.CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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