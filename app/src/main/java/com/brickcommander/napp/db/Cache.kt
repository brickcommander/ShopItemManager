package com.brickcommander.napp.db

import java.io.File
import okhttp3.Cache

object Cache {
    lateinit var cache: Cache

    fun initialize() {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cacheDirectory = File("data/data/com.brickcommander.napp/cache", "http-cache")
        cache = Cache(cacheDirectory, cacheSize.toLong())
    }
}