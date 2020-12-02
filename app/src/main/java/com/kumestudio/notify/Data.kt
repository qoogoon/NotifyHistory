package com.kumestudio.notify

import androidx.room.ColumnInfo
import com.kumestudio.notify.db.NotificationData

class Data {
    companion object{
        val NOTIFICATION = 0
        val TIME = 1
        val DATE = 2
    }
    data class NotificationGroup(
            val type : Int,
            val `when` : Long,
            val childNotifications : MutableList<NotificationData>? = null
    )
}