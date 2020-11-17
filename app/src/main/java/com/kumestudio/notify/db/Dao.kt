package com.kumestudio.notify.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationDao {
    @Query("SELECT * FROM NotificationData")
    fun getAll(): List<NotificationData>

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>

//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User

    @Insert
    fun insert(notification: NotificationData)

    @Insert
    fun insertAll(vararg notifications: NotificationData)

    @Delete
    fun delete(notification: NotificationData)
}