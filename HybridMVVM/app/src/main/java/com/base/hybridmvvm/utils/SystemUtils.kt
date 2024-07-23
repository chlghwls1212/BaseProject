package com.base.hybridmvvm.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat.finishAffinity
import kotlin.system.exitProcess

object SystemUtils {
    /**
     * moveAppToBackground (앱종료-권장)
     * 앱을 백그라운드로 전환시킵니다.
     * @param activity 현재 액티비티
     */
    fun moveAppToBackground(activity: Activity) {
        activity.moveTaskToBack(true)
    }

    /**
     * exitApp (앱완전종료-비권장)
     * 백그라운드 태스크까지 정리하여 앱을 종료합니다.
     * @param activity 현재 액티비티
     */
    fun exitApp(activity: Activity) {
        activity.finishAffinity() // 현재 액티비티와 같은 태스크에 있는 모든 액티비티 종료
        activity.finishAndRemoveTask() // 태스크를 백 스택에서 제거 (API 21 이상)
        exitProcess(0) // JVM 프로세스를 종료
    }

    /**
     * openAppSettings
     * 앱 설정 으로 이동
     * @param context 컨텍스트
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = android.net.Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}