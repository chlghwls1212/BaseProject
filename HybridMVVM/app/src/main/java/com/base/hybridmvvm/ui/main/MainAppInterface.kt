package com.base.hybridmvvm.ui.main

import android.app.Activity
import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.base.hybridmvvm.data.model.dangerousPermissions
import com.base.hybridmvvm.ui.loading.LoadingDialog
import com.base.hybridmvvm.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainAppInterface(private val context: Context, private val viewModel: MainViewModel) {
    @JavascriptInterface
    fun showRequestPermission() {
        val permissionsList = dangerousPermissions.flatMap { it.permissions }
        viewModel.requestPermission(context as Activity, permissionsList)
    }

    @JavascriptInterface
    fun showToast(message: String) {
        ToastUtils.showToast(
            context,
            "긴단어가 나와야하는걸 왜 모르는거니단어가 나와야하는걸 왜 모르는거니단어가 나와야하는걸 왜 모르는거니",
            3000,
            false
        )
    }

    @JavascriptInterface
    fun showLoading() {
        CoroutineScope(Dispatchers.Main).launch {
            LoadingDialog.showLoading(context)
            delay(3000)
            LoadingDialog.hideLoading()
        }
    }

    @JavascriptInterface
    fun doSomething(data: String) {
        // ViewModel을 사용하여 데이터 처리
        viewModel.loadData()
    }
}