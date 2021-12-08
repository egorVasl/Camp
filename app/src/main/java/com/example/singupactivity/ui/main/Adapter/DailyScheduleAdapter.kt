package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.DailySceduleListItemBinding
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.Fragment.DailyScheduleFragment

class DailyScheduleAdapter(fragment1: DailyScheduleFragment) : RecyclerView.Adapter<DailyScheduleAdapter.DailyScheduleHolder>() {

    val dailyScheduleList = ArrayList<DailyScheduleDataClass>()

    val fragment :DailyScheduleFragment = fragment1

    class DailyScheduleHolder(item : View) : RecyclerView.ViewHolder(item) {


        private val binding = DailySceduleListItemBinding.bind(item)

        fun bind(dailyScheduleDataClass: DailyScheduleDataClass) = with(binding){

            tvNameEvent.text = dailyScheduleDataClass.nameEvent
            tvData.text = dailyScheduleDataClass.dateEvent
            tvTime.text = dailyScheduleDataClass.timeEvent

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyScheduleHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_scedule_list_item, parent, false)

        return DailyScheduleHolder(view)

    }

    override fun onBindViewHolder(holder: DailyScheduleHolder, position: Int) {

        holder.bind(dailyScheduleList[position])

        val dailyScheduleDataClass: DailyScheduleDataClass = dailyScheduleList[position]

        holder.itemView.setOnClickListener{

            fragment.addAndEditSchedule(true, dailyScheduleDataClass, position)

        }

    }

    override fun getItemCount(): Int {

        return dailyScheduleList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDailySchedule(dailyScheduleDataClass: DailyScheduleDataClass) {

        dailyScheduleList.add(dailyScheduleDataClass)
        notifyDataSetChanged()

    }

}