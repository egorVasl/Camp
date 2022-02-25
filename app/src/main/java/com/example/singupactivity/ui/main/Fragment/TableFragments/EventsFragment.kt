package com.example.singupactivity.ui.main.Fragment.TableFragments

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AddDailyScheduleBinding
import com.example.singupactivity.ui.main.Adapter.EventsAdapter
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.DataBase.CampDbManager
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_DATE
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_EVENT_NAME
import com.example.singupactivity.ui.main.DataBase.CampDbNameClass.COLUMN_NAME_TIME
import com.example.singupactivity.ui.main.Fragment.BottomSheet.*
import com.example.singupactivity.ui.main.Fragment.act
import com.example.singupactivity.ui.main.Fragment.ctx
import com.example.singupactivity.ui.main.Notification.AlarmReceiver
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentDSDataClass
import com.example.singupactivity.ui.main.Objects.DailySchedule.ArgumentsDSFlag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class EventsFragment : Fragment() {

    lateinit var adapter: EventsAdapter
    lateinit var campDbManager: CampDbManager

    lateinit var picker: MaterialTimePicker
    lateinit var calendar: Calendar
    lateinit var datePickerDialog: DatePickerDialog

    private fun getData(const: String): ArrayList<String> {
        return campDbManager.selectToTableWeekEvent(
            const
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_EVENTS) { _, _ ->
            addAndEditEvents(false, null, -1)
        }

        setFragmentResultListener(RATES_BOTTOM_REQUEST_KEY_IMPORT_PDF_EVENTS) { _, _ ->
            if (adapter.eventsList.isEmpty())
                alert(
                    getString(R.string.no_data_to_save_title),
                    getString(R.string.no_data_to_save)
                )
            else
                importTextFile()
        }

        campDbManager = CampDbManager(act)
        adapter = EventsAdapter(this@EventsFragment)

        val timeList = runBlocking {
            async {
                getData(COLUMN_NAME_TIME)
            }.await()
        }

        val eventNameList = runBlocking {
            async {
                getData(COLUMN_NAME_EVENT_NAME)
            }.await()
        }
        val dateList = runBlocking {
            async {
                getData(COLUMN_NAME_DATE)
            }.await()
        }
        for ((i, _) in timeList.withIndex()) {
            adapter.addEvents(
                EventsDataClass(
                    dateList[i],
                    timeList[i],
                    eventNameList[i]
                )
            )

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        val response = EventsDataClass(
            time = ArgumentDSDataClass.timeEvent,
            eventName = ArgumentDSDataClass.nameEvent,
            date = ArgumentDSDataClass.dateEvent
        )
        val responseUpdate = EventsDataClass(
            time = ArgumentDSDataClass.timeEventUpdate,
            eventName = ArgumentDSDataClass.nameEventUpdate,
            date = ArgumentDSDataClass.dateEventUpdate
        )
        if (ArgumentsDSFlag.isUpdate) {
            adapter.let {
                for ((i, _) in it.eventsList.withIndex()) {
                    if (it.eventsList[i] == response)
                        it.updateEvents(i, responseUpdate)
                    it.notifyDataSetChanged()
                }
            }
        } else {
            adapter.let {
                for ((i, _) in it.eventsList.withIndex()) {
                    if (it.eventsList[i] == response)
                        it.removeEvents(i)
                    it.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_events, container, false)

        val rv = view.findViewById<RecyclerView>(R.id.rcEvents)
        val fabEvents = view.findViewById<FloatingActionButton>(R.id.fabEvents)

        rv.layoutManager = LinearLayoutManager(act)
        rv.itemAnimator = DefaultItemAnimator()

        fabEvents.setOnClickListener {
            EventsBottomSheetDialog.newInstance()
                .show(this.parentFragmentManager, "bottomDialogDS")

        }

        rv.adapter = adapter
        return view
    }

    @SuppressLint("SimpleDateFormat")
    private fun importTextFile() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        val path = context?.getExternalFilesDir(null)

        val letDirectory = File(path, "Events")
        letDirectory.mkdirs()
        val file = File(letDirectory, "Events $currentDate.txt")
        try {
            FileOutputStream(file).use { stream ->
                adapter.let {
                    for ((i, _) in it.eventsList.withIndex()) {
                        stream.write("День:${i + 1}\n".toByteArray())
                        stream.write("Дата: ${it.eventsList[i].date}\n".toByteArray())
                        stream.write("Название мероприятия: ${it.eventsList[i].eventName}\n".toByteArray())
                        stream.write("Время: ${it.eventsList[i].time}\n\n".toByteArray())

                    }
                }
                stream.close()
                alert(
                    "Файл успешно создан!",
                    "Путь: $path/Events/Events $currentDate.txt"
                )
            }
        } catch (exe: IOException) {
            Toast.makeText(ctx, "Ошибка создания файла: $exe", Toast.LENGTH_SHORT).show()
        }
    }

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

            if (isUpdate && eventsDataClass != null) {
                tiName.editText?.setText(eventsDataClass.eventName)
//                tiDate.editText?.setText(eventsDataClass.date)
//                tiTime.editText?.setText(eventsDataClass.time)
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
                            binding.tiName.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiDate.editText?.text.toString()) -> {
                            binding.tiDate.error = getString(R.string.enter_data_event)
                            binding.tiDate.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        TextUtils.isEmpty(tiTime.editText?.text.toString()) -> {
                            binding.tiTime.error = getString(R.string.enter_time_event)
                            binding.tiTime.defaultHintTextColor =
                                ctx.getColorStateList(R.color.errorColor)
                            return@OnClickListener
                        }
                        else -> {
                            alertDialog.dismiss()
                        }
                    }
                    if (isUpdate && eventsDataClass != null) {
                        if (etNameUpdate != null) {
                            updateEvents(
                                eventNameUpdate = tiName.editText?.text.toString(),
                                dateUpdate = tiDate.editText?.text.toString(),
                                timeUpdate = tiTime.editText?.text.toString(),
                                eventNameUpdatePosition = etNameUpdate,
                                position = position
                            )
                            createNotificationChannel()
                            setNotification(binding)
                        }

                    } else {
                        createEvents(
                            eventName = tiName.editText?.text.toString(),
                            date = tiDate.editText?.text.toString(),
                            time = tiTime.editText?.text.toString()
                        )
                        createNotificationChannel()
                        setNotification(binding)
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
            intent.putExtra(AlarmReceiver.IS_EVENT, true)

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

        adapter.updateEvents(position, eventsDataClassUpdate)

    }

    private fun createEvents(
        time: String,
        eventName: String,
        date: String
    ) {
        runBlocking {
            async {
                campDbManager.insertToTableWeekEvent(
                    eventName = eventName,
                    date = date,
                    time = time
                )
            }.await()
        }


        val eventsDataClassUpdate = EventsDataClass(
            time = time,
            eventName = eventName,
            date = date
        )

        adapter.addEvents(eventsDataClassUpdate)

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