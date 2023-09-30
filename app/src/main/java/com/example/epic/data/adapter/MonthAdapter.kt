package com.example.epic.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.databinding.ItemMonthBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonthAdapter @Inject constructor() :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(month: String) {
            binding.tvMonth.text = month
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val month = differ.currentList[position]
        holder.bind(month)
    }
}