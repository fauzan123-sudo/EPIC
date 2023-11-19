package com.example.epic.data.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.data.model.category.read.Data
import com.example.epic.databinding.ItemStockBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockAdapter @Inject constructor() : RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    var listener: ItemListener? = null

    interface ItemListener {
        fun pickCategory(data: Data)
    }

    inner class ViewHolder(val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Data) {
            binding.tvProduct.text = category.nama_kategori

            binding.root.setOnClickListener {
                listener?.pickCategory(category)
                Log.d("TAG", "data category : $category")
            }
        }
    }


    private val diffCallBack = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data) =
            oldItem.id_kategori == newItem.id_kategori

        override fun areContentsTheSame(oldItem: Data, newItem: Data) = oldItem == newItem

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stock = differ.currentList[position]
        holder.bind(stock)
    }
}