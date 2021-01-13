package com.raxx.player

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import java.util.*
import kotlin.collections.ArrayList


class MusicFinder(val contentResolver: ContentResolver) {
    private val mSongs: MutableList<Song> = ArrayList()
    private val mRandom: Random = Random()
    fun prepare() {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cur = contentResolver.query(
            uri, null,
            MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null
        )
            ?: return
        if (!cur.moveToFirst()) {
            return
        }
        val artistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST)
        val titleColumn = cur.getColumnIndex(MediaStore.Audio.Media.TITLE)
        val albumColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM)
        val albumArtColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
        val durationColumn = cur.getColumnIndex(MediaStore.Audio.Media.DURATION)
        val idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID)

        do {
            mSongs.add(
                Song(
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

    val randomSong: Song?
        get() = if (mSongs.size <= 0) null else mSongs[mRandom.nextInt(mSongs.size)]
    val allSongs: List<Song>
        get() = mSongs

    fun getalbums(): ArrayList<String> {
        var albumlist= arrayListOf<String>()
        for(s in mSongs){
            albumlist.add(s.album)
        }
        return albumlist
    }

    class Song(
        var id: Long,
        var artist: String,
        var title: String,
        var album: String,
        var duration: Long,
        var albumId: Long

    ) {
        val uri: Uri
            get() = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
            )
        val albumArt: Uri?
            get() = try {
                val genericArtUri: Uri = Uri.parse("content://media/external/audio/albumart")
                ContentUris.withAppendedId(genericArtUri, albumId)
            } catch (e: Exception) {
                null
            }

    }

    companion object {
        private const val TAG = "MPLAY_MUSIC_FINDER"
    }
}