package com.udacity.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.R
import com.udacity.customview.ButtonState
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainViewModelFactory: MainViewModelFactory

    private var PERMISSION_REQUEST_CODE = 112

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        mainViewModelFactory = MainViewModelFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        binding.contentmain.mainViewModel = mainViewModel
        binding.lifecycleOwner = this

        // TODO: Implement code below
        binding.contentmain.customButton.setOnClickListener{
            binding.contentmain.customButton.isEnabled = false
            binding.contentmain.customButton.buttonState = ButtonState.LOADING
            mainViewModel.download()
        }

        mainViewModel.downloadCompleted.observe(this, Observer {
            binding.contentmain.customButton.isEnabled = true
            binding.contentmain.customButton.buttonState = ButtonState.COMPLETED
            Toast.makeText(
                this,
                getString(R.string.success),
                Toast.LENGTH_SHORT
            ).show()
        })

        if (Build.VERSION.SDK_INT >= 33) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
            else{
                createChannel(
                    getString(R.string.load_notification_channel_id),
                    getString(R.string.load_notification_channel_name)
                )
            }
        } else {
            createChannel(
                getString(R.string.load_notification_channel_id),
                getString(R.string.load_notification_channel_name)
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty()){
            createChannel(
                getString(R.string.load_notification_channel_id),
                getString(R.string.load_notification_channel_name)
            )
        }
    }

    private fun createChannel(channelId: String, channelName: String) {

        // Create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )// Disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.app_name)

            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}