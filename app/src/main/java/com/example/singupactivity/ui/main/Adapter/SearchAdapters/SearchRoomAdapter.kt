package com.example.singupactivity.ui.main.Adapter.SearchAdapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.RoomListItemBinding
import com.example.singupactivity.ui.main.Data.EventsDataClass
import com.example.singupactivity.ui.main.Data.RoomDataClass
import com.example.singupactivity.ui.main.Fragment.Search.SearchRoomFragment

private const val ITEM_ROOM: Int = 0
private const val ITEM_EMPTY_LIST: Int = 1

class SearchRoomAdapter(fragment1: SearchRoomFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var roomList = ArrayList<RoomDataClass>()

    val fragment: SearchRoomFragment = fragment1

    class RoomHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = RoomListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(roomDataClass: RoomDataClass) = with(binding) {

            tvFloor.text = "Этаж: ${roomDataClass.floor}"
            tvRoomNumber.text = "Номер комнаты: ${roomDataClass.roomNumber}"
            tvQuantity.text = "Детей: ${roomDataClass.quantity}"
            tvFIOChild.text = "Ребёнок: ${roomDataClass.FIOChild}"

        }


    }
    override fun getItemViewType(position: Int): Int {
        return when {
            roomList.isNullOrEmpty() -> ITEM_EMPTY_LIST
            else -> ITEM_ROOM
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ITEM_ROOM -> RoomHolder(parent.inflate(R.layout.room_list_item))
            else -> EmptyListViewHolder(parent.inflate(R.layout.partial_empty_list))
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RoomHolder) {
            holder.bind(roomList[position])

            val roomDataClass: RoomDataClass = roomList[position]

            holder.itemView.setOnClickListener {

                fragment.addAndEditRoom(true, roomDataClass, position)

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

        return if (roomList.isNullOrEmpty()) 1 else roomList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addRoom(roomDataClass: RoomDataClass) {

        roomList.add(roomDataClass)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeRoom(position: Int) {

        roomList.removeAt(position)
        notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRoom(position: Int, roomDataClass: RoomDataClass) {

        roomList[position] = roomDataClass
        notifyDataSetChanged()

    }

    fun ViewGroup.inflate(@LayoutRes resId: Int) =
        LayoutInflater.from(this.context).inflate(resId, this, false)!!
}