package com.example.epic.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.R
import com.example.epic.data.model.report.ItemPdf
import com.example.epic.databinding.ItemNewReportBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportAdapter @Inject constructor() : RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNewReportBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<ItemPdf>() {
        override fun areItemsTheSame(oldItem: ItemPdf, newItem: ItemPdf): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: ItemPdf, newItem: ItemPdf): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemNewReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = differ.currentList.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowPos = holder.adapterPosition
        if (rowPos == 0) {
            holder.apply {
                binding.apply {
                    setHeaderBg(txtNo)
                    setHeaderBg(txtCode)
                    setHeaderBg(txtSales)
                    setHeaderBg(txtProductName)
                    setHeaderBg(txtSeller)
                    setHeaderBg(txtProductReturn)
                    setHeaderBg(txtStock)

                    txtNo.text = "No"
                    txtCode.text = "Kode Barang"
                    txtSales.text = "Sales"
                    txtProductName.text = "Nama Barang"
                    txtSeller.text = "Penjualan"
                    txtProductReturn.text = "Pengembalian Barang"
                    txtStock.text = "Persediaan"
                }
            }
        } else {
            val data = differ.currentList[rowPos - 1]
            holder.apply {
                binding.apply {
                    setContentBg(txtNo)
                    setContentBg(txtCode)
                    setContentBg(txtSales)
                    setContentBg(txtProductName)
                    setContentBg(txtSeller)
                    setContentBg(txtProductReturn)
                    setContentBg(txtStock)

                    txtNo.text = data.no
                    txtCode.text = data.code
                    txtSales.text = data.sales
                    txtProductName.text = data.productName
                    txtSeller.text = data.seller
                    txtProductReturn.text = data.productReturn
                    txtStock.text = data.stock
                }
            }
        }

    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View) {
        view.setBackgroundResource(R.drawable.table_content_cell_bg)
    }

    fun addHeaderItem(item: ItemPdf) {
        val currentList = ArrayList(differ.currentList)
        currentList.add(0, item)
        differ.submitList(currentList)
    }
}