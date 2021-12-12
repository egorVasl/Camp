package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.DailySceduleListItemBinding
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.Fragment.EventsFragment

class EventsAdapter (fragment1: EventsFragment) :
    RecyclerView.Adapter<EventsAdapter.EventsHolder>() {

    var eventsList = ArrayList<EventsDataClass>()

    val fragment: EventsFragment = fragment1

    class EventsHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = DailySceduleListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(eventsDataClass: EventsDataClass) = with(binding) {

            tvNameEvent.text = "Название: ${eventsDataClass.eventName}"
            tvData.text = eventsDataClass.date
            tvTime.text = "Время: ${eventsDataClass.time}"

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_scedule_list_item, parent, false)

        return EventsHolder(view)

    }

    override fun onBindViewHolder(holder: EventsHolder, position: Int) {

        holder.bind(eventsList[position])

        val eventsDataClass: EventsDataClass = eventsList[position]

        holder.itemView.setOnClickListener {

            fragment.addAndEditEvents(true, eventsDataClass, position)

        }

    }

    override fun getItemCount(): Int {

        return eventsList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addEvents(eventsDataClass: EventsDataClass) {

        eventsList.add(eventsDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeEvents(position: Int) {

        eventsList.removeAt(position)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateEvents(position: Int, eventsDataClass: EventsDataClass) {

        eventsList[position] = eventsDataClass
        notifyDataSetChanged()

    }

}