package com.udacity.detail

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.udacity.R
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.main.MainActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var notificationManager: NotificationManager

    companion object {
        const val EXTRA_DETAIL_STATUS = "status"
        const val EXTRA_DETAIL_FILENAME = "fileName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)

        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.cancelAll()

        binding.contentDetail.fileName.text = intent.getStringExtra(EXTRA_DETAIL_FILENAME)
        binding.contentDetail.status.text = intent.getStringExtra(EXTRA_DETAIL_STATUS)


        binding.contentDetail.okButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}
