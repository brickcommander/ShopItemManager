package com.brickcommander.napp.db

import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.time.LocalDate
import android.util.Base64
import android.util.Log
import com.brickcommander.napp.BuildConfig
import com.brickcommander.napp.model.Item
import com.brickcommander.napp.utils.LocalDateDeserializer
import com.brickcommander.napp.utils.LocalDateSerializer
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class GitHubJsonHandler() {

    init {
        Cache.initialize()
    }

    private val githubToken = BuildConfig.GITHUB_TOKEN
    private val repoOwner = BuildConfig.REPO_OWNER
    private val repoName = BuildConfig.REPO_NAME
    private val fileItemListPath = BuildConfig.FILE_ITEMLIST_PATH

    private val client = OkHttpClient.Builder()
        .cache(Cache.cache)
        .addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            // Define caching policies based on response headers
            val cacheControl = CacheControl.Builder()
                .maxAge(60*24*2, TimeUnit.MINUTES) // Cache for 30 minutes
                .build()

            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
        .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        .create()

    private val apiUrl = "https://api.github.com/repos/$repoOwner/$repoName/contents/"
    private var fileSha: String? = null  // Variable to store the SHA

    companion object {
        const val TAG = "GitHubJsonHandler"
    }

    // Fetch JSON file from GitHub
    private fun fetchJsonFromGitHub(filePath: String): String? {
        Log.i(TAG, "fetchJsonFromGitHub : ${filePath} : START")

        val request = Request.Builder()
            .url(apiUrl+filePath)
            .header("Authorization", githubToken)
            .build()

        Log.i(TAG, "fetchJsonFromGitHub : calling Read API : ${filePath} : ${request.url}")

        try {
            val response = client.newCall(request).execute() // Synchronous call
            if (!response.isSuccessful) {
                Log.i(TAG, "fetchJsonFromGitHub : Failed to fetch JSON : ${filePath} : ${response.message}")
                return null
            }

            val responseBody = response.body?.string() ?: return null
            val fileInfo = gson.fromJson(responseBody, GitHubFileResponse::class.java)
            fileSha = fileInfo.sha  // Save the SHA

            val jsonContent = String(Base64.decode(fileInfo.content, Base64.DEFAULT))
            Log.i(TAG, "fetchJsonFromGitHub : ${filePath} : ${jsonContent}")
            return jsonContent
        } catch (e: IOException) {
            Log.i(TAG, "fetchJsonFromGitHub : Exception Occured : REQ=${request} : FILEPATH=${filePath}")
            e.printStackTrace()
            return null
        }
    }

    // Get File SHA
    private fun getFileSha(filePath: String): String? {
        Log.i(TAG, "getFileSha : ${filePath} : START")

        val request = Request.Builder()
            .url(apiUrl+filePath)
            .header("Authorization", githubToken)
            .build()

        Log.i(TAG, "getFileSha : calling Read API for SHA : ${filePath} : ${request.url}")

        try {
            val response = client.newCall(request).execute() // Synchronous call
            if (!response.isSuccessful) {
                Log.i(TAG,"getFileSha : Failed to fetch JSON : ${filePath}: ${response.message}")
                return null
            }

            val responseBody = response.body?.string() ?: return null
            val fileInfo = gson.fromJson(responseBody, GitHubFileResponse::class.java)
            return fileInfo.sha  // Save the SHA
        } catch (e: IOException) {
            Log.i(TAG, "getFileSha : Exception Occured : REQ=${request} : FILEPATH=${filePath}")
            e.printStackTrace()
            return null
        }
    }

    // Update JSON file on GitHub
    private fun updateJsonOnGitHub(updatedJson: String, filePath: String, commitMessage: String): Boolean {
        val encodedContent = Base64.encodeToString(updatedJson.toByteArray(), Base64.NO_WRAP)

        val sha = getFileSha(filePath) ?: run {
            Log.i(TAG, "updateJsonOnGitHub : Get SHA API Failed : ${filePath}. Unable to update the JSON file.")
            return false
        }

        Log.i(TAG, "updateJsonOnGitHub : ${filePath} : ${updatedJson} : ${sha} : ${commitMessage}")

        val requestBody = """
            {
              "message": "$commitMessage",
              "content": "$encodedContent",
              "sha": "$sha"
            }
        """.trimIndent()

        Log.i(TAG, "request body: $requestBody")

        val request = Request.Builder()
            .url(apiUrl+filePath)
            .header("Authorization", githubToken)
            .put(requestBody.toRequestBody("application/json".toMediaType()))
            .build()

        Log.i(TAG, "calling Update API : ${filePath} : ${request}")

        try {
            val response = client.newCall(request).execute() // Synchronous call
            if (response.isSuccessful) {
                Log.i(TAG, "updateJsonOnGitHub : Successfully updated the JSON file on GitHub : ${filePath}.")
                return true
            } else {
                Log.i(TAG, "updateJsonOnGitHub : Failed to update the JSON file : ${filePath} : ${response.body?.string()}")
                return false
            }
        } catch (e: IOException) {
            Log.i(TAG, "updateJsonOnGitHub: Exception Occured : REQ=${request} : FILEPATH=${filePath}")
            e.printStackTrace()
            return false
        }
    }

    fun getItemList(): MutableList<Item> {
        try {
            Log.i(TAG, "getItemList")
            val jsonContent = fetchJsonFromGitHub(fileItemListPath)
            val itemListType = object : TypeToken<MutableList<Item>>() {}.type
            return gson.fromJson(jsonContent, itemListType)
        } catch (e: Exception) {
            Log.i(TAG, "Exception Occured : getItemList : ${e.message}")
            return mutableListOf()
        }
    }

    fun updateItemList(itemList: MutableList<Item>): Boolean {
        try {
            Log.i(TAG, "updateItemList : itemList=$itemList")
            val updatedJson = gson.toJson(itemList)
            Cache.cache.evictAll() // Invalidate the cache
            return updateJsonOnGitHub(updatedJson, fileItemListPath, "Update ItemList : ${LocalDateTime.now()}")
        } catch (e: Exception) {
            Log.i(TAG, "Exception Occured : updateItemList : ${e.message}")
            return false
        }
    }



    // Helper data class to store GitHub file metadata response
    private data class GitHubFileResponse(
        val content: String,
        val sha: String
    )
}
