package com.mh.circutversionupdate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.mh.circutversionupdate.utils.Constant
import com.mh.circutversionupdate.activity.HomeActivity
import com.mh.circutversionupdate.activity.LoginActivity
import com.mh.circutversionupdate.utils.AppStorage


class SplashActvity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullscreen()
        setContentView(R.layout.activity_main)
        AppStorage.init(this)
        addHandler()

    }
    private fun checkSession() {
        if (AppStorage.getUser()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()

        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }

    }

    private fun addHandler() {

        Handler().postDelayed({
            //  startActivity(Intent(this,AddItemsActivity::class.java))

            checkSession()

        }, Constant.SPLASH_TIMEOUT.toLong())
    }

    private fun fullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

    }
}