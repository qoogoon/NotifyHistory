package com.kumestudio.notification.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumestudio.notification.R
import com.kumestudio.notification.db.NotificationData
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