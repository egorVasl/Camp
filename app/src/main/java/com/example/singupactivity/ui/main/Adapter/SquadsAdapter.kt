package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.DailySceduleListItemBinding
import com.example.singupactivity.databinding.SquadaListItemBinding
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.Data.SquadsDataClass
import com.example.singupactivity.ui.main.Fragment.DailyScheduleFragment
import com.example.singupactivity.ui.main.Fragment.SquadsFragment

class SquadsAdapter(fragment1: SquadsFragment) :
    RecyclerView.Adapter<SquadsAdapter.SquadsHolder>() {

    var squadsList = ArrayList<SquadsDataClass>()

    val fragment: SquadsFragment = fragment1

    class SquadsHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = SquadaListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(squadsDataClass: SquadsDataClass) = with(binding) {

            tvSquadsName.text = squadsDataClass.squadName
            tvSquadsNumber.text = "№ ${squadsDataClass.squadNumber}"

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.squada_list_item, parent, false)

        return SquadsHolder(view)

    }

    override fun onBindViewHolder(holder: SquadsHolder, position: Int) {

        holder.bind(squadsList[position])

        val squadsDataClass: SquadsDataClass = squadsList[position]

        holder.itemView.setOnClickListener {

            fragment.addAndEditSquads(true, squadsDataClass, position)

        }

    }

    override fun getItemCount(): Int {

        return squadsList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSquads(squadsDataClass: SquadsDataClass) {

        squadsList.add(squadsDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeSquads(position: Int) {

        squadsList.removeAt(position)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSquads(position: Int, squadsDataClass: SquadsDataClass) {

        squadsList[position] = squadsDataClass
        notifyDataSetChanged()

    }
}
