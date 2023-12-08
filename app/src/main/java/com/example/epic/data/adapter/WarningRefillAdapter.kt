package com.example.epic.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.data.model.notification.refill.Data
//import com.example.epic.data.model.notification.refill
import com.example.epic.databinding.ItemStockCategoryBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WarningRefillAdapter @Inject constructor() : RecyclerView.Adapter<WarningRefillAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemStockCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Data) {
            binding.tvProduct.text = product.nama_barang
            binding.tvTotalProduct.text = product.minimal_persediaan
            val code = product.kode_barang
            val unit = product.satuan
            binding.tvCodeAndUnit.text = "$code/$unit"

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemStockCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }
}