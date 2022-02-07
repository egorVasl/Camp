package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.SquadaListItemBinding
import com.example.singupactivity.ui.main.Adapter.SearchAdapters.SearchSquadsAdapter
import com.example.singupactivity.ui.main.Data.SquadsDataClass
import com.example.singupactivity.ui.main.Fragment.TableFragments.SquadsFragment

private const val ITEM_SQUADS: Int = 0
private const val ITEM_EMPTY_LIST: Int = 1

class SquadsAdapter(fragment1: SquadsFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var squadsList = ArrayList<SquadsDataClass>()

    val fragment: SquadsFragment = fragment1

    class SquadsHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = SquadaListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(squadsDataClass: SquadsDataClass) = with(binding) {

            tvSquadsName.text = "Название отряда: ${squadsDataClass.squadName}"
            tvSquadsNumber.text = "Номер: ${squadsDataClass.squadNumber}"

        }


    }
    override fun getItemViewType(position: Int): Int {
        return when {
            squadsList.isNullOrEmpty() -> ITEM_EMPTY_LIST
            else -> ITEM_SQUADS
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_SQUADS -> SquadsHolder(parent.inflate(R.layout.squada_list_item))
            else -> EmptyListViewHolder(parent.inflate(R.layout.partial_empty_list))
        }

    }

    inner class EmptyListViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        fun bind() {
        }
    }

    override fun getItemCount(): Int {

        return if (squadsList.isNullOrEmpty()) 1 else squadsList.size

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SquadsHolder) {
            holder.bind(squadsList[position])

            val squadsDataClass: SquadsDataClass = squadsList[position]

            holder.itemView.setOnClickListener {

                fragment.addAndEditSquads(true, squadsDataClass, position)

            }
        } else if (holder is EmptyListViewHolder) {
            holder.bind()
        }

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
    fun ViewGroup.inflate(@LayoutRes resId: Int) =
        LayoutInflater.from(this.context).inflate(resId, this, false)!!
}
