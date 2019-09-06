package com.lambdaschool.pokemonapi.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object NetworkAdapter {
    val GET = "GET"
    val POST = "POST"
    val PUT = "PUT"
    val DELETE = "DELETE"
    val TIMEOUT = 3000

    fun getBitmapFromURL(stringUrl: String, width: Int = 0, height: Int = 0): Bitmap? {
        var result: Bitmap? = null
        var stream: InputStream? = null
        var connection: HttpURLConnection? = null
        try {
            val url = URL(stringUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.readTimeout =
                TIMEOUT
            connection.connectTimeout =
                TIMEOUT

            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                stream = connection.inputStream
                if (stream != null) {
                    result = if (width > 0 && height > 0) {
                        Bitmap.createScaledBitmap(BitmapFactory.decodeStream(stream), width, height, true)
                    } else {
                        BitmapFactory.decodeStream(stream)
                    }
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()

            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    fun httpGETRequest(urlString: String): String {
        return httpRequest(urlString, GET, null)
    }

    fun httpRequest(urlString: String, requestMethod: String, requestBody: JSONObject?, headerProperties: Map<String, String>? = null): String {
        var result = ""
        var inputStream: InputStream? = null
        var connection: HttpsURLConnection? = null

        try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpsURLConnection

            connection.requestMethod = requestMethod

            if (headerProperties != null) {
                for ((key, value) in headerProperties) {
                    connection.setRequestProperty(key, value)
                }
            }

            if (requestMethod == POST || requestMethod == PUT) {
                connection.doInput = true
                val outputStream = connection.outputStream
                outputStream.write(requestBody!!.toString().toByteArray())
                outputStream.close()
            } else {
                connection.connect()
            }

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.inputStream
                if (inputStream != null) {
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val builder = StringBuilder()

                    var line: String?
                    do {
                        line = reader.readLine()
                        builder.append(line)
                    } while (line != null)
                    result = builder.toString()
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            connection?.disconnect()
        }
        return result
    }
}