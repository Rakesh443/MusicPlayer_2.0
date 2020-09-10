package com.raxx.player

import android.net.Uri

class Songs {
    var aPath: String? = null
    var aName: String? = null
    var aAlbum: String? = null
    var aArtist: String? = null
    var auri: Uri? =null

    fun getauri(): Uri? {
        return auri
    }

    fun setauri(aPath: Uri?) {
        this.auri = auri
    }

    fun getaPath(): String? {
        return aPath
    }

    fun setaPath(aPath: String?) {
        this.aPath = aPath
    }

    fun getaName(): String? {
        return aName
    }

    fun setaName(aName: String?) {
        this.aName = aName
    }

    fun getaAlbum(): String? {
        return aAlbum
    }

    fun setaAlbum(aAlbum: String?) {
        this.aAlbum = aAlbum
    }

    fun getaArtist(): String? {
        return aArtist
    }

    fun setaArtist(aArtist: String?) {
        this.aArtist = aArtist
    }
}