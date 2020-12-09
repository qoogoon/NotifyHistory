package com.kumestudio.notification.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.kumestudio.notification.R
import com.kumestudio.notification.constant.MyTag
import com.kumestudio.notification.service.NotificationListener
import com.kumestudio.notification.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    companion object{
        var mainFragment: MainFragment= MainFragment.newInstance()
        var active : Boolean = false
        var instance : MainActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        active = true
        instance = this
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()
    }

    override fun onStart() {
        super.onStart()
        if (isNotificationPermissionAllowed()){
            if(!NotificationListener.isRunning)
                startService(Intent(applicationContext, NotificationListener::class.java))
        }else
            startActivity(Intent(this, PermissionActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        active = false
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater: MenuInflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.blackList -> {
//                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

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