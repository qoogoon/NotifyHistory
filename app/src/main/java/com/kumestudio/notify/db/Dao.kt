package com.kumestudio.notify.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationDao {
    @Query("SELECT * FROM NotificationData order by `when` desc")
    fun getAll(): List<NotificationData>

    @Insert
    fun insert(notification: NotificationData)

    @Insert
    fun insertAll(vararg notifications: NotificationData)

    @Delete
    fun delete(notification: NotificationData)
}