package com.adityaikhbalm.blobanimation.handler

import android.graphics.Bitmap
import android.graphics.Canvas
import com.adityaikhbalm.blobanimation.model.Blob

class BlobLayoutHandler {

    private lateinit var blobs: Blob
    private var onViewUpdateCallback: (() -> Unit)? = null

    fun setOnViewUpdateListener(onViewUpdate: () -> Unit) {
        onViewUpdateCallback = onViewUpdate
    }

    fun onDraw(canvas: Canvas?, bitmap: Bitmap?) {
        blobs.drawPath(canvas, bitmap)
    }

    fun addBlob(blobConfig: Blob.Configuration) {
        blobs = Blob(blobConfig) {
            onViewUpdateCallback?.invoke()
        }
    }
}