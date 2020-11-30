package com.kumestudio.notify.adapter.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.kumestudio.notify.Data
import com.kumestudio.notify.R
import com.kumestudio.notify.db.NotificationData
import kotlinx.android.synthetic.main.main_listitem_notifycation.view.*
import java.text.SimpleDateFormat
import java.util.*


class NotifyListAdapter(var data  :MutableList<Data.NotificationGroup>, var context : Context) : RecyclerView.Adapter<NotifyListAdapter.ViewHolder>() {
    inner class ViewHolder constructor(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).
        inflate(R.layout.main_item_notification, parent, false)){
        var notifyText: TextView = itemView.text
        var smallIcon : ImageView = itemView.smallIcon
        var appName: TextView = itemView.appName
        var date: TextView = itemView.date
        var title: TextView = itemView.title
        var packageName: TextView = itemView.packageName
//        var childList: RecyclerView = itemView.childList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(data == null) return

        val text = data[position].text
        val title = data[position].title
        val smallIconResID = data[position].smallIcon
        val packageName = data[position].packageName
        val appName = data[position].appName
        val date = Date(data[position].`when`)
        val smallIcon = Icon.createWithResource(packageName, smallIconResID).setTint(Color.GRAY)

        holder.appName.text = appName
        holder.notifyText.text = text
        holder.smallIcon.setImageDrawable(smallIcon.loadDrawable(context))
        holder.date.text = SimpleDateFormat("h:mm a", Locale.KOREA).format(date)
        //#region test

//        data[position].extra = data[position].extra!!.replace("[","")
//        data[position].extra = data[position].extra!!.replace("]","")
//        data[position].extra = data[position].extra!!.replace("{","")
//        data[position].extra = data[position].extra!!.replace("}","")
//        data[position].extra = data[position].extra!!.replace("Bundle","")
//        val arrExtra  = data[position].extra!!.split(",")
//        var extraContent = ""
//        val childListData : MutableList<NotifyChildListAdapter.Data> = mutableListOf()
//        for(extra in arrExtra){
//            val title = "${extra.split("=")[0].replace(" ","")}"
//            val content = if(extra.split("=").count() > 1) "${extra.split("=")[1].replace('\n',' ')}" else ""
//            childListData.add(NotifyChildListAdapter.Data(title, content))
//        }
//        holder.childList.adapter = NotifyChildListAdapter(childListData)
//        holder.childList.adapter!!.notifyDataSetChanged()
        holder.packageName.text = data[position].packageName

        //#endregion

        holder.itemView.contentLayout.setOnClickListener {
            openApp(context, data[position].packageName)
        }

        if(title != null)
            holder.title.text = title
        else
            holder.title.visibility = View.GONE
    }

    private fun openApp(context: Context, packageName: String?): Boolean {
        val manager = context.packageManager
        return try {
            val i = manager.getLaunchIntentForPackage(packageName!!)
                    ?: throw PackageManager.NameNotFoundException()
            i.addCategory(Intent.CATEGORY_LAUNCHER)
            val bundle = Bundle()
            bundle.putString("chartLogId", "23949708667")
            i.putExtras(bundle)
            context.startActivity(i)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getItemCount(): Int {
        if(data == null) return 0
        return data.size
    }
}