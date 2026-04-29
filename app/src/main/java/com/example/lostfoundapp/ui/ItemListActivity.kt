package com.example.lostfoundapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfoundapp.R
import com.example.lostfoundapp.data.DatabaseHelper

class ItemListActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var edtSearch: EditText
    private lateinit var spinnerFilterCategory: Spinner
    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var txtEmptyState: TextView
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var btnBackHome: Button
    private lateinit var btnCreateNewAdvert: Button

    private var currentKeyword = ""
    private var currentCategory = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        databaseHelper = DatabaseHelper(this)

        bindViews()
        setupRecyclerView()
        setupCategorySpinner()
        setupSearch()
        setupNavigationButtons()

        loadItems()
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }

    private fun bindViews() {
        edtSearch = findViewById(R.id.edtSearch)
        spinnerFilterCategory = findViewById(R.id.spinnerFilterCategory)
        recyclerViewItems = findViewById(R.id.recyclerViewItems)
        txtEmptyState = findViewById(R.id.txtEmptyState)
        btnBackHome = findViewById(R.id.btnBackHome)
        btnCreateNewAdvert = findViewById(R.id.btnCreateNewAdvert)
    }

    private fun setupNavigationButtons() {
        btnBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        btnCreateNewAdvert.setOnClickListener {
            val intent = Intent(this, CreateAdvertActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter(emptyList()) { item ->
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("item_id", item.id)
            startActivity(intent)
        }

        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        recyclerViewItems.adapter = itemAdapter
    }

    private fun setupCategorySpinner() {
        val categories = listOf(
            "All",
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

        spinnerFilterCategory.adapter = adapter

        spinnerFilterCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentCategory = categories[position]
                loadItems()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                currentCategory = "All"
            }
        }
    }

    private fun setupSearch() {
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                currentKeyword = s.toString()
                loadItems()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun loadItems() {
        val items = databaseHelper.searchAndFilterItems(currentKeyword, currentCategory)
        itemAdapter.updateItems(items)

        if (items.isEmpty()) {
            recyclerViewItems.visibility = View.GONE
            txtEmptyState.visibility = View.VISIBLE
        } else {
            recyclerViewItems.visibility = View.VISIBLE
            txtEmptyState.visibility = View.GONE
        }
    }
}