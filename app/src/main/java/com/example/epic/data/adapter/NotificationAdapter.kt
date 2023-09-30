package com.example.epic.data.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.epic.R
import com.example.epic.data.model.NotificationData
import com.example.epic.databinding.ItemNotification2Binding
import com.github.marlonlom.utilities.timeago.TimeAgoMessages

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notifications: List<NotificationData> = mutableListOf()
    private var timeAgoMessages: TimeAgoMessages? = null

    fun setNotifications(notifications: List<NotificationData>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }

    fun setTimeAgoMessages(timeAgoMessages: TimeAgoMessages) {
        this.timeAgoMessages = timeAgoMessages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotification2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    inner class NotificationViewHolder(private val binding: ItemNotification2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationData) {
            binding.titleTextView.text = notification.title
            binding.bodyTextView.text = notification.body

            if (notification.isRead) {
                binding.readIndicator.setBackgroundResource(R.drawable.circle_read)
            } else {
                binding.readIndicator.setBackgroundResource(R.drawable.circle_unread)
            }

            val currentTimeMillis = System.currentTimeMillis()
            val notificationTimeMillis = notification.timestamp

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


