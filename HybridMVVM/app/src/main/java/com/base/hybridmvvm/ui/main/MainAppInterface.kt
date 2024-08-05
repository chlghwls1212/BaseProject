package com.base.hybridmvvm.ui.main

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.LifecycleOwner
import com.base.hybridmvvm.data.model.ImagePermission
import com.base.hybridmvvm.data.model.dangerousPermissions
import com.base.hybridmvvm.data.model.locationPermission
import com.base.hybridmvvm.ui.base.BaseViewModel
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

/**
 * MainAppInterface
 *
 * 비즈니스 로직이 필요한 경우 혹은 데이터의 조작 및 업데이트가 필요 할 경우 ViewModel을 경유,
 * 이 외의 경우 단순 UI 업데이트 경우 바로 View 에서 호출.
 *
 * @property context
 * @property webView
 * @property viewModel
 * @property sharedViewModel
 * @property lifecycleOwner
 */
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
    fun showToast(message: String, durationTime: Int) {
        ToastUtils.showToast(
            context,
            "긴단어가 나와야하는걸 왜 모르는거니단어가 나와야하는걸 왜 모르는거니단어가 나와야하는걸 왜 모르는거니",
            3000,
            false
        )
    }

    @JavascriptInterface
    fun showLoading() {
        //테스트용으로 생성 후 삭제
        CoroutineScope(Dispatchers.Main).launch {
            LoadingDialog.showLoading(context)
            delay(3000)
            LoadingDialog.hideLoading()
        }
    }

    @JavascriptInterface
    fun getDeviceInfo() {
        val deviceInfo = DeviceInfoUtils.getDeviceInfo(context)

        ToastUtils.showToast(
            context,
            deviceInfo.toString(),
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
            viewModel.permissionsGranted.observe(lifecycleOwner) { granted ->
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
    fun showCalendar() {
        viewModel.requestCalendarEvents()
    }

    @JavascriptInterface
    fun sendSMS(
        phoneNumber: String,
        message: String,
        chooserTitle: String? = "문자 메시지 보내기"
    ) {
        SystemUtils.sendSMS(context, phoneNumber, message, chooserTitle)
    }

    @JavascriptInterface
    fun phoneCall(phoneNumber: String) {
        SystemUtils.makePhoneCall(context, phoneNumber)
    }

    /**
     *  데이터 모델 사용 예시 함수.
     *  MainAppInterface 의 규칙에 따라 간단한 UI update등 간단한 함수는 ViewModel을 경유하지 아니한다.
     */
    @JavascriptInterface
    fun makeShareContent(
        subject: String,
        text: String,
        chooserTitle: String? = "공유하기"
    ) {
        viewModel.shareContent(subject, text, chooserTitle)
    }

    @JavascriptInterface
    fun callbackScript(callback: String, json: String) {
        webView.post {
            webView.evaluateJavascript("javascript:$callback('$json')", null)
        }
    }

}
