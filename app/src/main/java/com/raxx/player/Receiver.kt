package com.raxx.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast

class Receiver:BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Toast.makeText(p0,"fghjk",Toast.LENGTH_LONG).show()
        val i= Intent(p0, MainActivity::class.java)
        i.putExtra("Phone", "call")
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        p0?.startActivity(i)
    }
}