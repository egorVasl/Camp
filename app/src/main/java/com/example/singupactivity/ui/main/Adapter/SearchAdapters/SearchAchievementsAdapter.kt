package com.example.singupactivity.ui.main.Adapter.SearchAdapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AchievementsListItemBinding
import com.example.singupactivity.ui.main.Data.AchievementsDataClass
import com.example.singupactivity.ui.main.Data.RoomDataClass
import com.example.singupactivity.ui.main.Fragment.Search.SearchAchievementsFragment
import com.example.singupactivity.ui.main.Fragment.Search.SearchRoomFragment


private const val ITEM_ACHIEVEMENTS: Int = 0
private const val ITEM_EMPTY_LIST: Int = 1

class SearchAchievementsAdapter(fragment1: SearchAchievementsFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var achievementsList = ArrayList<AchievementsDataClass>()

    val fragment: SearchAchievementsFragment = fragment1

    class AchievementsHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = AchievementsListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(achievementsDataClass: AchievementsDataClass) = with(binding) {

            tvAchievementEvent.text = "Мероприятие: ${achievementsDataClass.eventName}"
            tvAchievementPlace.text = "Место: ${achievementsDataClass.place}"
            tvAchievementSquad.text = "Отряд: ${achievementsDataClass.squadName}"

        }


    }

    override fun getItemViewType(position: Int): Int {
        return when {
            achievementsList.isNullOrEmpty() -> ITEM_EMPTY_LIST
            else -> ITEM_ACHIEVEMENTS
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_ACHIEVEMENTS -> AchievementsHolder(parent.inflate(R.layout.achievements_list_item))
            else -> EmptyListViewHolder(parent.inflate(R.layout.partial_empty_list))
        }

    }


    inner class EmptyListViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        fun bind() {
        }
    }

    override fun getItemCount(): Int {

        return if (achievementsList.isNullOrEmpty()) 1 else achievementsList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AchievementsHolder) {
            holder.bind(achievementsList[position])

            val achievementsDataClass: AchievementsDataClass = achievementsList[position]

            holder.itemView.setOnClickListener {

                fragment.addAndEditAchievements(true, achievementsDataClass, position)

            }
        } else if (holder is EmptyListViewHolder) {
            holder.bind()
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    fun addAchievements(achievementsDataClass: AchievementsDataClass) {

        achievementsList.add(achievementsDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAchievements(position: Int) {

        achievementsList.removeAt(position)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAchievements(position: Int, achievementsDataClass: AchievementsDataClass) {

        achievementsList[position] = achievementsDataClass
        notifyDataSetChanged()

    }


    fun ViewGroup.inflate(@LayoutRes resId: Int) =
        LayoutInflater.from(this.context).inflate(resId, this, false)!!



}