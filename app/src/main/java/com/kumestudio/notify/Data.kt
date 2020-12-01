package com.kumestudio.notify

import androidx.room.ColumnInfo
import com.kumestudio.notify.db.NotificationData

class Data {
    data class NotificationGroup(
            val appName : String,
            val text : String,
            val `when` : Long,
            val smallIcon : Int,
            val packageName : String,
            val childNotifications : MutableList<NotificationData>,
            val type : Int
    ){
        var title : String? = null
    }

}