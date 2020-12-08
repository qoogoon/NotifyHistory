package com.kumestudio.notification

import com.kumestudio.notification.db.NotificationData

class Data {
    companion object{
        val NOTIFICATION = 0
        val TIME = 1
        val DATE = 2
    }
    data class NotificationGroup(
            val type : Int,
            val `when` : Long,
            var isExpanding : Boolean = false,
            val childNotifications : MutableList<NotificationData>? = null
    )
}