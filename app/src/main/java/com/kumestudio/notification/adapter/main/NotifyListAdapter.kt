package com.kumestudio.notification.adapter.main

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
import androidx.room.Room
import com.kumestudio.notification.Data
import com.kumestudio.notification.R
import com.kumestudio.notification.db.AppDatabase
import com.kumestudio.notification.ui.main.MainFragment
import kotlinx.android.synthetic.main.main_item_date.view.*
import kotlinx.android.synthetic.main.main_item_header.view.*
import kotlinx.android.synthetic.main.main_item_notification.view.*
import kotlinx.android.synthetic.main.main_item_time.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NotifyListAdapter(var data: MutableList<Data.NotificationGroup>, var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class HeaderViewHolder constructor(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item_header, parent, false))

    inner class DateViewHolder constructor(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item_date, parent, false))

    inner class TimeViewHolder constructor(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item_time, parent, false))

    inner class ContentViewHolder constructor(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item_notification, parent, false))

    val db = Room.databaseBuilder(context, AppDatabase::class.java, "database").build()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Data.HEADER -> HeaderViewHolder(parent)
            Data.NOTIFICATION -> ContentViewHolder(parent)
            Data.TIME -> TimeViewHolder(parent)
            Data.DATE -> DateViewHolder(parent)

            else -> TimeViewHolder(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(data[position].type){
            Data.DATE ->{
                val date = Date(data[position].`when`)
                holder.itemView.headDate.text = SimpleDateFormat("MM / dd ", Locale.KOREA).format(date)
            }
            Data.TIME -> {
                val date = Date(data[position].`when`)
                holder.itemView.time.text = SimpleDateFormat("a h", Locale.KOREA).format(date)
            }
            Data.NOTIFICATION ->{
                setNotificationItem(holder, position)
            }
            Data.HEADER -> {
                setHeaderItem(holder)
            }
        }
    }

    private fun setHeaderItem(holder: RecyclerView.ViewHolder){
        val infoText =  if(data.count() == 1) "알림을 기다리고 있습니다. 설치 전 알림은 알 수 없습니다." else getRandomInfoText()
        holder.itemView.infoText.text = infoText
    }

    private fun getRandomInfoText() : String{
        val infoList = listOf(
                "저장 된 알림은 3일간 유지 되며, 이후 순차적으로 삭제 됩니다.",
                "알림을 터치하면 확장됩니다.",
                "저장 된 알림은 기기 내에 저장 되며, 어플을 삭제 시 같이 제거됩니다."
                )
        return infoList[(Date().time % infoList.count()).toInt()]
    }

    private fun setNotificationItem(holder: RecyclerView.ViewHolder, position: Int){
        val childFirstData = data[position].childNotifications!!.first()
        val text = childFirstData.text
        val title = childFirstData.title
        val smallIconResID = childFirstData.smallIcon
        val packageName = childFirstData.packageName
        val appName = childFirstData.appName
        val date = Date(childFirstData.`when`)
        val smallIcon = Icon.createWithResource(packageName, smallIconResID).setTint(Color.GRAY)

        holder.itemView.appName.text = appName
        holder.itemView.text.text = text
        holder.itemView.smallIcon.setImageDrawable(smallIcon.loadDrawable(context))
        holder.itemView.date.text = SimpleDateFormat("h:mm a", Locale.KOREA).format(date)
        holder.itemView.packageName.text = childFirstData.packageName
        holder.itemView.childList.adapter = NotifyChildListAdapter(data[position].childNotifications!!.drop(1))
        holder.itemView.childList.adapter!!.notifyDataSetChanged()

        holder.itemView.itemLayout.setOnClickListener(getExpandListener(holder.itemView, position))
        holder.itemView.expandButton.setOnClickListener(getExpandListener(holder.itemView, position))

        if (data[position].isExpanding) {
            holder.itemView.detailLayout.expand(false)
            holder.itemView.text.maxLines = 100
            holder.itemView.expandButton.rotation = 180f
        } else {
            holder.itemView.text.maxLines = 1
            holder.itemView.detailLayout.collapse(false)
            holder.itemView.expandButton.rotation = 270f
        }

        holder.itemView.headerLayout.setOnClickListener {
            openApp(context, childFirstData.packageName)
        }

        holder.itemView.alarmSettingBtn.setOnClickListener {
            val packageName = data[position].childNotifications!![0].packageName
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            context.startActivity(intent)
        }

        holder.itemView.alarmDeleteBtn.setOnClickListener {
            val deleteList = data[position].childNotifications!!
            CoroutineScope(Dispatchers.IO).launch {
                if (MainFragment.viewModel == null) return@launch
                //DB에서 제거
                deleteList.forEach { data ->
                    db.notificationDao().delete(data)
                }

                //UI에서 제거
                CoroutineScope(Dispatchers.Main).launch {
                    val deletedList = MainFragment.viewModel!!.listData.value!!.filter { data -> !(deleteList.contains(data)) }
                    MainFragment.viewModel!!.listData.value = deletedList.toMutableList()
                }
            }
        }

        if (title != null)
            holder.itemView.title.text = title
        else
            holder.itemView.title.visibility = View.GONE
    }

    private fun getExpandListener(parentView: View, position: Int): View.OnClickListener {
        return View.OnClickListener {
            data[position].isExpanding = !data[position].isExpanding
            parentView.detailLayout.toggle()
            notifyItemChanged(position)
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
        return data.size
    }
}