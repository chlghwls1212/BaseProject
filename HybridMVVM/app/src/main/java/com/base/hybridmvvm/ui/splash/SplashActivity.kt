package com.base.hybridmvvm.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.base.hybridmvvm.databinding.ActivitySplashBinding
import com.base.hybridmvvm.ui.base.BaseActivity
import com.base.hybridmvvm.ui.permission.PermissionActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            delay(2000) // 스플래시 화면 2초 대기
            withContext(Dispatchers.Main) {
                val intent = Intent(this@SplashActivity, PermissionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}