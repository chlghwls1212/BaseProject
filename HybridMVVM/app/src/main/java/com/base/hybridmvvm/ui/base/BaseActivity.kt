package com.base.hybridmvvm.ui.base

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.base.hybridmvvm.utils.PermissionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.coroutines.resumeWithException

abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val SCREEN_CAPTURE = "screenshot.png"
        const val SCREEN_PATH = "SCREENSHOT_PATH"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 공통 초기화 코드

        // 뒤로가기 컨트롤 영역 설정
        setupBackButtonHandler()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * setupBackButtonHandler
     * 뒤로 가기 버튼 핸들러를 설정.
     */
    private fun setupBackButtonHandler() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                finish()
            }
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            })
        }
    }

    /**
     * captureAndTransitionToActivity
     * 현재 화면을 캡처하고 대상 액티비티로 전환
     * @param targetActivity 전환할 대상 액티비티 클래스
     */
    fun captureAndTransitionToActivity(targetActivity: Class<out AppCompatActivity>) {
        lifecycleScope.launch {
            val bitmap = captureScreenWithPixelCopy()
            if (bitmap != null) {
                val filePath = saveBitmapToFile(bitmap)
                transitionToActivityWithPath(targetActivity, filePath)
            } else {
                Timber.tag("BaseActivity").e("Failed to capture screen.")
            }
        }
    }

    /**
     * transitionToActivity
     * 캡처된 스크린샷과 함께 지정된 액티비티로 전환
     * @param targetActivity 전환할 대상 액티비티 클래스
     * @param filePath 캡처된 스크린샷 파일 경로
     */
    private fun transitionToActivityWithPath(
        targetActivity: Class<out AppCompatActivity>,
        filePath: String?
    ) {
        val intent = Intent(this, targetActivity).apply {
            putExtra(SCREEN_PATH, filePath)
        }
        val options = ActivityOptionsCompat.makeCustomAnimation(this, 0, 0)
        startActivity(intent, options.toBundle())
        finish()
    }

    /**
     * captureScreenWithPixelCopy
     * PixelCopy를 사용하여 현재 화면을 캡처
     * @return 캡처된 비트맵, 캡처 실패 시 null 반환
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun captureScreenWithPixelCopy(): Bitmap? {
        return withContext(Dispatchers.Main) {
            try {
                val rootView: View = window.decorView.rootView
                val bitmap =
                    Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
                suspendCancellableCoroutine { cont ->
                    PixelCopy.request(window, bitmap, { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            cont.resume(bitmap) {}
                        } else {
                            cont.resumeWithException(RuntimeException("PixelCopy 이미지 저장 실패 $copyResult"))
                        }
                    }, Handler(Looper.getMainLooper()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * saveBitmapToFile
     * 캡처된 비트맵을 파일에 저장
     * @param bitmap 저장할 비트맵
     * @return 저장된 비트맵 파일 경로, 저장 실패 시 null 반환
     */
    private fun saveBitmapToFile(bitmap: Bitmap): String? {
        return try {
            val file = File(getExternalFilesDir(null), SCREEN_CAPTURE)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


}
