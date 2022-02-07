package com.example.singupactivity.ui.main.Adapter.SearchAdapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.ChildListListItemBinding
import com.example.singupactivity.ui.main.Data.ChildListDataClass
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.Fragment.Search.SearchChildFragment
import com.example.singupactivity.ui.main.Fragment.Search.SearchEventsFragment


private const val ITEM_CHILD: Int = 0
private const val ITEM_EMPTY_LIST: Int = 1

class SearchChildAdapter(fragment1: SearchChildFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var childList = ArrayList<ChildListDataClass>()

    val fragment: SearchChildFragment = fragment1

    class ChildListHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = ChildListListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(childListDataClass: ChildListDataClass) = with(binding) {
            tvNameChild.text = "Имя: ${childListDataClass.nameChild}"
            tvSurnameChild.text = "Фамилия: ${childListDataClass.surnameChild}"
            tvPatronamycChild.text = "Отчество: ${childListDataClass.patronamycChild}"
            tvBirthdayChild.text = "Дата рождения: ${childListDataClass.birthdayChild}"
            tvParentsPhoneNumber.text = "Номер телефона родителей: ${childListDataClass.parentsNumberChild}"
        }


    }

    override fun getItemViewType(position: Int): Int {
        return when {
            childList.isNullOrEmpty() -> ITEM_EMPTY_LIST
            else -> ITEM_CHILD
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_CHILD -> ChildListHolder(parent.inflate(R.layout.child_list_list_item))
            else -> EmptyListViewHolder(parent.inflate(R.layout.partial_empty_list))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChildListHolder) {
            holder.bind(childList[position])

            val childListDataClass: ChildListDataClass = childList[position]

            holder.itemView.setOnClickListener {

                fragment.addAndEditChildList(true, childListDataClass, position)
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

        return if (childList.isNullOrEmpty()) 1 else childList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addChildList(childListDataClass: ChildListDataClass) {

        childList.add(childListDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeChildList(position: Int) {

        childList.removeAt(position)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateChildList(position: Int, childListDataClass: ChildListDataClass) {

        childList[position] = childListDataClass
        notifyDataSetChanged()

    }

    fun ViewGroup.inflate(@LayoutRes resId: Int) =
        LayoutInflater.from(this.context).inflate(resId, this, false)!!
}