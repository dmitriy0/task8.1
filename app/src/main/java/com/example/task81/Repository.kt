package com.example.task81

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.*


class Repository(private val context: Context) {

    private val filename = "json.txt"

    fun getAllHeroes(): ArrayList<Hero>? {
        return try {
            jsonToHero(readFromFile())
        } catch (e: Exception) {
            requestToApi()
        }
    }

    fun requestToApi(): ArrayList<Hero>? {
        try {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder().url("https://api.opendota.com/api/herostats").build()
            okHttpClient.newCall(request).execute().use { response ->
                val stringResponse = response.body!!.string()
                writeToFile(stringResponse)
                return jsonToHero(stringResponse)
            }
        } catch (e: Exception) {
            return null
        }
    }

    private fun jsonToHero(json: String): ArrayList<Hero> {
        val type = Types.newParameterizedType(List::class.java, Hero::class.java)
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter = moshi.adapter<ArrayList<Hero>>(type)
        return adapter.fromJson(json)!!
    }

    private fun readFromFile(): String {
        val fileInputStream: FileInputStream = context.openFileInput(filename)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        return bufferedReader.readText()
    }

    private fun writeToFile(data: String) {
        val outputStream: FileOutputStream =
            context.openFileOutput(filename, Context.MODE_PRIVATE)
        outputStream.write(data.toByteArray())
        outputStream.close()
    }
}