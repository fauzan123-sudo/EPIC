package com.example.epic.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.data.model.category.read.Data
import com.example.epic.databinding.ItemCategoryBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryAdapter @Inject constructor() :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    var listener: ItemListener? = null

    interface ItemListener {
        fun updateCategory(data: Data)
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Data) {
            binding.tvCategory.text = category.nama_kategori
            binding.root.setOnClickListener {
                Log.d("clicked", "bind: on fire")
                listener?.updateCategory(category)
            }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id_kategori == newItem.id_kategori
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.bind(category)
    }

    fun deleteItem(position: Int) {
//        val data = differ.currentList[position]
//        differ.currentList.removeAt(position)
//        differ.submitList(differ.currentList)
        val updatedList = differ.currentList.toMutableList()
        updatedList.removeAt(position)
        differ.submitList(updatedList)
    }


    fun addItem(category: Data) {
        val updatedList = differ.currentList.toMutableList()
        updatedList.add(category)
        differ.submitList(updatedList)
        notifyItemInserted(updatedList.size - 1)
    }


//    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//        // Get the item from the viewHolder
//        val position = viewHolder.adapterPosition
//        val data = differ.currentList[position]
//
//        // Remove the item from the list
//        remove(position, direction)
//    }


}