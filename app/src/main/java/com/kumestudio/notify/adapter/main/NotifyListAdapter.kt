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
import kotlinx.android.synthetic.main.main_listitem_notifycation.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotifyListAdapter(var data : LiveData<List<NotificationData>>, var context : Context) : RecyclerView.Adapter<NotifyListAdapter.ViewHolder>() {
    inner class ViewHolder constructor(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).
        inflate(R.layout.main_listitem_notifycation, parent, false)){
        var notifyText: TextView = itemView.text
        var smallIcon : ImageView = itemView.smallIcon
        var appName: TextView = itemView.appName
        var date: TextView = itemView.date
        var title: TextView = itemView.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(data.value == null) return

        val text = data.value!![position].text
        val title = data.value!![position].title
        val smallIconResID = data.value!![position].smallIcon
        val packageName = data.value!![position].packageName
        val appName = data.value!![position].appName
        val date = Date(data.value!![position].`when`)
        val smallIcon = Icon.createWithResource(packageName, smallIconResID).setTint(Color.GRAY)

        holder.appName.text = appName
        holder.notifyText.text = text
        holder.smallIcon.setImageDrawable(smallIcon.loadDrawable(context))
        holder.date.text = SimpleDateFormat("h:mm a", Locale.KOREA).format(date)

        if(title != null)
            holder.title.text = title
        else
            holder.title.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        if(data.value == null) return 0
        return data.value!!.size
    }
}