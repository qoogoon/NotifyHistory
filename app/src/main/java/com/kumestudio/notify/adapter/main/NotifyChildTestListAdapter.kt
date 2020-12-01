package com.kumestudio.notify.adapter.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kumestudio.notify.R
import kotlinx.android.synthetic.main.main_item_notifycation_child_test.view.*

class NotifyChildTestListAdapter(var data : List<Data>) : RecyclerView.Adapter<NotifyChildTestListAdapter.ViewHolder>() {
    data class Data(val title : String, val content : String)
    private val baseBlackList = listOf(
            "android.title"
            , "android.text"
            , "android.bigText"
            , "android.title.big"
    )
    inner class ViewHolder constructor(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).
        inflate(R.layout.main_item_notifycation_child_test, parent, false)){
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