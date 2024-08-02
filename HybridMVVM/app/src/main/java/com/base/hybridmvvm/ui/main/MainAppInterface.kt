package com.base.hybridmvvm.ui.main

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.LifecycleOwner
import com.base.hybridmvvm.data.model.ImagePermission
import com.base.hybridmvvm.data.model.dangerousPermissions
import com.base.hybridmvvm.data.model.locationPermission
import com.base.hybridmvvm.ui.base.SharedViewModel
import com.base.hybridmvvm.ui.custom.LoadingDialog
import com.base.hybridmvvm.utils.DeviceInfoUtils
import com.base.hybridmvvm.utils.LocationUtils
import com.base.hybridmvvm.utils.PreferenceUtils
import com.base.hybridmvvm.utils.SystemUtils
import com.base.hybridmvvm.utils.ToastUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainAppInterface(
    private val context: Context,
    private val webView: WebView,
    private val viewModel: MainViewModel,
    private val sharedViewModel: SharedViewModel,
    private val lifecycleOwner: LifecycleOwner
) {
    @JavascriptInterface
    fun showRequestPermission() {
        val permissionsList = dangerousPermissions.flatMap { it.permissions }
        viewModel.requestPermission(context, permissionsList)
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

    @JavascriptInterface
    fun getDeviceInfo() {
        ToastUtils.showToast(
            context,
            DeviceInfoUtils.getDeviceInfo(context).toString(),
            3000,
            true
        )
    }

    @JavascriptInterface
    fun setPrefString() {
        PreferenceUtils.putString(context, "ID", "chlghwls+${Math.random()}")
    }

    @JavascriptInterface
    fun getPrefString() {
        ToastUtils.showToast(
            context,
            PreferenceUtils.getString(context, "ID").toString(),
            3000,
            true
        )
    }

    @JavascriptInterface
    fun exitApp() {
        SystemUtils.exitApp(context)
    }

    @JavascriptInterface
    fun getLocationWithGPS() {
        viewModel.requestPermission(context, locationPermission)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.permissionsGranted.observeForever { granted ->
                if (granted) {
                    LocationUtils.getLastLocation(context) {
                        ToastUtils.showToast(context, it.toString())
                    }
                    viewModel.permissionsGranted.removeObserver { }
                }
            }
        }
    }

    @JavascriptInterface
    fun getPicImage(isCapture: Boolean = true, isSingle: Boolean = true) {
        viewModel.requestPermission(context, ImagePermission)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.permissionsGranted.observe(lifecycleOwner) { granted ->
                if (granted) {
                    if (isCapture) {
                        (context as MainActivity).takePhotoLauncher.launch(
                            sharedViewModel.createImageUri(context)
                        )
                    } else if (isSingle) {
                        (context as MainActivity).pickSingleImageLauncher.launch("image/*")
                    } else {
                        val intent = sharedViewModel.createPickMultipleImagesIntent()
                        (context as MainActivity).pickMultipleImagesLauncher.launch(intent)
                    }
                    viewModel.permissionsGranted.removeObservers(lifecycleOwner)
                }
            }
        }
    }

    @JavascriptInterface
    fun callbackScript(callback: String, json: String) {
        webView.post {
            webView.evaluateJavascript("javascript:$callback('$json')", null)
        }
    }

    @JavascriptInterface
    fun showCalendar(){
        viewModel.requestCalendarEvents()
    }

}
