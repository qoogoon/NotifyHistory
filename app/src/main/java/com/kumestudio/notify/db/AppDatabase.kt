package com.kumestudio.notify.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotificationData::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
