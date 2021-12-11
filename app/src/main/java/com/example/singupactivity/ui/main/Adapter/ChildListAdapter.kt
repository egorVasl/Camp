package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.ChildListListItemBinding
import com.example.singupactivity.databinding.DailySceduleListItemBinding
import com.example.singupactivity.ui.main.Data.ChildListDataClass
import com.example.singupactivity.ui.main.Data.DailyScheduleDataClass
import com.example.singupactivity.ui.main.Fragment.ChildListFragment

class ChildListAdapter(fragment1: ChildListFragment) :
    RecyclerView.Adapter<ChildListAdapter.ChildListHolder>() {

    var ChildList = ArrayList<ChildListDataClass>()

    val fragment: ChildListFragment = fragment1

    class ChildListHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = ChildListListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(childListDataClass: ChildListDataClass) = with(binding) {
            tvNameChild.text = childListDataClass.nameChild
            tvSurnameChild.text = childListDataClass.surnameChild
            tvPatronamycChild.text = childListDataClass.patronamycChild
            tvBirthdayChild.text = "Дата рождения: ${childListDataClass.birthdayChild}"
            tvParentsPhoneNumber.text = "Номер телефона родителей: ${childListDataClass.parentsNumberChild}"
            tvNameSquad.text = "Название отряда: ${childListDataClass.nameSquad}"
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.child_list_list_item, parent, false)

        return ChildListHolder(view)

    }

    override fun onBindViewHolder(holder: ChildListHolder, position: Int) {

        holder.bind(ChildList[position])

        val childListDataClass: ChildListDataClass = ChildList[position]

        holder.itemView.setOnClickListener {

          //  fragment.addAndEditSchedule(true, childListDataClass, position)

        }

    }

    override fun getItemCount(): Int {

        return ChildList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addChildList(childListDataClass: ChildListDataClass) {

        ChildList.add(childListDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeChildList(position: Int) {

        ChildList.removeAt(position)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChildList(position: Int, childListDataClass: ChildListDataClass) {

        ChildList[position] = childListDataClass
        notifyDataSetChanged()

    }

}