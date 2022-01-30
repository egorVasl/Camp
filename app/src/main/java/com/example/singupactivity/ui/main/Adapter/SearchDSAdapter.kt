package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.DailySceduleListItemBinding
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.Fragment.Search.SearchFragment

private const val ITEM_DAILY_SCHEDULE: Int = 0
private const val ITEM_EMPTY_LIST: Int = 1

class SearchDSAdapter(fragment1: SearchFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var searchList = ArrayList<DailyScheduleDataClass>()

    val fragment: SearchFragment = fragment1

    class SearchHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = DailySceduleListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(dailyScheduleDataClass: DailyScheduleDataClass) = with(binding) {

            tvNameEvent.text = "Название: ${dailyScheduleDataClass.nameEvent}"
            tvData.text = dailyScheduleDataClass.dateEvent
            tvTime.text = "Время: ${dailyScheduleDataClass.timeEvent}"

        }


    }

    inner class EmptyListViewHolder(item: View): RecyclerView.ViewHolder(item) {

        fun bind(){
        }
    }

    override fun getItemViewType(position: Int) : Int {
        return when{
            searchList.isNullOrEmpty() -> ITEM_EMPTY_LIST
            else -> ITEM_DAILY_SCHEDULE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {

        return when(viewType) {
            ITEM_DAILY_SCHEDULE -> SearchHolder(parent.inflate(R.layout.daily_scedule_list_item))
            else -> EmptyListViewHolder(parent.inflate(R.layout.partial_empty_list))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SearchHolder) {

            holder.bind(searchList[position])


            val dailyScheduleDataClass: DailyScheduleDataClass = searchList[position]

            holder.itemView.setOnClickListener {

                fragment.addAndEditSchedule(true, dailyScheduleDataClass, position)

            }
        } else if (holder is EmptyListViewHolder) {
            holder.bind()
        }
        }

    override fun getItemCount(): Int {

        return if(searchList.isNullOrEmpty()) 1 else searchList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDailySchedule(dailyScheduleDataClass: DailyScheduleDataClass) {

        searchList.add(dailyScheduleDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeDailySchedule(position: Int) {

        searchList.removeAt(position)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDailySchedule(position: Int, dailyScheduleDataClass: DailyScheduleDataClass) {

        searchList[position] = dailyScheduleDataClass
        notifyDataSetChanged()

    }

    fun ViewGroup.inflate(@LayoutRes resId: Int) =
        LayoutInflater.from(this.context).inflate(resId, this, false)!!

}
