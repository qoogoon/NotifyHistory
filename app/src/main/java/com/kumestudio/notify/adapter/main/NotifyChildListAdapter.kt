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
import kotlinx.android.synthetic.main.main_listitem_notifycation_child.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotifyChildListAdapter(var data : List<Data>) : RecyclerView.Adapter<NotifyChildListAdapter.ViewHolder>() {
    data class Data(val title : String, val content : String)
    private val baseBlackList = listOf(
            "android.title"
            , "android.text"
            , "android.bigText"
            , "android.title.big"
    )
    inner class ViewHolder constructor(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).
        inflate(R.layout.main_listitem_notifycation_child, parent, false)){
        var title: TextView = itemView.title
        var content: TextView = itemView.content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(baseBlackList.contains(data[position].title)){
            holder.title.height = 0
            holder.content.height = 0
            return
        }
        holder.title.text = data[position].title
        holder.content.text = data[position].content
    }

    override fun getItemCount(): Int {
        return data.size
    }
}