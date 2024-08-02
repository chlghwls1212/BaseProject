package com.base.hybridmvvm.data.repository

import com.base.hybridmvvm.data.model.SplashRequest
import com.base.hybridmvvm.data.model.SplashResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class SplashRepository @Inject constructor()  {

    suspend fun sendSplashRequest(splashRequest: SplashRequest): SplashResponse? {
        return withContext(Dispatchers.IO) {
            val url = URL("https://example.com/api/splash")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; utf-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            val jsonInputString = JSONObject().apply {
                put("deviceId", splashRequest.deviceId)
            }.toString()

            connection.outputStream.use { os ->
                val input = jsonInputString.toByteArray()
                os.write(input, 0, input.size)
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                parseSplashResponse(response)
            } else {
                null
            }
        }
    }

    private fun parseSplashResponse(response: String): SplashResponse {
        val jsonObject = JSONObject(response)
        return SplashResponse(
            serverStatus = jsonObject.getBoolean("serverStatus"),
            forceUpdate = jsonObject.getBoolean("forceUpdate"),
            updateUrl = jsonObject.optString("updateUrl", null)
        )
    }
}
