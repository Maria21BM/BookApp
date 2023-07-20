package com.example.bookapp

interface CameraPermissionListener {
    fun onCameraPermissionGranted(book: BookModel)
    fun onCameraPermissionDenied(book: BookModel)
}