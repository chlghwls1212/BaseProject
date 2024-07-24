package com.base.hybridmvvm.data.model

data class DeviceInfo(
    val deviceId: String,            // 고유한 장치 ID (ANDROID_ID)
    val deviceBrand: String,         // 기기 브랜드 (예: Samsung, Google)
    val deviceModel: String,         // 기기 모델명 (예: Galaxy S21, Pixel 5)
    val deviceManufacturer: String,  // 기기 제조사 (예: Samsung, Google)
    val phoneNumber: String?,         // 전화번호 (SIM 카드에 저장된 전화번호)
    val androidVersion: String,      // 안드로이드 운영체제 버전 (예: 11, 12)
    val sdkVersion: Int,             // SDK 버전 (예: 30, 31)
    val imei: String?,               // IMEI 번호 (고유 기기 식별자, 일부 기기에서만 사용 가능)
    val simSerialNumber: String?,    // SIM 카드 일련 번호
    val simOperatorName: String,     // SIM 카드 운영자 이름 (예: Verizon, AT&T)
    val networkOperatorName: String // 네트워크 운영자 이름 (예: Verizon, AT&T)
)
