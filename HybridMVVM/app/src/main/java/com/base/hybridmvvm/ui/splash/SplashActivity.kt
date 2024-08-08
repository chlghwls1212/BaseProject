package com.base.hybridmvvm.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.activity.viewModels
import com.base.hybridmvvm.databinding.ActivitySplashBinding
import com.base.hybridmvvm.ui.base.BaseActivity
import com.base.hybridmvvm.ui.permission.PermissionActivity
import com.base.hybridmvvm.utils.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        webView = WebView(this).apply {
//            loadUrl("https://stackoverflow.com/")
//        }

        viewModel.splashResponse.observe(this) { response ->
            response?.let {
                if (!it.serverStatus)
                    ToastUtils.showToast(this, "서버가 불안정 합니다.", 3000)
            }
        }

        viewModel.splashError.observe(this) { error ->
            error?.let {
                ToastUtils.showToast(this, "서버가 불안정 합니다. \n $it", 3000)
            }
        }

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