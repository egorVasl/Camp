package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.DailySceduleListItemBinding
import com.example.singupactivity.databinding.SearchLayoutBinding
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass

class SearchAdapter :
    RecyclerView.Adapter<SearchAdapter.SearchHolder>() {


    class SearchHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = SearchLayoutBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind() = with(binding) {

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_layout, parent, false)

        return SearchHolder(view)

    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {

        holder.bind()
//
//        val dailyScheduleDataClass: DailyScheduleDataClass = dailyScheduleList[position]
//
//        holder.itemView.setOnClickListener {
//
//            fragment.addAndEditSchedule(true, dailyScheduleDataClass, position)

        }

    override fun getItemCount(): Int {
        return 1
    }

}
