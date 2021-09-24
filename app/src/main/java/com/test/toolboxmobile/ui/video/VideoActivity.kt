package com.test.toolboxmobile.ui.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.toolboxmobile.databinding.ActivityVideoBinding

class VideoActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}