package com.example.bookapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.CardFinancialItemLayoutBinding
import com.example.bookapp.databinding.CardItemLayoutBinding
import com.example.bookapp.databinding.CardKidsItemLayoutBinding
import com.example.bookapp.databinding.LayoutEmptyBinding
import com.squareup.picasso.Picasso

class BookAdapter(
    private var books: ArrayList<BookModel>,
    private val context: Context,
    private val onDetailsClickListener: OnDetailsClickListener,
    private val cameraPermissionListener: CameraPermissionListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_NULL = -1
        const val VIEW_TYPE_FINANCIAL = 0
        const val VIEW_TYPE_SELF_HELP = 1
        const val VIEW_TYPE_KIDS = 2
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    var currentPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FINANCIAL -> {
                val financialBook = CardFinancialItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                FinancialBookViewHolder(financialBook)
            }
            VIEW_TYPE_SELF_HELP -> {
                val selfHelpBook = CardItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SelfHelpBookViewHolder(selfHelpBook)
            }
            VIEW_TYPE_KIDS -> {
                val kidsBook = CardKidsItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                KidsBookViewHolder(kidsBook)
            }
            VIEW_TYPE_NULL -> {
                val empty =
                    LayoutEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderEmpty(empty)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type: $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val book = books[position]
        when (holder) {
            is FinancialBookViewHolder -> holder.bind(book)
            is SelfHelpBookViewHolder -> holder.bind(book)
            is KidsBookViewHolder -> holder.bind(book)
            else -> {
                throw IllegalArgumentException("Invalid view holder: ${holder.javaClass.simpleName}")
            }
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun setCurrentBookPosition(position: Int) {
        currentPosition = position
    }

    inner class FinancialBookViewHolder(private val financialBook: CardFinancialItemLayoutBinding) :
        RecyclerView.ViewHolder(financialBook.root) {

        fun bind(book: BookModel) {
            Picasso.get().load(book.bookImage).into(financialBook.bookImage)
            // Open Camera
            financialBook.bookImage.setOnClickListener {
                if (hasCameraPermission()) {
                    cameraPermissionListener.onCameraPermissionGranted(book)
                } else {
                    currentPosition = adapterPosition
                    requestCameraPermission(book)
                }
            }
            financialBook.bookName.text = book.bookName
            Picasso.get().load(book.authorImage).into(financialBook.authorImage)
            financialBook.authorName.text = book.authorName
            financialBook.isbn.text = book.isbn
            financialBook.bookType.text = book.bookType.type
            financialBook.bookType.setCompoundDrawablesWithIntrinsicBounds(
                BookTypeImage.getImage(
                    book.bookType.type
                ), 0, 0, 0
            )
            financialBook.favourite.isChecked = book.isFavourite
            financialBook.favourite.setOnCheckedChangeListener { _, isChecked ->
                book.isFavourite = isChecked
            }

            // Set the expand button
            financialBook.bookName.maxLines = if (book.isExpanded) Int.MAX_VALUE else 1
            financialBook.bookName.setOnClickListener {
                book.isExpanded = !book.isExpanded
                notifyItemChanged(layoutPosition)
            }

            // Delete Button click listener
            financialBook.delete.setOnClickListener {
                books.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
                notifyItemRangeChanged(layoutPosition, books.size)
            }

            financialBook.details.setOnClickListener {
                onDetailsClickListener.onDetailsClick(book)
            }

            // Share Button click listener
            financialBook.bookName.setOnClickListener {
                val title = financialBook.bookName.text
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, title)

                val chooser = Intent.createChooser(intent, "Share using...")
                context.startActivity(chooser)
            }
        }
    }

    inner class SelfHelpBookViewHolder(private val selfHelpBook: CardItemLayoutBinding) :
        RecyclerView.ViewHolder(selfHelpBook.root) {

        fun bind(book: BookModel) {
            Picasso.get().load(book.bookImage).into(selfHelpBook.bookImage)
            selfHelpBook.bookImage.setOnClickListener {
                if (hasCameraPermission()) {
                    cameraPermissionListener.onCameraPermissionGranted(book)
                } else {
                    currentPosition = adapterPosition
                    requestCameraPermission(book)
                }
            }
            selfHelpBook.bookName.text = book.bookName
            selfHelpBook.bookName.maxLines = if (book.isExpanded) Int.MAX_VALUE else 1
            Picasso.get().load(book.authorImage).into(selfHelpBook.authorImage)
            selfHelpBook.authorName.text = book.authorName
            selfHelpBook.isbn.text = book.isbn
            selfHelpBook.bookType.text = book.bookType.type
            selfHelpBook.bookType.setCompoundDrawablesWithIntrinsicBounds(
                BookTypeImage.getImage(
                    book.bookType.type
                ), 0, 0, 0
            )

            selfHelpBook.favourite.isChecked = book.isFavourite
            selfHelpBook.favourite.setOnCheckedChangeListener { _, isChecked ->
                book.isFavourite = isChecked
            }

            // Set the expand button
            selfHelpBook.bookName.maxLines = if (book.isExpanded) Int.MAX_VALUE else 1
            selfHelpBook.bookName.setOnClickListener {
                book.isExpanded = !book.isExpanded
                notifyItemChanged(layoutPosition)
            }

            // Delete Button click listener
            selfHelpBook.delete.setOnClickListener {
                books.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
                notifyItemRangeChanged(layoutPosition, books.size)
            }

            selfHelpBook.details.setOnClickListener {
                onDetailsClickListener.onDetailsClick(book)
            }

            // Share Button click listener
            selfHelpBook.bookName.setOnClickListener {
                val title = selfHelpBook.bookName.text
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, title)

                val chooser = Intent.createChooser(intent, "Share using...")
                context.startActivity(chooser)
            }
        }
    }

    inner class KidsBookViewHolder(private val kidsBook: CardKidsItemLayoutBinding) :
        RecyclerView.ViewHolder(kidsBook.root) {

        fun bind(book: BookModel) {
            Picasso.get().load(book.bookImage).into(kidsBook.bookImage)
            kidsBook.bookImage.setOnClickListener {
                if (hasCameraPermission()) {
                    cameraPermissionListener.onCameraPermissionGranted(book)
                } else {
                    currentPosition = adapterPosition
                    requestCameraPermission(book)
                }
            }
            kidsBook.bookName.text = book.bookName
            Picasso.get().load(book.authorImage).into(kidsBook.authorImage)
            kidsBook.authorName.text = book.authorName
            kidsBook.isbn.text = book.isbn
            kidsBook.bookType.text = book.bookType.type
            kidsBook.bookType.setCompoundDrawablesWithIntrinsicBounds(
                BookTypeImage.getImage(book.bookType.type), 0, 0, 0
            )
            kidsBook.favourite.isChecked = book.isFavourite
            kidsBook.favourite.setOnCheckedChangeListener { _, isChecked ->
                book.isFavourite = isChecked
            }

            // Set the expand button
            kidsBook.bookName.maxLines = if (book.isExpanded) Int.MAX_VALUE else 1
            kidsBook.bookName.setOnClickListener {
                book.isExpanded = !book.isExpanded
                notifyItemChanged(layoutPosition)
            }

            // Delete Button click listener
            kidsBook.delete.setOnClickListener {
                books.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
                notifyItemRangeChanged(layoutPosition, books.size)
            }

            kidsBook.details.setOnClickListener {
                onDetailsClickListener.onDetailsClick(book)
            }

            // Share Button click listener
            kidsBook.bookName.setOnClickListener {
                val title = kidsBook.bookName.text
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, title)

                val chooser = Intent.createChooser(intent, "Share using...")
                context.startActivity(chooser)
            }
        }
    }

    inner class ViewHolderEmpty(binding: LayoutEmptyBinding) : RecyclerView.ViewHolder(binding.root)


    // Function to convert a Base64 encoded string to a Bitmap
    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (books.isEmpty()) {
            VIEW_TYPE_NULL
        } else {
            when (books[position].bookType) {
                BookType.FINANCIAL_EDUCATION -> VIEW_TYPE_FINANCIAL
                BookType.SELF_HELP_BOOK -> VIEW_TYPE_SELF_HELP
                BookType.KIDS -> VIEW_TYPE_KIDS
            }
        }
    }

    // Check if the app has camera permission
    private fun hasCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request camera permission
    private fun requestCameraPermission(book: BookModel) {
        ActivityCompat.requestPermissions(
            context as Activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
        )
    }

}
