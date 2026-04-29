package com.example.lostfoundapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.lostfoundapp.R
import com.example.lostfoundapp.data.DatabaseHelper
import com.example.lostfoundapp.data.LostFoundItem

class ItemDetailActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var imgItem: ImageView
    private lateinit var txtPostType: TextView
    private lateinit var txtDescription: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtDateTime: TextView
    private lateinit var txtLocation: TextView
    private lateinit var txtContactName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var btnBackToList: Button
    private lateinit var btnRemove: Button

    private var currentItem: LostFoundItem? = null
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        databaseHelper = DatabaseHelper(this)

        bindViews()

        itemId = intent.getIntExtra("item_id", -1)

        if (itemId == -1) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadItemDetails()

        btnBackToList.setOnClickListener {
            finish()
        }

        btnRemove.setOnClickListener {
            removeItem()
        }
    }

    private fun bindViews() {
        imgItem = findViewById(R.id.imgItem)
        txtPostType = findViewById(R.id.txtPostType)
        txtDescription = findViewById(R.id.txtDescription)
        txtCategory = findViewById(R.id.txtCategory)
        txtDateTime = findViewById(R.id.txtDateTime)
        txtLocation = findViewById(R.id.txtLocation)
        txtContactName = findViewById(R.id.txtContactName)
        txtPhone = findViewById(R.id.txtPhone)
        btnBackToList = findViewById(R.id.btnBackToList)
        btnRemove = findViewById(R.id.btnRemove)
    }

    private fun loadItemDetails() {
        val item = databaseHelper.getItemById(itemId)

        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        currentItem = item

        try {
            imgItem.setImageURI(Uri.parse(item.imageUri))
        } catch (_: Exception) {
            imgItem.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        txtPostType.text = item.postType
        txtDescription.text = item.description
        txtCategory.text = "Category: ${item.category}"
        txtDateTime.text = "Posted: ${item.dateTime}"
        txtLocation.text = "Location: ${item.location}"
        txtContactName.text = "Contact name: ${item.contactName}"
        txtPhone.text = "Phone: ${item.phone}"
    }

    private fun removeItem() {
        val item = currentItem ?: return

        val deletedRows = databaseHelper.deleteItem(item.id)

        if (deletedRows > 0) {
            Toast.makeText(this, "Item removed successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ItemListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to remove item", Toast.LENGTH_SHORT).show()
        }
    }
}