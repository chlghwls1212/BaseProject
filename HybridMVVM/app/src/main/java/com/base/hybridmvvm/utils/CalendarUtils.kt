package com.base.hybridmvvm.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import java.util.TimeZone

object CalendarUtils {

    // 캘린더에서 이벤트 읽기
    fun readCalendarEvents(context: Context, calendarId: Long? = null): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()
        val uri: Uri = CalendarContract.Events.CONTENT_URI
        val projection: Array<String> = arrayOf(
            CalendarContract.Events._ID, // 기념일 인덱스
            CalendarContract.Events.TITLE, // 기념일 이름
            CalendarContract.Events.DTSTART, // 시작 날짜
            CalendarContract.Events.DTEND, // 끝 날짜
            CalendarContract.Events.DESCRIPTION, // 기념일 설명
            CalendarContract.Events.CALENDAR_ID // 기념일 종류
        )

        val selection = calendarId?.let { "${CalendarContract.Events.CALENDAR_ID} = ?" }
        val selectionArgs = calendarId?.let { arrayOf(calendarId.toString()) }.run { null }

        val cursor: Cursor? =
            context.contentResolver.query(uri, projection, selection, selectionArgs, null)

        cursor?.use {
            val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val dtStartIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val dtEndIndex = it.getColumnIndex(CalendarContract.Events.DTEND)
            val descriptionIndex = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)
            val calendarIdIndex = it.getColumnIndex(CalendarContract.Events.CALENDAR_ID)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val title = it.getString(titleIndex)
                val dtStart = it.getLong(dtStartIndex)
                val dtEnd = it.getLong(dtEndIndex)
                val description = it.getString(descriptionIndex)
                val calId = it.getLong(calendarIdIndex)
                val event = CalendarEvent(id, title, dtStart, dtEnd, description, calId)
                events.add(event)
            }
        }

        return events
    }

    // 캘린더에 이벤트 추가하기
    fun addCalendarEvent(
        context: Context,
        title: String,
        description: String,
        startTime: Long,
        endTime: Long,
        calendarId: Long
    ): Long? {
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTime)
            put(CalendarContract.Events.DTEND, endTime)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val uri: Uri? = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        return uri?.lastPathSegment?.toLong()
    }

    // 캘린더 이벤트 업데이트하기
    fun updateCalendarEvent(
        context: Context,
        eventId: Long,
        title: String,
        description: String,
        startTime: Long,
        endTime: Long
    ) {
        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, startTime)
            put(CalendarContract.Events.DTEND, endTime)
        }

        val updateUri: Uri =
            ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        context.contentResolver.update(updateUri, values, null, null)
    }

    // 캘린더 이벤트 삭제하기
    fun deleteCalendarEvent(context: Context, eventId: Long) {
        val deleteUri: Uri =
            ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        context.contentResolver.delete(deleteUri, null, null)
    }
}

// CalendarEvent 데이터 클래스
data class CalendarEvent(
    val id: Long,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val description: String,
    val calendarId: Long
)