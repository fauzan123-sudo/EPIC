package com.example.epic.data.adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.R
import com.example.epic.data.room.ContactsEntity
import com.example.epic.databinding.ItemNotification2Binding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsAdapter @Inject constructor() : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    private lateinit var binding: ItemNotification2Binding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemNotification2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(item: ContactsEntity) {
            binding.apply {
                binding.titleTextView.text = item.title
                binding.bodyTextView.text = item.body

                if (item.isRead) {
                    binding.readIndicator.setBackgroundResource(R.drawable.circle_read)
                } else {
                    binding.readIndicator.setBackgroundResource(R.drawable.circle_unread)
                }

                val currentTimeMillis = System.currentTimeMillis()
                val notificationTimeMillis = item.timestamp

                val timeAgo = DateUtils.getRelativeTimeSpanString(
                    notificationTimeMillis,
                    currentTimeMillis,
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_RELATIVE
                ).toString()

                binding.timeAgoTextView.text = timeAgo
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ContactsEntity>() {
        override fun areItemsTheSame(oldItem: ContactsEntity, newItem: ContactsEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactsEntity, newItem: ContactsEntity): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

}