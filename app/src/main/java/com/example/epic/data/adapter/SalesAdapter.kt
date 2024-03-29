package com.example.epic.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.data.model.sales.read.Data
import com.example.epic.databinding.ItemSalesBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SalesAdapter @Inject constructor() : RecyclerView.Adapter<SalesAdapter.ViewHolder>() {
    var listener: ItemListener? = null

    interface ItemListener {
        fun deleteSales(data: Data)
    }


    inner class ViewHolder(val binding: ItemSalesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sales: Data) {
            with(binding){
                tvDay.text = sales.tanggal_sales
                tvProduct.text = sales.nama_barang
                tvTotalProduct.text = sales.jumlah_sales
                tvCodeAndUnit.text = sales.satuan
            }
            binding.tvDeleteSaller.setOnClickListener {
                listener?.deleteSales(sales)
            }
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
        ItemSalesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sales = differ.currentList[position]
        holder.bind(sales)
    }
}