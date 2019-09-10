package edu.pes.laresistencia.service

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class TranslatorService {

    private val apiKey: String = "trnsl.1.1.20180428T180354Z.c02822788baebacb.2945277ade1327328abf9cf82c0269ff33981493"

    fun getTranslation(textToTranslate: String, langFrom: String, langTo: String): Pair<Int, String?> {
        var yandexUrl: String = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" +
                apiKey
        val arrayString = textToTranslate.split("\n")
        for (i in 0 until arrayString.size)
            yandexUrl = yandexUrl + "&text=" + arrayString[i]
        yandexUrl = yandexUrl + "&lang=" + langFrom + "-" + langTo
        val url: URL = URL(yandexUrl)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        when (connection.responseCode) {
            401 -> return Pair(401, null)
            402 -> return Pair(402, null)
            404 -> return Pair(404, null)
            413 -> return Pair(413, null)
            422 -> return Pair(422, null)
            501 -> return Pair(501, null)
            200 -> {
                val inputStream: InputStream = connection.inputStream
                val response: String = convertInputStreamToString(inputStream)
                inputStream.close()
                connection.disconnect()
                return Pair(200, getTranslationFromJSON(response))
            }
            else -> {
                return Pair(connection.responseCode, null)
            }
        }
    }

    fun getSupportedLanguages(): Pair<Int, HashMap<String, String>?> {
        val yandexUrl: String = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?key=" +
                apiKey +
                "&ui=en"
        val url: URL = URL(yandexUrl)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        when (connection.responseCode) {
            401 -> return Pair(401, null)
            402 -> return Pair(402, null)
            else -> {
                val inputStream: InputStream = connection.inputStream
                val response: String = convertInputStreamToString(inputStream)
                inputStream.close()
                connection.disconnect()
                return Pair(200, getLanguagesFromJSON(response))
            }
        }
    }

    private fun getLanguagesFromJSON(response: String): HashMap<String, String> {
        val jObject: JSONObject = JSONObject(response)
        val jLang: JSONObject = jObject.getJSONObject("langs")
        val languages: HashMap<String, String> = HashMap()
        val it: Iterator<String> = jLang.keys()
        while (it.hasNext()) {
            val s: String = it.next()
            languages[jLang.getString(s)] = s
        }
        return languages
    }

    private fun getTranslationFromJSON(response: String): String {
        val jText: JSONObject = JSONObject(response)
        val textArray: JSONArray = jText.getJSONArray("text")
        val builder: StringBuilder = StringBuilder()
        for (i in 0 until textArray.length()) {
            builder.appendln(textArray.getString(i))
        }
        return builder.toString()
    }

    private fun convertInputStreamToString(inputStream: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = null
        var result: String = ""
        while ({ line = bufferedReader.readLine(); line }() != null) result += line
        inputStream.close()
        return result
    }
}