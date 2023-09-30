package com.example.epic.data.adapter

//
//class DialogNotificationAdapter(private val notifications: MutableList<NotificationItem>) :
//    RecyclerView.Adapter<DialogNotificationAdapter.ViewHolder>() {
//
//    inner class ViewHolder(private val binding: ItemNotificationBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(notification: NotificationItem) {
//            binding.notificationTitle.text = notification.title
//            binding.notificationDescription.text = notification.description
//            binding.notificationTime.text = notification.time
//
//            if (notification.isNew) {
//                binding.root.setBackgroundColor(notification.color)
//            } else {
//                binding.root.setBackgroundColor(Color.TRANSPARENT)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemNotificationBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val notification = notifications[position]
//        holder.bind(notification)
//    }
//
//    override fun getItemCount(): Int = notifications.size
//
//    fun addNotification(notification: NotificationItem) {
//        notifications.add(0, notification)
//        notifyItemInserted(0)
//    }
//
//    fun markNotificationAsRead(position: Int) {
//        if (position in 0 until notifications.size) {
//            val notification = notifications[position]
//            notification.isNew = false
//            notification.color = Color.TRANSPARENT
//            notifyItemChanged(position)
//        }
//    }
//}
