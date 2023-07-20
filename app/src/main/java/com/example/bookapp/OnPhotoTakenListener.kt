package com.example.bookapp

import android.graphics.Bitmap

interface OnPhotoTakenListener {
    fun onPhotoTaken(bitmap: Bitmap?)
}