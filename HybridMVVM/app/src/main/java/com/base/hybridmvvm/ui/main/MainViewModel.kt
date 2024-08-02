package com.base.hybridmvvm.ui.main

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.hybridmvvm.BaseApplication
import com.base.hybridmvvm.ui.base.BaseViewModel
import com.base.hybridmvvm.utils.CalendarEvent
import com.base.hybridmvvm.utils.CalendarUtils
import com.base.hybridmvvm.utils.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application) : BaseViewModel(application) {

    private val _calendarEvents = MutableLiveData<List<CalendarEvent>>()
    val calendarEvents: LiveData<List<CalendarEvent>> = _calendarEvents

    private val _base64Image = MutableLiveData<String>()
    val base64Image: LiveData<String> = _base64Image

    fun loadData() {
        viewModelScope.launch {
            // 데이터 로드 로직
        }
    }

    fun requestCalendarEvents() {
        loadCalendarEvents()
    }

    private fun loadCalendarEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val events = CalendarUtils.readCalendarEvents(getApplication())
            _calendarEvents.postValue(events)
        }
    }

    fun processImage(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = ImageUtils.uriToBitmap(context, uri)
            val base64Image = ImageUtils.bitmapToBase64(bitmap)
            _base64Image.postValue(base64Image)
        }
    }
}
