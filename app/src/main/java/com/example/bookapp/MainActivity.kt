package com.example.bookapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookapp.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), CameraPermissionListener {

    private lateinit var binding: ActivityMainBinding
    val books = BookData.getBooks()
    lateinit var image: Bitmap

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailsClickListener = object : OnDetailsClickListener {
            override fun onDetailsClick(book: BookModel) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra("passingObject", book)
                intent.putExtra("isFavourite", book.isFavourite)
                startActivity(intent)
            }
        }

        // Create the adapter instance
        var adapter = BookAdapter(books, this@MainActivity, detailsClickListener, this)
        adapter.setCurrentBookPosition(-1)
        // Set the adapter to the RecyclerView
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val adapter = binding.recyclerView.adapter as BookAdapter
        val book = books[adapter.currentPosition]
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                onCameraPermissionGranted(book)
            }else{
                onCameraPermissionDenied(book)
                Toast.makeText(this, "You just denied the permission for camera. You can allow it in the settings.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){
                val thumbnail: Bitmap = data?.extras?.get("data") as Bitmap
                image = data.extras?.get("data") as Bitmap
                val adapter = binding.recyclerView.adapter as BookAdapter

                binding.imageView.setImageBitmap(data.extras?.get("data") as Bitmap)

                if (adapter.currentPosition != -1) {
                    val book = books[adapter.currentPosition]
                    if(thumbnail != null){
                        val bookImageString = bitmapToBase64(thumbnail)
                        book.bookImage = bookImageString // Update the bookImage with the Base64 encoded string
                    } else {
                        val imageUri = data?.data
                        book.bookImage = imageUri.toString()
                    }
//                    book.bookImage = thumbnail.toString()
                    adapter.notifyItemChanged(adapter.currentPosition)
                }
            }
        }
    }

    override fun onCameraPermissionGranted(book: BookModel) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onCameraPermissionDenied(book: BookModel) {
        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}