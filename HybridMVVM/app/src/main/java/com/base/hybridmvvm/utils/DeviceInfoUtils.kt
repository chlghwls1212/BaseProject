package com.base.hybridmvvm.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.base.hybridmvvm.data.model.DeviceInfo
import timber.log.Timber

object DeviceInfoUtils {

    private const val TAG = "DeviceInfoUtils"

    fun getDeviceInfo(context: Context): DeviceInfo {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        return DeviceInfo(
            deviceId = getDeviceId(context),
            deviceBrand = Build.BRAND,
            deviceModel = Build.MODEL,
            deviceManufacturer = Build.MANUFACTURER,
            androidVersion = Build.VERSION.RELEASE,
            sdkVersion = Build.VERSION.SDK_INT,
            imei = getIMEI(telephonyManager),
            simSerialNumber = getSimSerialNumber(telephonyManager),
            simOperatorName = telephonyManager.simOperatorName,
            networkOperatorName = telephonyManager.networkOperatorName,
            phoneNumber = getPhoneNumber(telephonyManager)
        )
    }

    private fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun getIMEI(telephonyManager: TelephonyManager): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei
            } else {
                @Suppress("DEPRECATION")
                telephonyManager.deviceId
            }
        } catch (e: SecurityException) {
            Timber.e(TAG, "Permission denied to access IMEI", e)
            null
        }
    }

    private fun getSimSerialNumber(telephonyManager: TelephonyManager): String? {
        return try {
            @Suppress("DEPRECATION")
            telephonyManager.simSerialNumber
        } catch (e: SecurityException) {
            Timber.e(TAG, "Permission denied to access SIM serial number", e)
            null
        }
    }

    private fun getPhoneNumber(telephonyManager: TelephonyManager): String? {
        return try {
            @Suppress("DEPRECATION")
            telephonyManager.line1Number
        } catch (e: SecurityException) {
            Timber.e(TAG, "Permission denied to access phone number", e)
            null
        }
    }
}