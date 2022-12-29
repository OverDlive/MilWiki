package com.example.milwiki

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class NaverLogin {
    fun get(apiUrl: String, requestHeaders: Map<String, String>): String? {
        val con: HttpURLConnection = connect(apiUrl)
        return try {
            con.setRequestMethod("GET")
            for ((key, value) in requestHeaders) {
                con.setRequestProperty(key, value)
            }
            val responseCode: Int = con.getResponseCode()
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                readBody(con.getInputStream())
            } else { // 에러 발생
                readBody(con.getErrorStream())
            }
        } catch (e: IOException) {
            throw RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }

    fun connect(apiUrl: String): HttpURLConnection {
        return try {
            val url: URL = URL(apiUrl)
            url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
        } catch (e: IOException) {
            throw RuntimeException("연결이 실패했습니다. : $apiUrl", e)
        }
    }

    fun readBody(body: InputStream): String? {
        val streamReader = InputStreamReader(body)
        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()
                var line: String?
                while (lineReader.readLine().also { line = it } != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }
}