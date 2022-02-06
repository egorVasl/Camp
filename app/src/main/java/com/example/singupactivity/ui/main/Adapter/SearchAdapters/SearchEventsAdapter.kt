package com.example.singupactivity.ui.main.Adapter.SearchAdapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.DailySceduleListItemBinding
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.Fragment.Search.SearchEventsFragment


private const val ITEM_EVENTS: Int = 0
private const val ITEM_EMPTY_LIST: Int = 1


class SearchEventsAdapter (fragment1: SearchEventsFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var eventsList = ArrayList<EventsDataClass>()

    val fragment: SearchEventsFragment = fragment1

    class EventsHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = DailySceduleListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(eventsDataClass: EventsDataClass) = with(binding) {

            tvNameEvent.text = "Название: ${eventsDataClass.eventName}"
            tvData.text = eventsDataClass.date
            tvTime.text = "Время: ${eventsDataClass.time}"

        }


    }

    override fun getItemViewType(position: Int): Int {
        return when {
            eventsList.isNullOrEmpty() -> ITEM_EMPTY_LIST
            else -> ITEM_EVENTS
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_EVENTS -> EventsHolder(parent.inflate(R.layout.daily_scedule_list_item))
            else -> EmptyListViewHolder(parent.inflate(R.layout.partial_empty_list))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EventsHolder) {
            holder.bind(eventsList[position])

            val eventsDataClass: EventsDataClass = eventsList[position]

            holder.itemView.setOnClickListener {

                fragment.addAndEditEvents(true, eventsDataClass, position)

            }
        } else if (holder is EmptyListViewHolder) {
            holder.bind()
        }


    }

    inner class EmptyListViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        fun bind() {
        }
    }

    override fun getItemCount(): Int {

        return if (eventsList.isNullOrEmpty()) 1 else eventsList.size

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

    fun ViewGroup.inflate(@LayoutRes resId: Int) =
        LayoutInflater.from(this.context).inflate(resId, this, false)!!
}