package com.example.epic.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.R
import com.example.epic.data.model.report.Data
import com.example.epic.databinding.ItemReportBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportAdapter @Inject constructor() : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
//            val rowPos = holder.bindingAdapterPosition
            binding.tvUserAge.text = data.nama_barang
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.kode_barang == newItem.kode_barang
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }
}