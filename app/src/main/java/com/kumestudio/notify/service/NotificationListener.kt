package com.kumestudio.notify.service

import android.app.ActivityManager
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.kumestudio.notify.act.MainActivity
import com.kumestudio.notify.constant.Tag
import com.kumestudio.notify.db.AppDatabase
import com.kumestudio.notify.db.NotificationData
import com.kumestudio.notify.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

/**
 * 알림 수신 시 Background에서 해당 데이터를 가공하여, 로컬 DB에 삽입하는 Service
 */
class NotificationListener : NotificationListenerService() {
    private lateinit var db : AppDatabase
    private var nullAbleColumns : List<String> = listOf(
        Notification.EXTRA_SUB_TEXT, Notification.EXTRA_TITLE)
    private val
            SYSTEM_ALARM_DEFAULT_TIME = 0L
    private val baseBlackList = listOf(
        "com.android.vending"   //Google Play 스토어
        , "com.google.android.gms"    //Google Play 서비스
        , "com.android.systemui"      //시스템UI
    )
    
    override fun onBind(intent: Intent?): IBinder? {
        Log.i(Tag.SERVICE,"onBind")
        return super.onBind(intent)
    }

    override fun onCreate() {
        Log.i(Tag.SERVICE,"onCreate")
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).build()
    }

    override fun getActiveNotifications(): Array<StatusBarNotification> {
        Log.i(Tag.SERVICE,"getActiveNotifications")
        return super.getActiveNotifications()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i(Tag.SERVICE,"onListenerConnected")

    }

    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationPosted(sbn, rankingMap)
        if(sbn == null)
            return

        val showWhen = sbn.notification.extras.get(Notification.EXTRA_SHOW_WHEN)
        val isShowWhen = showWhen != null && showWhen as Boolean
        if(!isShowWhen)
            return

        val isSystemAlarm = sbn.notification.`when` == SYSTEM_ALARM_DEFAULT_TIME
        if(isSystemAlarm)
            return

        val isNullText = sbn.notification.extras.get(Notification.EXTRA_TEXT) == null
        val isNullTitle = sbn.notification.extras.get(Notification.EXTRA_TITLE) == null
        if(isNullText && isNullTitle)
            return

        val template = sbn.notification.extras.get(Notification.EXTRA_TEMPLATE)
        val isMediaNotify = template == "android.app.Notification"+"$"+"MediaStyle"
        if(isMediaNotify)
            return

        if(baseBlackList.contains(sbn.packageName))
            return

        val notification = getNotificationData(sbn)

        CoroutineScope(Dispatchers.IO).launch {
            db.notificationDao().insert(notification)
            Log.i(Tag.SERVICE,"insert notification data : ${notification.packageName}")
            if(!MainActivity.active) return@launch
            Log.i(Tag.SERVICE,"refresh list")

            val notificationAll = db.notificationDao().getAll()
//            try{


                val viewModel = ViewModelProvider(MainActivity.mainFragment).get(MainViewModel::class.java)
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.listData.value = notificationAll.toMutableList()
                }

//            }catch (ex : java.lang.IllegalStateException){
//                Log.e(Tag.SERVICE, "viewmodel error : ${ex.message}")
//            }

        }
    }

    /**
     * 상태바의 알림정보로부터 Entity Row 객체생성
     * @param sbn 캡슐화 된 상태메시지 정보
     * @return 알림 Entity의 Row객체
     */
    private fun getNotificationData(sbn: StatusBarNotification): NotificationData {
        try{
            val appName = getApplicationName(sbn)
            val text = sbn.notification.extras.get(Notification.EXTRA_TEXT).toString()
            val `when` = sbn.notification.`when`
            val smallIcon = sbn.notification.smallIcon.resId
            val packageName = sbn.packageName

            val notification = NotificationData(appName, text, `when`, smallIcon, packageName)
            notification.extra = sbn.notification.extras.toString()
            nullAbleColumns.forEach { column->
                val value = sbn.notification.extras.get(column)
                if(value != null)
                    when(column){
                        Notification.EXTRA_TITLE ->
                            notification.title =  value.toString()
                        Notification.EXTRA_SUB_TEXT ->
                            notification.subText =  value.toString()
                        Notification.EXTRA_PROGRESS_INDETERMINATE->
                            notification.progress = value as Boolean
                    }
            }

            if(sbn.notification.getLargeIcon() != null){
                try {
                    notification.largeIcon =  sbn.notification.getLargeIcon().resId
                }catch (exception: IllegalStateException){
                    Log.i(Tag.SERVICE, "largeIcon resId 예외처리")
                }
            }
            return notification

        }catch (e: Exception){
            return NotificationData("error", e.message!!, Date().time, 0, packageName)
        }
    }

    /**
     * 상태바의 메시지 정보로부터 어플리케이션 이름 얻기
     * @param sbn 캡슐화 된 상태메시지 정보
     */
    private fun getApplicationName(sbn: StatusBarNotification): String{
        val pm = applicationContext.packageManager
        val ai: ApplicationInfo = pm.getApplicationInfo(sbn.packageName, 0)
        return pm.getApplicationLabel(ai).toString()
    }
}