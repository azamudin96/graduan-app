package com.graduan.graduanapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.graduan.graduanapp.AccountManager
import com.graduan.graduanapp.MainActivity
import com.graduan.graduanapp.R
import com.graduan.graduanapp.databinding.ActivityLoginBinding
import com.graduan.graduanapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private var splashBinding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        val view: View = splashBinding!!.root
        setContentView(view)

        initUi()
    }

    private fun initUi() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (AccountManager.instance.isSingleSignOn()) {
                AccountManager.instance.loadAccount();
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }, 1000)
    }
}