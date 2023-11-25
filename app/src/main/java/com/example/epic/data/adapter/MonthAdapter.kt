package com.example.epic.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.data.model.month.Month
import com.example.epic.databinding.ItemMonthBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonthAdapter @Inject constructor() :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    var listener: ItemListener? = null
    private var lastClickedPosition: Int = -1

    interface ItemListener {
        fun onClickItem(data: Month)
    }

    inner class ViewHolder(val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Month, position: Int) {
            binding.tvMonth.text = item.month
            binding.tvMonth.setTextColor(
                if (item.isSelected) Color.parseColor("#0660C7")
                else Color.parseColor("#7F838B")
            )
            if (item.isSelected) {
                binding.tvMonth.setTextColor(Color.parseColor("#0660C7"))
            } else {
                binding.tvMonth.setTextColor(Color.parseColor("#7F838B"))
            }
            binding.root.setOnClickListener {
                for (month in differ.currentList) {
                    month.isSelected = false
                }
                item.isSelected = true
                notifyDataSetChanged()
                listener?.onClickItem(item)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Month>() {
        override fun areItemsTheSame(oldItem: Month, newItem: Month): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Month, newItem: Month): Boolean {
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
        holder.bind(month, position)
    }
}