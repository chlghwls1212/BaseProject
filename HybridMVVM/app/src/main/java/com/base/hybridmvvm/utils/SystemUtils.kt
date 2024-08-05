package com.base.hybridmvvm.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat.finishAffinity
import timber.log.Timber
import kotlin.system.exitProcess

object SystemUtils {
    /**
     * moveAppToBackground (앱종료-권장)
     * 앱을 백그라운드로 전환시킵니다.
     * @param activity 현재 액티비티
     */
    fun moveAppToBackground(context: Context) {
        (context as Activity).moveTaskToBack(true)
    }

    /**
     * exitApp (앱완전종료-비권장)
     * 백그라운드 태스크까지 정리하여 앱을 종료합니다.
     * @param activity 현재 액티비티
     */
    fun exitApp(context: Context) {
        (context as Activity).finishAffinity() // 현재 액티비티와 같은 태스크에 있는 모든 액티비티 종료
        context.finishAndRemoveTask() // 태스크를 백 스택에서 제거 (API 21 이상)
        exitProcess(0) // JVM 프로세스를 종료
    }

    /**
     * openAppSettings
     * 앱 설정 으로 이동
     * @param context 컨텍스트
     */
    fun openAppSettings(context: Context) {

        if (context !is Activity) {
            throw IllegalArgumentException("Context must be an Activity")
        }
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    /**
     * sendSMS
     * 문자 메시지 전송
     * @param context 컨텍스트
     * @param phoneNumber
     * @param message
     * @param chooserTitle
     */
    fun sendSMS(
        context: Context,
        phoneNumber: String,
        message: String,
        chooserTitle: String? = "문자 메시지 보내기"
    ) {
        val smsIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("sms:$phoneNumber")
            putExtra("sms_body", message)
        }
        context.startActivity(Intent.createChooser(smsIntent, chooserTitle))
    }

    /**
     * makePhoneCall
     * 전화 통화
     * @param context
     * @param phoneNumber
     */
    fun makePhoneCall(context: Context, phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(callIntent)
    }

    /**
     * shareContent
     * 컨텐츠 공유
     * @param context
     * @param subject
     * @param text
     * @param chooserTitle
     */
    fun shareContent(context: Context, subject: String, text: String, chooserTitle: String? = "공유하기") {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(shareIntent, chooserTitle))
    }

}