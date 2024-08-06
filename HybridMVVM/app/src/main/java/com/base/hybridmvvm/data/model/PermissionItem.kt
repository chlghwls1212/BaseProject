package com.base.hybridmvvm.data.model

import android.Manifest
import android.os.Build

/**
 * 권한 정보
 * minSdk 23,Android 6 이후 권한
 * 호출 불 가능한 권한 요청 시 emptyList() 로 반환
 *
 * 권한 변경 대응 방법
 * 미 사용 권한 : 현 위치 의 권한 삭제, AndroidManifest.xml 내부 해당 권한 삭제.
 * 권한 거부 로직 : BaseViewModel의 LiveData인 아래 변수 observe 이후 컨트롤
 *  - permissionsGranted : 모든 권한 수락
 *  - deniedPermissions : 권한 거절 (1건이상)
 *  - showRationaleDialog : 권한 거절 반복 (2회 이후 OS 팝업 출력 불가.)
 */

// 카메라 사용
val cameraPermission = listOf(
    Manifest.permission.CAMERA // 사진 및 동영상 촬영
)

// 연락처 관리
val contactsPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    listOf(
        Manifest.permission.READ_CONTACTS, // 연락처 읽기
        Manifest.permission.WRITE_CONTACTS, // 연락처 쓰기
        Manifest.permission.GET_ACCOUNTS // 계정 정보 접근
    )
} else {
    listOf(
        Manifest.permission.READ_CONTACTS, // 연락처 읽기
        Manifest.permission.WRITE_CONTACTS // 연락처 쓰기
    )
}

// 위치 정보 접근
val locationPermission = listOf(
    Manifest.permission.ACCESS_FINE_LOCATION, // 정밀 위치
    Manifest.permission.ACCESS_COARSE_LOCATION // 대략적 위치
)

// 캘린더 관리
val calendarPermission = listOf(
    Manifest.permission.READ_CALENDAR, // 캘린더 읽기
    Manifest.permission.WRITE_CALENDAR // 캘린더 쓰기
)

// 저장소 접근
val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    listOf(
        Manifest.permission.READ_MEDIA_IMAGES, // 이미지 읽기
        Manifest.permission.READ_MEDIA_VIDEO, // 비디오 읽기
        Manifest.permission.READ_MEDIA_AUDIO // 오디오 읽기
    )
} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    listOf(
        Manifest.permission.MANAGE_EXTERNAL_STORAGE // 전체 저장소 관리
    )
} else {
    listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, // 외부 저장소 읽기
        Manifest.permission.WRITE_EXTERNAL_STORAGE // 외부 저장소 쓰기
    )
}

// 마이크 사용
val microphonePermission = listOf(
    Manifest.permission.RECORD_AUDIO // 오디오 녹음
)

// 전화 기능 접근
val phonePermission = listOf(
    Manifest.permission.READ_PHONE_NUMBERS, // 전화번호 읽기
    Manifest.permission.ANSWER_PHONE_CALLS, // 전화 응답
    Manifest.permission.CALL_PHONE, // 전화 걸기
    Manifest.permission.ADD_VOICEMAIL, // 음성 메일 추가
    Manifest.permission.READ_PHONE_STATE, // 전화 상태 읽기
    Manifest.permission.USE_SIP, // SIP 사용
    Manifest.permission.PROCESS_OUTGOING_CALLS // 발신 전화 처리
)

// 센서 정보 접근
val sensorsPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    listOf(
        Manifest.permission.BODY_SENSORS, // 센서 정보
        Manifest.permission.BODY_SENSORS_BACKGROUND // 백그라운드 센서 정보 (API 33+)
    )
} else {
    listOf(
        Manifest.permission.BODY_SENSORS // 센서 정보 읽기
    )
}

// SMS 기능
val smsPermission = listOf(
    Manifest.permission.SEND_SMS, // SMS 보내기
    Manifest.permission.RECEIVE_SMS, // SMS 수신
    Manifest.permission.READ_SMS, // SMS 읽기
    Manifest.permission.RECEIVE_WAP_PUSH, // WAP 푸시 메시지 수신
    Manifest.permission.RECEIVE_MMS // MMS 수신
)

// 활동 인식 (API 29+)
val activityRecognitionPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    listOf(
        Manifest.permission.ACTIVITY_RECOGNITION // 활동 인식
    )
} else {
    emptyList()
}

// 블루투스 사용 (API 31+)
val bluetoothPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    listOf(
        Manifest.permission.BLUETOOTH_SCAN, // 블루투스 스캔
        Manifest.permission.BLUETOOTH_CONNECT, // 블루투스 연결
        Manifest.permission.BLUETOOTH_ADVERTISE // 블루투스 광고
    )
} else {
    emptyList()
}

/** 커스텀형태 */
data class PermissionItem(
    val name: String,
    val description: String,
    val permissions: List<String>
)

/** 요청 가능한 모든 권한 집합 */
val dangerousPermissions = listOf(
    PermissionItem(
        name = "CAMERA",
        description = "사진과 동영상을 찍기 위해 해당 권한에 접근합니다.",
        permissions = cameraPermission
    ),
    PermissionItem(
        name = "CONTACTS",
        description = "연락처를 읽고 관리하기 위해 해당 권한에 접근합니다.",
        permissions = contactsPermission
    ),
    PermissionItem(
        name = "LOCATION",
        description = "기기의 위치 정보를 얻기 위해 해당 권한에 접근합니다.",
        permissions = locationPermission
    ),
    PermissionItem(
        name = "CALENDAR",
        description = "캘린더 이벤트를 읽고 쓰기 위해 해당 권한에 접근합니다.",
        permissions = calendarPermission
    ),
    PermissionItem(
        name = "STORAGE",
        description = "외부 저장소에 접근하기 위해 해당 권한에 접근합니다.",
        permissions = storagePermission
    ),
    PermissionItem(
        name = "MICROPHONE",
        description = "오디오 녹음을 위해 해당 권한에 접근합니다.",
        permissions = microphonePermission
    ),
    PermissionItem(
        name = "PHONE",
        description = "휴대폰 정보를 얻기 위해 해당 권한에 접근합니다.",
        permissions = phonePermission
    ),
    PermissionItem(
        name = "SENSOR",
        description = "생체 정보를 읽기 위해 해당 권한에 접근합니다.",
        permissions = sensorsPermission
    ),
    PermissionItem(
        name = "SMS",
        description = "SMS 메시지를 관리하기 위해 해당 권한에 접근합니다.",
        permissions = smsPermission
    ),
    PermissionItem(
        name = "ACTIVITY_RECOGNITION",
        description = "활동 인식을 위해 해당 권한에 접근합니다.",
        permissions = activityRecognitionPermission
    ),
    PermissionItem(
        name = "BLUETOOTH",
        description = "블루투스 기능을 사용하기 위해 해당 권한에 접근합니다.",
        permissions = bluetoothPermission
    )

)
