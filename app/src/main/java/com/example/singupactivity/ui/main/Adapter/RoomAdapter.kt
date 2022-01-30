package com.example.singupactivity.ui.main.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.singupactivity.R
import com.example.singupactivity.databinding.RoomListItemBinding
import com.example.singupactivity.ui.main.Data.RoomDataClass
import com.example.singupactivity.ui.main.Fragment.TableFragments.RoomFragment

class RoomAdapter(fragment1: RoomFragment) :
    RecyclerView.Adapter<RoomAdapter.RoomHolder>() {

    var roomList = ArrayList<RoomDataClass>()

    val fragment: RoomFragment = fragment1

    class RoomHolder(item: View) : RecyclerView.ViewHolder(item) {


        private val binding = RoomListItemBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(roomDataClass: RoomDataClass) = with(binding) {

            tvFloor.text = "Этаж: ${roomDataClass.floor}"
            tvRoomNumber.text = "№: ${roomDataClass.roomNumber}"
            tvQuantity.text = "Детей: ${roomDataClass.quantity}"

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.room_list_item, parent, false)

        return RoomHolder(view)

    }

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {

        holder.bind(roomList[position])

        val roomDataClass: RoomDataClass = roomList[position]

        holder.itemView.setOnClickListener {

            fragment.addAndEditRoom(true, roomDataClass, position)

        }

    }

    override fun getItemCount(): Int {

        return roomList.size

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

}