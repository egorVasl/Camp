package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.CounselorListItemBinding
import com.example.singupactivity.databinding.PartialEmptyListBinding
import com.example.singupactivity.ui.main.Data.CounselorDataClass


private const val ITEM_COUNSELOR: Int = 0
private const val ITEM_EMPTY_LIST: Int = 1

class CounselorAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var counselorList = ArrayList<CounselorDataClass>()

    class SearchHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val bindingCounselor = CounselorListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(counselorDataClass: CounselorDataClass) = with(bindingCounselor) {

            tvNameParameterProfile.text = counselorDataClass.title
            tvParameterProfile.text = counselorDataClass.subscription

        }

    }

    inner class EmptyListViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val bindingEmptyList = PartialEmptyListBinding.bind(item)

        fun bind() = with(bindingEmptyList) {

            tvNoItems.text = "Данные пользователя не были добавлены."

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            counselorList.isNullOrEmpty() -> ITEM_EMPTY_LIST
            else -> ITEM_COUNSELOR
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_COUNSELOR -> SearchHolder(parent.inflate(R.layout.daily_scedule_list_item))
            else -> EmptyListViewHolder(parent.inflate(R.layout.partial_empty_list))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SearchHolder) {

            holder.bind(counselorList[position])

        } else if (holder is EmptyListViewHolder) {

            holder.bind()

        }

    }

    override fun getItemCount(): Int {

        return if (counselorList.isNullOrEmpty()) 1 else counselorList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDailyCounselor(counselorDataClass: CounselorDataClass) {

        counselorList.add(counselorDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteAllCounselor() {

        counselorList.clear()
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCounselor(position: Int, counselorDataClass: CounselorDataClass) {

        counselorList[position] = counselorDataClass
        notifyDataSetChanged()

    }

    fun ViewGroup.inflate(@LayoutRes resId: Int) =
        LayoutInflater.from(this.context).inflate(resId, this, false)!!

}