package com.pinslog.ww.presentation.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import com.pinslog.ww.base.BaseActivity
import com.pinslog.ww.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getBinding(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    override fun initSetting() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

}