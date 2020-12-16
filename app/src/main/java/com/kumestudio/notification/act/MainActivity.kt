package com.kumestudio.notification.act

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.kumestudio.notification.R
import com.kumestudio.notification.service.NotificationListener
import com.kumestudio.notification.ui.main.MainFragment
import kotlinx.android.synthetic.main.dialog_policy.*
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object{
        var mainFragment: MainFragment= MainFragment.newInstance()
        var active : Boolean = false
        var instance : MainActivity? = null
    }
    private lateinit var preference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        active = true
        instance = this
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commitNow()

        preference = getSharedPreferences("APP_SETTING", Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        if (isNotificationPermissionAllowed()){
            if(!NotificationListener.isRunning)
                startService(Intent(applicationContext, NotificationListener::class.java))
        }else
            startActivity(Intent(this, PermissionActivity::class.java))

        if(!preference.getBoolean("policyAgree", false)){
            if(Locale.getDefault().displayLanguage == Locale.KOREA.language){

            }
//            showPolicyDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        active = false
    }


    //이용약관 다이얼로그
    private fun showPolicyDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_policy)

        //버튼
        dialog.policyAgreeBtn.isEnabled = false
        dialog.policyAgreeBtn.setOnClickListener {
            val editor = preference.edit()
            editor.putBoolean("policyAgree", true)
            editor.apply()
            dialog.dismiss()
        }

        //체크박스
        dialog.policyCheck.setOnCheckedChangeListener {_, isChecked ->
            dialog.policyAgreeBtn.isEnabled = isChecked
        }

        //web
        dialog.policyWeb.loadUrl(getString(R.string.policy_url))
        dialog.show()

        //동의 안한 상태에서 다이얼로그 죽이면 어플도 죽이기
        dialog.setOnDismissListener {
            if (!preference.getBoolean("policyAgree", false)) {
                this@MainActivity.finish()
            }
        }
    }
    //endregion

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