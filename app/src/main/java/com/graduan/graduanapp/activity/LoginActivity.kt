package com.graduan.graduanapp.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.graduan.graduanapp.AccountManager
import com.graduan.graduanapp.MainActivity
import com.graduan.graduanapp.R
import com.graduan.graduanapp.databinding.ActivityLoginBinding
import com.graduan.graduanapp.model.LoginModel
import com.graduan.graduanapp.utils.LoadingDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    var loginBinding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view: View = loginBinding!!.root
        setContentView(view)
        init()
    }

    private fun init() {
        //for testing only
        loginBinding!!.etName.setText("isabell.mccullough@example.org")
        loginBinding!!.etPassword.setText("password")

        loginBinding!!.btnLogin.setOnClickListener {
            LoadingDialog.getInstance().with(this@LoginActivity).isLoading

            CoroutineScope(Dispatchers.IO).launch {
                loginUser()
                LoadingDialog.getInstance().with(this@LoginActivity).dismissLoading()
            }
        }
    }

    private suspend fun loginUser() {
        withContext(Main) {
            val loginModel = LoginModel(
                loginBinding!!.etName.text.toString(),
                loginBinding!!.etPassword.text.toString()
            )
            val res = AccountManager.instance.login(this@LoginActivity, loginModel)
            if (res) {
                AccountManager.instance.setSingleSignOn(res)
                AccountManager.instance.storeAccount()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}