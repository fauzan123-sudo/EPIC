package com.example.epic.ui.fragment.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.data.adapter.ContactsAdapter
import com.example.epic.data.model.NotificationData
import com.example.epic.databinding.FragmentMenuBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.DatabaseViewModel
import com.example.epic.util.Constans
import com.example.epic.util.DataStatus
import com.example.epic.util.getNotification
import com.example.epic.util.isVisible
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {
//    private lateinit var adapter: NotificationAdapter
    private val viewModel: DatabaseViewModel by viewModels()

    @Inject
    lateinit var contactsAdapter: ContactsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            val notifications =
                Paper.book().read(Constans.notify, mutableListOf<NotificationData>())
            val unreadNotifications = notifications!!.filter { !it.isRead }
            val unreadCount = unreadNotifications.size
            unreadNotificationCountTextView.text = unreadCount.toString()

//            adapter.setNotifications(notifications)

//            recyclerView.layoutManager = LinearLayoutManager(requireContext())
//            recyclerView.adapter = adapter

            viewModel.getAllContacts()
            viewModel.contactsList.observe(requireActivity()) {
                when (it.status) {
                    DataStatus.Status.LOADING -> {
                        loading.isVisible(true, recyclerView)
                        emptyBody.isVisible(false, recyclerView)
                    }
                    DataStatus.Status.SUCCESS -> {
//                        it.isEmpty?.let { isEmpty -> showEmpty(isEmpty) }
                        loading.isVisible(false, recyclerView)
                        contactsAdapter.differ.submitList(it.data)
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = adapter
                        }
                    }
                    DataStatus.Status.ERROR -> {
                        loading.isVisible(false, recyclerView)
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun updateBadge() {
        val badge = getNotification()
//        binding.notificationCount.badgeValue = badge.

    }

//    private fun showEmpty(isShown: Boolean) {
//        binding.apply {
//            if (isShown) {
//                emptyBody.visibility = View.VISIBLE
//                listBody.visibility = View.GONE
//            } else {
//                emptyBody.visibility = View.GONE
//                listBody.visibility = View.VISIBLE
//            }
//        }
//    }
}