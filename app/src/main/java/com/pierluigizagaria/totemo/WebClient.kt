package com.pierluigizagaria.totemo

import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.google.gson.Gson
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


class WebClient private constructor(context: Context) {
    companion object {
        private val TAG = WebClient::class.java.simpleName
        private const val CLIENT_PATH = "client"
        private const val CLIENT_ASSET_PATH = "client.zip"
        private const val VERSION_FILE_PATH = "client.json"

        private data class WebClientVersion(val versionCode: Int)

        @Volatile
        private var INSTANCE: WebClient? = null

        fun getInstance(context: Context): WebClient =
            synchronized(this) {
                INSTANCE ?: WebClient(context)
                    .also { INSTANCE = it }
            }
    }

    var isUpdating : Boolean = false
        private set

    val path: File = File(context.getExternalFilesDir(null), CLIENT_PATH)
    private val assets: AssetManager = context.applicationContext.assets
    private val updateWork = OneTimeWorkRequestBuilder<UpdaterWorker>().build()

    init {
        path.mkdirs()
    }

    fun deleteInstalledVersion() {
        Arrays.stream(path.listFiles()).forEach(File::delete);
    }

    fun checkForUpdates(context: Context) = WorkManager.getInstance(context)
        .beginUniqueWork(TAG, ExistingWorkPolicy.REPLACE, updateWork)
        .enqueue()

    @Throws(Exception::class)
    private fun getAssetVersion(): WebClientVersion? {
        val inputStream = assets.open(CLIENT_ASSET_PATH)
        val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
        var zipEntry: ZipEntry?
        while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
            if (zipEntry!!.isDirectory || zipEntry!!.name != VERSION_FILE_PATH) continue
            val jsonString = zipInputStream.use { it.readBytes() }.decodeToString()
            return Gson().fromJson(jsonString, WebClientVersion::class.java)
        }
        return null
    }

    @Throws(Exception::class)
    private fun getInstalledVersion(): WebClientVersion? {
        val versionFile = File(
            Uri.Builder()
                .appendPath(path.absolutePath)
                .appendPath(VERSION_FILE_PATH)
                .build().path.toString()
        )
        if (!versionFile.exists()) return null
        versionFile.inputStream().use {
            val jsonString = it.readBytes().decodeToString()
            return Gson().fromJson(jsonString, WebClientVersion::class.java)
        }
    }

    @Throws(Exception::class)
    private fun copyAssetFiles() {
        val inputStream = assets.open(CLIENT_ASSET_PATH)
        val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
        var zipEntry: ZipEntry?
        while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
            val entryPath = Uri.Builder()
                .scheme("file")
                .appendPath(path.absolutePath)
                .appendPath(zipEntry!!.name)
                .build()
                .path.toString()
            if (zipEntry!!.isDirectory) {
                File(entryPath).mkdirs()
                continue
            }
            FileOutputStream(entryPath).use {
                it.write(zipInputStream.readBytes())
            }
        }
    }

    class UpdaterWorker(appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result {
            return try {
                val webClient: WebClient = getInstance(applicationContext)
                val installedVersion = webClient.getInstalledVersion()?.versionCode
                val assetsVersion = webClient.getAssetVersion()?.versionCode
                if (installedVersion == null || assetsVersion!! > installedVersion) {
                    getInstance(applicationContext).isUpdating = true
                    postToast(applicationContext, "Updating web client", Toast.LENGTH_LONG)
                    webClient.copyAssetFiles()
                    postToast(applicationContext, "Web client installed", Toast.LENGTH_LONG)
                    getInstance(applicationContext).isUpdating = false
                } else {
                    Log.i(TAG, "Web client is up to date")
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }

        fun postToast(context: Context, text : CharSequence, duration : Int) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                Toast.makeText(context, text, duration).show()
            }, 1000)
        }
    }
}