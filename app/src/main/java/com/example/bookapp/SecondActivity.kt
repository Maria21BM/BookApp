package com.example.bookapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookapp.databinding.ActivitySecondBinding
import com.squareup.picasso.Picasso

class SecondActivity : AppCompatActivity() {

    private lateinit var bindingSecond: ActivitySecondBinding
    private lateinit var book: BookModel
    private var phoneNumber: String = "0729635783"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSecond = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(bindingSecond.root)

        book = intent.getParcelableExtra("passingObject") ?: return
        bindingSecond.bookName.text = book.bookName
        bindingSecond.favourite.isChecked = book.isFavourite

        bindingSecond.favourite.isClickable = false

        Picasso.get().load(book.bookImage).into(bindingSecond.bookImage)
        Picasso.get().load(book.authorImage).into(bindingSecond.authorImage)
        bindingSecond.authorName.text = book.authorName
        bindingSecond.isbn.text = book.isbn
        bindingSecond.bookType.text = book.bookType.toString()
        bindingSecond.btnCall.setOnClickListener {
            val phoneCallIntent = Intent(Intent.ACTION_DIAL)
            phoneCallIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(phoneCallIntent)
        }

    }
}