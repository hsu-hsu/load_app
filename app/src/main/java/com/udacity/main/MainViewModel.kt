package com.udacity.main

import android.app.Application
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udacity.R
import com.udacity.utils.sendNotification

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var downloadID: Long = 0
    var checkedRadioId = MutableLiveData<Int>()
    var downloadCompleted = MutableLiveData<Boolean>()
    var checkedRadioText = ""
    private val context = getApplication<Application>().applicationContext


    fun sendNotification(message: String, status: String, fileName: String) {

        val notificationManager =
            ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.sendNotification(message, status, fileName, context)
    }


    fun download() {
        var path = ""

        when (checkedRadioId.value) {
            R.id.radio_glide -> {
                path = context.getString(R.string.radio_glide_download)
                checkedRadioText = context.getString(R.string.radio_glide)
            }

            R.id.radio_loadApp -> {
                path = context.getString(R.string.radio_app_download)
                checkedRadioText = context.getString(R.string.radio_app)
            }
            R.id.radio_retrofit -> {
                path = context.getString(R.string.radio_retrofit_download)
                checkedRadioText = context.getString(R.string.radio_retrofit)
            }
            else -> Toast.makeText(
                context,
                context.getString(R.string.no_option_selected),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (path != "") {
            val request =
                DownloadManager.Request(Uri.parse(path))
                    .setTitle(context.getString(R.string.app_name))
                    .setDescription(context.getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)
                    // Required for api 29 and up
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "/$context.getString(R.string.app_name)"
                    )

            val downloadManager =
                context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)
        } else {

            sendNotification(
                context!!.getString(R.string.notification_description_error),
                context!!.getString(R.string.fail),
                checkedRadioText
            )
            downloadCompleted.value = true
        }


        var br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                Log.d("BroadcastReceiver", id.toString() + " " + downloadID)
                if (id == downloadID) {
                    Log.d("BroadcastReceiver", "Download Successful")
                    downloadCompleted.value = true
                    sendNotification(
                        context!!.getString(R.string.notification_description),
                        context!!.getString(R.string.success),
                        checkedRadioText
                    )
                } else {
                    sendNotification(
                        context!!.getString(R.string.notification_description_error),
                        context!!.getString(R.string.fail),
                        checkedRadioText
                    )
                }
            }

        }
        context.registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

}