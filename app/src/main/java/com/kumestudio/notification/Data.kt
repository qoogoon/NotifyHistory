package com.kumestudio.notification

import com.kumestudio.notification.db.NotificationData

class Data {
    companion object {
        val HEADER = 0
        val NOTIFICATION = 1
        val TIME = 2
        val DATE = 3
    }

    //    data class NotificationGroup(
//            val type : Int,
//            val `when` : Long = 0,
//            var isExpanding : Boolean = false,
//            val childNotifications : MutableList<NotificationData>? = null
//    )
    class NotificationGroup {
        var type: Int = 0
        var `when`: Long = 0
        var isExpanding: Boolean = false
        var childNotifications: MutableList<NotificationData>? = null

        constructor(type: Int) {
            this.type = type
        }

        constructor(type: Int, `when`: Long = 0) {
            this.type = type
            this.`when` = `when`
        }

        constructor(type: Int, `when`: Long = 0, isExpanding: Boolean = false, childNotifications: MutableList<NotificationData>? = null){
            this.type = type
            this.`when` = `when`
            this.isExpanding = isExpanding
            this.childNotifications = childNotifications
        }

    }


}