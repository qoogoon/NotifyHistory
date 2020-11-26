package com.kumestudio.notify.db

import android.graphics.drawable.Icon
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationData(
    @ColumnInfo(name="app_name") val appName : String,
    @ColumnInfo(name="text") val text : String,
    @ColumnInfo(name="when") val `when` : Long,
    @ColumnInfo(name="small_icon") val smallIcon : Int,
    @ColumnInfo(name="package_name") val packageName : String
    ){
    @PrimaryKey(autoGenerate = true) var seq : Int = 0
    @ColumnInfo(name="large_icon") var largeIcon : Int? = null
    @ColumnInfo(name="title") var title : String? = null
    @ColumnInfo(name="sub_text") var subText : String? = null
    @ColumnInfo(name="progress") var progress : Boolean ? = null
    @ColumnInfo(name="extra") var extra : String ? = null       //개발용(모든 상태 저장)

}