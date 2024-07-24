package com.base.hybridmvvm.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import com.base.hybridmvvm.databinding.ActivityMainBinding
import com.base.hybridmvvm.ui.base.BaseActivity
import com.base.hybridmvvm.ui.loading.LoadingDialog
import com.base.hybridmvvm.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel 데이터 Observe
        observeViewModel()

        val webView: WebView = binding.mainWebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(MainAppInterface(this, viewModel), "MainAppInterface")
        webView.loadUrl("file:///android_asset/TestPage.html")
        
    }


    private fun observeViewModel(){
        viewModel.permissionsGranted.observe(this) { granted ->
            if (granted) {
                Toast.makeText(this,"모든권한이 수락되었습니다.",Toast.LENGTH_LONG).show()
            }
        }

        viewModel.deniedPermissions.observe(this) { deniedPermissions ->
            showPermissionRationale(deniedPermissions)
        }
    }

    private fun showPermissionRationale(deniedPermissions: List<String>) {
        val rationalePermissions = PermissionUtils.getRationalePermissions(deniedPermissions)
        val showRationale = deniedPermissions.any { permission ->
            viewModel.shouldShowRationale(this, permission)
        }

        AlertDialog.Builder(this)
            .setTitle("권한요청")
            .setMessage("필수 $rationalePermissions 권한이 수락되지 않았습니다.")
            .setPositiveButton(if (showRationale) "확인" else "환경 설정") { _, _ ->
                if (showRationale) {
                    viewModel.requestPermission(this, deniedPermissions)
                } else {
                    viewModel.openAppSettings(this)
                }
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

}