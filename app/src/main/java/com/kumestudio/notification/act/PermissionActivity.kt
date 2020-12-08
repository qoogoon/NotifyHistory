package com.kumestudio.notification.act

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kumestudio.notification.R
import kotlinx.android.synthetic.main.activity_permission.*

class PermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        permissionSettingBtn.setOnClickListener {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(MainActivity.instance == null) return
        MainActivity.instance!!.finish()
    }
}