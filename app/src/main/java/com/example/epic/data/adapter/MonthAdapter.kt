package com.example.epic.data.adapter

import android.graphics.Color
import android.util.Log
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
        fun bind(month: Month, position: Int) {
            Log.d("data", "month is: ${month.isSelected}")
            binding.tvMonth.text = month.month

            binding.tvMonth.setTextColor(
                if (month.isSelected) Color.parseColor("#0660C7")
                else Color.parseColor("#7F838B")
            )
            binding.root.setOnClickListener {
                if (!month.isSelected) {

                    if (lastClickedPosition != -1) {
                        val lastClickedMonth = differ.currentList[lastClickedPosition]
                        lastClickedMonth.isSelected = false
                        notifyItemChanged(lastClickedPosition)
                    }

                    month.isSelected = true
                    binding.tvMonth.setTextColor(Color.parseColor("#0660C7"))
                    notifyItemChanged(position)
                    lastClickedPosition = position
                }

                listener?.onClickItem(month)
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