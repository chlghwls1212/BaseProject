package com.base.hybridmvvm.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.base.hybridmvvm.R

object ToastUtils {
    private var currentToast: Toast? = null

    /**
     * 커스텀 토스트를 표시합니다.
     * @param context 컨텍스트
     * @param message 토스트 메시지
     * @param durationMillis 토스트 표시 시간 (밀리초)
     * @param isSingleton 이전 토스트를 제거하고 새로운 토스트를 표시할지 여부
     * @param layoutResId 커스텀 토스트 레이아웃 리소스 ID
     * @param iconResId 아이콘 리소스 ID (선택 사항)
     */
    fun showToast(
        context: Context,
        message: String,
        durationMillis: Long = 2000L,
        isSingleton: Boolean = true,
        layoutResId: Int = R.layout.custom_toast,
        iconResId: Int? = null
    ) {
        if (isSingleton) {
            currentToast?.cancel()
        }

        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(layoutResId, null)

        val textView: TextView = layout.findViewById(R.id.toast_text)
        textView.text = message

        val icon: ImageView? = layout.findViewById(R.id.toast_icon)
        iconResId?.let {
            icon?.setImageResource(it)
            icon?.visibility = View.VISIBLE
        } ?: run {
            icon?.visibility = View.GONE
        }

        currentToast = Toast(context.applicationContext).apply {
            setGravity(Gravity.BOTTOM, 0, 100)
            view = layout
            duration = Toast.LENGTH_SHORT
        }

        currentToast?.show()

        Handler(Looper.getMainLooper()).postDelayed({
            currentToast?.cancel()
        }, durationMillis)
    }
}
