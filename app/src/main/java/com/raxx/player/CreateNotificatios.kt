package com.raxx.player

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


open class CreateNotificatios {

    open val  CHANNEL_ID="channel1"


    constructor()

    constructor(context: Context, songs: MusicFinder.Song, playButton:Int, position: Int,swipe:Boolean){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            var activityIntent=Intent(context,MainActivity::class.java)
            var contentIntent = PendingIntent.getActivity(context,0,activityIntent,0)
            var notificationManagerCompat =NotificationManagerCompat.from(context)

            var mediaSessionCompact=MediaSessionCompat(context,"tag")
            var icon = BitmapFactory.decodeResource(context.resources,playButton)

            var notificatios= NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                .setContentTitle(songs.title)
                .setContentText(songs.artist)
                .setLargeIcon(icon)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(contentIntent)
                .setOngoing(swipe)
                .build()

            notificationManagerCompat.notify(1,notificatios)
        }
    }
}