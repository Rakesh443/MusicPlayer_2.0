package com.raxx.player

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore

class VideoFinder(val contentResolver: ContentResolver) {
    private val videos: MutableList<Video> = ArrayList()
    fun prepare(){
        val projection =
            arrayOf(MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME)
        val uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val cur = contentResolver.query(
            uri, projection,
            null, null,null
        )?: return
        if (!cur.moveToFirst()) {
            return
        }
        val artistColumn = cur.getColumnIndex(MediaStore.Video.Media.ARTIST)
        val titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE)
        val albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)
        val albumArtColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
        val durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION)
        val idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID)
        do {
            videos.add(
                VideoFinder.Video(
                    cur.getLong(idColumn),
                    cur.getString(artistColumn),
                    cur.getString(titleColumn),
                    cur.getString(albumColumn),
                    cur.getLong(durationColumn),
                    cur.getLong(albumArtColumn)
                )
            )
        } while (cur.moveToNext())
    }


    val allvideos: List<VideoFinder.Video>
        get() = videos




    class Video (
        var id: Long,
        var artist: String,
        var title: String,
        var album: String,
        var duration: Long,
        var albumId: Long
    ){
        val uri: Uri
            get() = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id
            )
        val albumArt: Uri?
            get() = try {
                val genericArtUri: Uri = Uri.parse("content://media/external/audio/albumart")
                ContentUris.withAppendedId(genericArtUri, albumId)
            } catch (e: Exception) {
                null
            }
    }
}