package com.example.epic.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.data.model.returnProduct.read.Data
import com.example.epic.databinding.ItemProductReturnBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductReturnAdapter @Inject constructor() :
    RecyclerView.Adapter<ProductReturnAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemProductReturnBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            with(binding) {
                tvTotalProduct.text = data.jumlah_barang
                tvProduct.text = data.nama_barang
                tvDay.text = data.tanggal_pengembalian
                tvCodeAndUnit.text = data.satuan
            }
        }

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id_data_pengembalian_barang == newItem.id_data_pengembalian_barang
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemProductReturnBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }
}