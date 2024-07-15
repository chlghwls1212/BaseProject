package com.example.choibaseproject.com.example.choibaseproject.ui.permission

import android.Manifest
import android.Manifest.permission.WRITE_CALL_LOG

data class PermissionItem(
    val name: String,
    val description: String,
    val permissions: List<String>
)

val dangerousPermissions = listOf(
    PermissionItem(
        name = "캘린더(CALENDAR)",
        description = "캘린더 이벤트를 읽고 쓰기 위해 해당 권한에 접근합니다.",
        permissions = listOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
    ),
    PermissionItem(
        name = "통화 목록(CALL_LOG)",
        description = "통화 목록 읽고 관리하기 위해 해당 권한에 접근합니다.",
        permissions = listOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS
        )
    ),
    PermissionItem(
        name = "카메라(CAMERA)",
        description = "사진과 동영상을 찍기 위해 해당 권한에 접근합니다.",
        permissions = listOf(Manifest.permission.CAMERA)
    ),
    PermissionItem(
        name = "연락처(CONTACTS)",
        description = "연락처를 읽고 관리하기 위해 해당 권한에 접근합니다.",
        permissions = listOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
    ),
    PermissionItem(
        name = "위치(LOCATION)",
        description = "기기의 위치정보를 얻기 위해 해당 권한에 접근합니다.",
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ),
    PermissionItem(
        name = "마이크(MICROPHONE)",
        description = "오디오 녹음을 위해 해당 권한에 접근합니다.",
        permissions = listOf(Manifest.permission.RECORD_AUDIO)
    ),
    PermissionItem(
        name = "전화(PHONE)",
        description = "휴대폰 정보를 위해 해당 권한에 접근합니다.",
        permissions = listOf(
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.USE_SIP,
        )
    ),
    PermissionItem(
        name = "생체정보(SENSOR)",
        description = "생체정보를 읽기 위해 해당 권한에 접근합니다.",
        permissions = listOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
    ),
    PermissionItem(
        name = "문자(SMS)",
        description = "통화 목록 읽고 관리하기 위해 해당 권한에 접근합니다.",
        permissions = listOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
        )
    ),
    PermissionItem(
        name = "저장소(STORAGE)",
        description = "외부 저장소에 읽고 쓰기 위해 해당 권한에 접근합니다.",
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    ),
)
