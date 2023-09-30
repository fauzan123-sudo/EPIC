package com.example.epic.ui.fragment.sales

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.epic.R
import com.example.epic.databinding.FragmentListSalesBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListSalesFragment :
    BaseFragment<FragmentListSalesBinding>(FragmentListSalesBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_text_action -> {

                        Toast.makeText(requireContext(), "add has been clicked", Toast.LENGTH_SHORT)
                            .show()
                        true
                    }

                    else -> false
                }
            }, MenuItem.SHOW_AS_ACTION_ALWAYS)


            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Sales"
                )
            }
        }
    }

}