package com.raxx.player

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler

import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.TabHost
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.imageResource
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity()  {
    var context = this
     var mediaPlayer: MediaPlayer? = null
    private var shuffeltoggle=false
    var doubleBackToExitPressedOnce = false;
    private val TIME_INTERVAL =
        2000 // # milliseconds, desired time passed between two back presses.

    private var mBackPressed: Long = 0





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var intent : String? = intent.getStringExtra("Phone")
//        if(intent=="call") {
//            pause()
//        }


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            createChannel()
        }

        image_view.setImageResource(R.mipmap.ic_launcher_foreground)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )

        }



        var tabHost=findViewById<TabHost>(R.id.tabHost)
        tabHost.setup()

        var spec1 =tabHost.newTabSpec("Tab1")
        spec1.setContent(R.id.tab1)
        spec1.setIndicator("All Songs")
        tabHost.addTab(spec1)

        var spec2 =tabHost.newTabSpec("Tab3")
        spec2.setContent(R.id.tab2)
        spec2.setIndicator("Player")
        tabHost.addTab(spec2)

        var spec3 =tabHost.newTabSpec("Tab3")
        spec3.setContent(R.id.tab3)
        spec3.setIndicator("PlayList")
        tabHost.addTab(spec3)





        val songFinder = MusicFinder(contentResolver)
        songFinder.prepare()
        val songs = songFinder.allSongs
        val i = songs.size
        var j=0
        val list1= mutableListOf<String>()
        for(song in songs){list1.add(song.title)}

        // Display external storage music files list on list view
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list1)
        songlist_view.adapter = adapter
        songlist_view.setOnItemClickListener{ songlist_view, view, position: Int, id: Long->nextplay(
            songs[position]
        )}



        playButton.setOnClickListener{
            playOrPause()
        }
        nextButton.setOnClickListener{

            if(shuffeltoggle) {

                nextplay(songs.random())
            }
           else {
                if (j <= i) {
                    j++
                } else j = 0
                nextplay(songs[j])
            }
        }
        backButton2.setOnClickListener{
            when {
                j==0 -> j=i-1
                j<=i -> {
                    j--
                }
            }
            nextplay(songs[j])
        }

        shuffel.setOnClickListener{

            when{
                shuffeltoggle -> {
                    shuffeltoggle=false
                    shuffel.setImageResource(R.drawable.ic_baseline_shuffle_24)

                }
                !shuffeltoggle ->{
                    shuffeltoggle=true
                    shuffel.setImageResource(R.drawable.ic_baseline_shuffle_24_white)
                }
            }

        }

        seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2) mediaPlayer?.seekTo(p1)

                textView2.text= mediaPlayer?.duration?.let { milliSecondsToSeconds(it) }
                textView3.text= mediaPlayer?.currentPosition?.let { milliSecondsToSeconds(it) }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

                if (p0 != null) {
                    mediaPlayer?.currentPosition?.let { p0.setProgress(it,true) }
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    mediaPlayer?.currentPosition?.let { p0.setProgress(it,true) }
                }
            }

        })
    }

    override fun onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            if (mediaPlayer!= null) {
                if(mediaPlayer!!.isPlaying()) {
                    mediaPlayer!!.stop();
                }
                mediaPlayer!!.release();
                mediaPlayer= null;
            }
            super.onBackPressed()
            return
        } else {
            Toast.makeText(baseContext, "Tap back button in order to exit", Toast.LENGTH_SHORT)
                .show()
        }
        mBackPressed = System.currentTimeMillis()
    }




    private fun createChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

           var channel = NotificationChannel(CreateNotificatios().CHANNEL_ID,"IsmartApps",NotificationManager.IMPORTANCE_LOW)
            var notificationManager=getSystemService(NotificationManager::class.java)
            if(notificationManager!=null){
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    private fun pause(songs: MusicFinder.Song) {
        var swipe=false

            if (mediaPlayer!!.isPlaying()) {
                swipe = true
            }


        CreateNotificatios(this,songs,R.drawable.ic_baseline_play_arrow_24,1,swipe)
    }

    fun milliSecondsToSeconds(ms:Int) : String{

        var sec= TimeUnit.MILLISECONDS.toSeconds(ms.toLong())
        var min = TimeUnit.SECONDS.toMinutes(sec)
        sec=sec%60
        return "$min : $sec"
    }

//    inner class UpdateSeekBarProgressThread : Runnable{
//        override fun run() {
//            var currentTym= mediaPlayer?.currentPosition
//            if (currentTym != null) {
//                seekBar.progress=currentTym
//            }
//            if(currentTym!= mediaPlayer?.duration) handler.postDelayed(this,50)
//
//        }
//
//    }

    private fun  initialiseSeekbar(){

        seekBar.max= mediaPlayer!!.duration



        val handler = Handler()

        handler.postDelayed(object :Runnable{
            override fun run() {
                try{
                    seekBar.progress= mediaPlayer!!.currentPosition
                    handler.postDelayed(this,500)
                }catch (e:Exception){
                    seekBar.progress=0
                }
            }

        },0)


    }
    fun nextplay(songs: MusicFinder.Song) {



        //playOrPause()
//        if(mediaPlayer==null) mediaPlayer=MediaPlayer.create(this,songs.uri)

        songname.text=songs.title
        playButton?.imageResource = R.drawable.ic_baseline_pause_24
        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer.create(ctx, songs.uri)
        initialiseSeekbar()
        mediaPlayer?.setOnCompletionListener {
            nextButton.callOnClick()
        }
        mediaPlayer?.start()
        pause(songs)

    }
    fun playOrPause(){
        var songPlaying:Boolean? = mediaPlayer?.isPlaying
        playButton?.imageResource = R.drawable.ic_baseline_pause_24
        if(songPlaying == true){
            mediaPlayer?.pause()
            playButton?.imageResource = R.drawable.ic_baseline_play_arrow_24
        }
        else{
            mediaPlayer?.start()
            playButton?.imageResource = R.drawable.ic_baseline_pause_24

        }
    }






}