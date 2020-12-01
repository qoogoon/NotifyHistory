package com.kumestudio.notify.adapter.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.kumestudio.notify.R
import com.kumestudio.notify.db.NotificationData
import kotlinx.android.synthetic.main.main_item_notifycation_child.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotifyChildListAdapter(var data : List<NotificationData>) : RecyclerView.Adapter<NotifyChildListAdapter.ViewHolder>() {
    inner class ViewHolder constructor(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).
        inflate(R.layout.main_item_notifycation_child, parent, false)){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.text.text = data[position].text

        val date = Date(data[position].`when`)
        holder.itemView.date.text = SimpleDateFormat("h:mm a", Locale.KOREA).format(date)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}