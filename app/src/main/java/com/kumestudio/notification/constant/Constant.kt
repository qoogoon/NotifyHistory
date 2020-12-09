package com.kumestudio.notification.constant

import java.util.*

object Constant {
    const val dateToList = 3    //리스트에 표시 할 일간(현재시간기준)
    const val dateToSaveInDB = 30   //DB에 데이터를 남길 일간(현재시간기준)
    const val dayMs :Long = 1000 * 60 * 60 * 24
    fun getHour(dateTime : Long): Int{
        val firstDataTime = Calendar.getInstance()
        firstDataTime.time = Date(dateTime)
        return firstDataTime.get(Calendar.HOUR_OF_DAY)
    }

    fun getDay(dateTime : Long): Int{
        val firstDataTime = Calendar.getInstance()
        firstDataTime.time = Date(dateTime)
        return firstDataTime.get(Calendar.DAY_OF_MONTH)
    }
}