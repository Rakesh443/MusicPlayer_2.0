package com.raxx.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TabHost
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_video.*

class VideoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

//        Toast.makeText(applicationContext, "Started", Toast.LENGTH_SHORT).show()
//        val videoFinder = VideoFinder(contentResolver)
//        videoFinder.prepare()
//        Toast.makeText(applicationContext, "All Set", Toast.LENGTH_SHORT).show()
//        val videos=videoFinder.allvideos
//        Toast.makeText(applicationContext, "${videos.size}", Toast.LENGTH_SHORT).show()


    }
}