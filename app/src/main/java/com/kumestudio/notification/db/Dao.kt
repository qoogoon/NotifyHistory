package com.kumestudio.notification.db

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

    /**
     * 이전 시간 제거
     * 특정 시각을 기준으로 이전의 알림을 모두 삭제
     */
    @Query("DELETE FROM NotificationData where `when` < :previousTime")
    fun deletePreviousTime(previousTime : Long)
}