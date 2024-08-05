package com.base.hybridmvvm.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.CalendarView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.base.hybridmvvm.R
import com.base.hybridmvvm.databinding.ActivityMainBinding
import com.base.hybridmvvm.ui.base.BaseActivity
import com.base.hybridmvvm.ui.base.SharedViewModel
import com.base.hybridmvvm.utils.CalendarEvent
import com.base.hybridmvvm.utils.CalendarUtils
import com.base.hybridmvvm.utils.DialogUtils
import com.base.hybridmvvm.utils.ImageUtils
import com.base.hybridmvvm.utils.PermissionUtils
import com.base.hybridmvvm.utils.SystemUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val mainAppInterfaceName = "MainAppInterface"

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>
    lateinit var pickSingleImageLauncher: ActivityResultLauncher<String>
    lateinit var pickMultipleImagesLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel 데이터 Observe
        observeViewModel()
        // Launcher 관련 initialize
        initializeActivityResultLaunchers()
        // 웹뷰 설정
        setWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        /**
         * 웹뷰 지연 로딩일 경우 백화현상 테스트를 위해.
         */
        binding.mainWebView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        val webView: WebView = binding.mainWebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(
            MainAppInterface(this, webView, viewModel, sharedViewModel, this),
            mainAppInterfaceName
        )
        webView.loadUrl("file:///android_asset/TestPage.html")
//        webView.loadUrl("https://stackoverflow.com/")

    }

    private fun observeViewModel() {
        viewModel.deniedPermissions.observe(this) { deniedPermissions ->
            showPermissionRationale(deniedPermissions)
        }

        viewModel.showRationaleDialog.observe(this) { rationaleData ->
            rationaleData?.let {
                showPermissionRationaleDialog(it.deniedPermissions, it.showRationale)
            }
        }
        viewModel.calendarEvents.observe(this) { events ->
            if (events.isNotEmpty()) {
                showCalendarPopup(events)
            }
        }

        viewModel.base64Image.observe(this) { base64Image ->
            callbackScript("handlePhotoUri", base64Image)
        }

        viewModel.shareContent.observe(this) { content ->
            SystemUtils.shareContent(this, content.subject, content.text, content.chooserTitle)
        }
    }

    private fun showPermissionRationale(deniedPermissions: List<String>) {
        lifecycleScope.launch {
            val showRationale = deniedPermissions.any { permission ->
                viewModel.shouldShowRationale(this@MainActivity, permission)
            }
            viewModel.onRationaleCalculated(deniedPermissions, showRationale)
        }
    }

    private fun showPermissionRationaleDialog(
        deniedPermissions: List<String>,
        showRationale: Boolean
    ) {
        val rationalePermissions = PermissionUtils.getRationalePermissions(deniedPermissions)

        DialogUtils.showPermissionDialog(
            context = this,
            title = "권한 요청",
            content = "필수 $rationalePermissions 권한이 허용 되지 않았습니다.",
            positiveButtonText = if (showRationale) "확인" else "환경 설정",
            onPositiveButtonClick = {
                if (showRationale) {
                    viewModel.requestPermission(this, deniedPermissions)
                } else {
                    viewModel.openAppSettings(this)
                }
            },
            negativeButtonText = "취소",
            onNegativeButtonClick = {},
        )
    }

    private fun initializeActivityResultLaunchers() {
        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    sharedViewModel.photoUri.value?.let {
                        callbackScript("test", it.toString())
                        viewModel.processImage(this, it)
                    }
                }
            }

        pickSingleImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    callbackScript("test", it.toString())
                    viewModel.processImage(this, it)
                }
            }

        pickMultipleImagesLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val uris = mutableListOf<Uri>()
                    result.data?.clipData?.let {
                        for (i in 0 until it.itemCount) {
                            uris.add(it.getItemAt(i).uri)
                        }
                    } ?: result.data?.data?.let {
                        uris.add(it)
                    }
                    callbackScript("handleSelectedUris", uris.toString())
                }
            }
    }

    fun showCalendarPopup(events: List<CalendarEvent>) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.calendar_popup)

        val calendarView = dialog.findViewById<CalendarView>(R.id.calendarView)
        val closeButton = dialog.findViewById<Button>(R.id.closeButton)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDateString = sdf.format(Date(selectedDate))

            val eventsOnSelectedDate = events.filter {
                val eventDateString = sdf.format(Date(it.startTime))
                eventDateString == selectedDateString
            }

            eventsOnSelectedDate.forEach {
                // 뷰 연동 시 캘린더 커스텀 필요 ( 시간소요 이슈 )
                Log.d("날짜별 정보", "제목: ${it.title}, 설명: ${it.description}")
            }
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroy() {
        binding.mainWebView.removeJavascriptInterface(mainAppInterfaceName)
        super.onDestroy()
    }

    private fun callbackScript(callbackName: String, data: String) {
        binding.mainWebView.evaluateJavascript("javascript:$callbackName('$data')", null)
    }
}