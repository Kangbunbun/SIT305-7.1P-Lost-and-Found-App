package com.example.lostfoundapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lostfoundapp.R
import com.example.lostfoundapp.data.DatabaseHelper
import com.example.lostfoundapp.data.LostFoundItem
import com.example.lostfoundapp.utils.DateTimeUtils

class CreateAdvertActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var radioLost: RadioButton
    private lateinit var radioFound: RadioButton
    private lateinit var edtContactName: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnChooseImage: Button
    private lateinit var imgPreview: ImageView
    private lateinit var txtDateTime: TextView
    private lateinit var edtLocation: EditText
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button

    private var selectedImageUri: String = ""

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }

            selectedImageUri = uri.toString()

            imgPreview.visibility = View.VISIBLE
            imgPreview.setImageURI(uri)

            btnChooseImage.text = "Image Selected"
            Toast.makeText(this, "Image selected successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        databaseHelper = DatabaseHelper(this)

        bindViews()
        setupCategorySpinner()

        txtDateTime.text = "Posting time: ${DateTimeUtils.getCurrentDateTime()}"

        btnChooseImage.setOnClickListener {
            imagePickerLauncher.launch(arrayOf("image/*"))
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            saveAdvert()
        }
    }

    private fun bindViews() {
        radioLost = findViewById(R.id.radioLost)
        radioFound = findViewById(R.id.radioFound)
        edtContactName = findViewById(R.id.edtContactName)
        edtPhone = findViewById(R.id.edtPhone)
        edtDescription = findViewById(R.id.edtDescription)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnChooseImage = findViewById(R.id.btnChooseImage)
        imgPreview = findViewById(R.id.imgPreview)
        txtDateTime = findViewById(R.id.txtDateTime)
        edtLocation = findViewById(R.id.edtLocation)
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupCategorySpinner() {
        val categories = listOf(
            "Electronics",
            "Pets",
            "Wallets",
            "Keys",
            "Documents",
            "Other"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun saveAdvert() {
        val postType = if (radioLost.isChecked) "Lost" else "Found"
        val contactName = edtContactName.text.toString().trim()
        val phone = edtPhone.text.toString().trim()
        val description = edtDescription.text.toString().trim()
        val category = spinnerCategory.selectedItem.toString()

        val dateTime = DateTimeUtils.getCurrentDateTime()
        txtDateTime.text = "Posting time: $dateTime"

        val location = edtLocation.text.toString().trim()

        if (contactName.isEmpty()) {
            edtContactName.error = "Contact name is required"
            return
        }

        if (phone.isEmpty()) {
            edtPhone.error = "Phone is required"
            return
        }

        if (description.isEmpty()) {
            edtDescription.error = "Description is required"
            return
        }

        if (location.isEmpty()) {
            edtLocation.error = "Location is required"
            return
        }

        if (selectedImageUri.isEmpty()) {
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show()
            return
        }

        val item = LostFoundItem(
            postType = postType,
            contactName = contactName,
            phone = phone,
            description = description,
            category = category,
            dateTime = dateTime,
            location = location,
            imageUri = selectedImageUri
        )

        val result = databaseHelper.insertItem(item)

        if (result > 0) {
            Toast.makeText(this, "Advert saved successfully", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ItemListActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Failed to save advert", Toast.LENGTH_SHORT).show()
        }
    }
}