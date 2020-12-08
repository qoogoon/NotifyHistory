package com.kumestudio.notification.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotificationData::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
