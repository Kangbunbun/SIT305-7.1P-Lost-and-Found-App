package com.example.lostfoundapp.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfoundapp.R
import com.example.lostfoundapp.data.LostFoundItem

class ItemAdapter(
    private var items: List<LostFoundItem>,
    private val onItemClick: (LostFoundItem) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItemThumbnail: ImageView = itemView.findViewById(R.id.imgItemThumbnail)
        val txtItemTitle: TextView = itemView.findViewById(R.id.txtItemTitle)
        val txtItemCategory: TextView = itemView.findViewById(R.id.txtItemCategory)
        val txtItemDateTime: TextView = itemView.findViewById(R.id.txtItemDateTime)
        val txtItemLocation: TextView = itemView.findViewById(R.id.txtItemLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lost_found, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        holder.txtItemTitle.text = "${item.postType}: ${item.description}"
        holder.txtItemCategory.text = "Category: ${item.category}"
        holder.txtItemDateTime.text = item.dateTime
        holder.txtItemLocation.text = "At ${item.location}"

        try {
            holder.imgItemThumbnail.setImageURI(Uri.parse(item.imageUri))
        } catch (_: Exception) {
            holder.imgItemThumbnail.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<LostFoundItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}