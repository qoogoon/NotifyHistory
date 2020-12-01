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
import androidx.recyclerview.widget.RecyclerView
import com.kumestudio.notify.Data
import com.kumestudio.notify.R
import kotlinx.android.synthetic.main.main_item_notification.view.*
import kotlinx.android.synthetic.main.main_item_time.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotifyListAdapter(var data  :MutableList<Data.NotificationGroup>, var context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class DateViewHolder constructor(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).
        inflate(R.layout.main_item_notification, parent, false))

    inner class ContentViewHolder constructor(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).
        inflate(R.layout.main_item_notification, parent, false))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0 -> ContentViewHolder(parent)
            else -> ContentViewHolder(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(data[position].type == 1){
            holder.itemView.time.text = data[position].title
        }else{
            val text = data[position].text
            val title = data[position].title
            val smallIconResID = data[position].smallIcon
            val packageName = data[position].packageName
            val appName = data[position].appName
            val date = Date(data[position].`when`)
            val smallIcon = Icon.createWithResource(packageName, smallIconResID).setTint(Color.GRAY)

            holder.itemView.appName.text = appName
            holder.itemView.text.text = text
            holder.itemView.smallIcon.setImageDrawable(smallIcon.loadDrawable(context))
            holder.itemView.date.text = SimpleDateFormat("h:mm a", Locale.KOREA).format(date)
            //#region test
//       data[position].extra = data[position].extra!!.replace("[","")
//       data[position].extra = data[position].extra!!.replace("]","")
//       data[position].extra = data[position].extra!!.replace("{","")
//       data[position].extra = data[position].extra!!.replace("}","")
//       data[position].extra = data[position].extra!!.replace("Bundle","")
//       val arrExtra  = data[position].extra!!.split(",")
//       var extraContent = ""
//       val childListData : MutableList<NotifyChildListAdapter.Data> = mutableListOf()
//       for(extra in arrExtra){
//           val title = "${extra.split("=")[0].replace(" ","")}"
//           val content = if(extra.split("=").count() > 1) "${extra.split("=")[1].replace('\n',' ')}" else ""
//           childListData.add(NotifyChildListAdapter.Data(title, content))
//       }
//       holder.childList.adapter = NotifyChildListAdapter(childListData)
//       holder.childList.adapter!!.notifyDataSetChanged()
            holder.itemView.packageName.text = data[position].packageName
            holder.itemView.childList.adapter = NotifyChildListAdapter(data[position].childNotifications)
            holder.itemView.childList.adapter!!.notifyDataSetChanged()
            //#endregion

            holder.itemView.expandButton.setOnClickListener {
                holder.itemView.detailLayout.toggle()
            }

            holder.itemView.contentLayout.setOnClickListener {
                openApp(context, data[position].packageName)
            }

            if(title != null)
                holder.itemView.title.text = title
            else
                holder.itemView.title.visibility = View.GONE
        }


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