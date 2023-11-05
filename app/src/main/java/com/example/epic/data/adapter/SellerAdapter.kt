package com.example.epic.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.data.model.seller.Data
import com.example.epic.databinding.ItemSellerBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerAdapter @Inject constructor() : RecyclerView.Adapter<SellerAdapter.ViewHolder>() {

    var listener: ItemListener? = null

    interface ItemListener {
        fun deleteSeller(seller: Data)
    }

    inner class ViewHolder (val binding:ItemSellerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seller: Data) {
            binding.tvDeleteSaller.setOnClickListener {
                listener?.deleteSeller(seller)
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemSellerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val seller = differ.currentList[position]
        holder.bind(seller)
    }


}