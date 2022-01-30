package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.AchievementsListItemBinding
import com.example.singupactivity.ui.main.Data.AchievementsDataClass
import com.example.singupactivity.ui.main.Fragment.TableFragments.AchievementsFragment

class AchievementsAdapter(fragment1: AchievementsFragment) :
    RecyclerView.Adapter<AchievementsAdapter.AchievementsHolder>() {

    var achievementsList = ArrayList<AchievementsDataClass>()

    val fragment: AchievementsFragment = fragment1

    class AchievementsHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = AchievementsListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(achievementsDataClass: AchievementsDataClass) = with(binding) {

            tvAchievementEvent.text = "Мероприятие: ${achievementsDataClass.eventName}"
            tvAchievementPlace.text = "Место: ${achievementsDataClass.place}"
            tvAchievementSquad.text = "Отряд: ${achievementsDataClass.squadName}"

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.achievements_list_item, parent, false)

        return AchievementsHolder(view)

    }

    override fun onBindViewHolder(holder: AchievementsHolder, position: Int) {

        holder.bind(achievementsList[position])

        val achievementsDataClass: AchievementsDataClass = achievementsList[position]

        holder.itemView.setOnClickListener {

            fragment.addAndEditAchievements(true, achievementsDataClass, position)

        }

    }

    override fun getItemCount(): Int {

        return achievementsList.size

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

}