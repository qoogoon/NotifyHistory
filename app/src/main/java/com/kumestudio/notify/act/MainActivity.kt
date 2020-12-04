package com.kumestudio.notify.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import com.kumestudio.notify.R
import com.kumestudio.notify.service.NotificationListener
import com.kumestudio.notify.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    companion object{
        var mainFragment: MainFragment= MainFragment.newInstance()
        var active : Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        active = true
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
        }

        if (!isNotificationPermissionAllowed())
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));

        startService(Intent(applicationContext, NotificationListener::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        active = false
    }

    /**
     * Notification 접근 권한 체크 메서드
     * @return 접근권한이 있을 경우 true, 아니면 false
     */
    private fun isNotificationPermissionAllowed(): Boolean {
        return NotificationManagerCompat.getEnabledListenerPackages(applicationContext)
            .any { enabledPackageName ->
                enabledPackageName == packageName
            }
    }
}