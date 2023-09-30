package com.example.epic.util

import android.app.Activity
import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.epic.R
import com.example.epic.ui.activity.MainActivity
import com.google.android.material.appbar.MaterialToolbar

fun hideKeyboard(view: View){
    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }catch (e: Exception){

    }
}

fun View.isVisible(isShowLoading: Boolean, container: View) {
    if (isShowLoading) {
        this.visibility = View.VISIBLE
        container.visibility = View.GONE
    } else {
        this.visibility = View.GONE
        container.visibility = View.VISIBLE
    }
}

fun Fragment.setupMenu(@MenuRes menuId: Int, onMenuSelected: (MenuItem) -> Boolean, showAsAction: Int) =
    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
            menuInflater.inflate(menuId, menu)

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            menuItem.setShowAsAction(showAsAction)
            return onMenuSelected(menuItem)
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)


//fun Fragment.setupMenu(@MenuRes menuId: Int, onMenuSelected: (MenuItem) -> Boolean) =
//    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
//        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
//            menuInflater.inflate(menuId, menu)
//
//        override fun onMenuItemSelected(menuItem: MenuItem) = onMenuSelected(menuItem)
//    }, viewLifecycleOwner, Lifecycle.State.RESUMED)

fun configureToolbarBackPress(
    customToolBar: MaterialToolbar,
    parentVw: View,
    activity: Activity,
    title: String,
) {
    customToolBar.setNavigationIcon(R.drawable.ic_arrow_back_button)
    customToolBar.title = title
    customToolBar.setNavigationOnClickListener {
        parentVw.findNavController().popBackStack()
    }

    (activity as MainActivity).onBackPressedDispatcher.addCallback(
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentVw.findNavController().popBackStack()
            }
        }
    )
}

//fun configureToolbarBackPress(
//    customToolBar: MaterialToolbar,
//    parentVw: View,
//    activity: Activity,
//    title: String,
//    targetResId: Int
//) {
//    customToolBar.setNavigationIcon(R.drawable.ic_arrow_back_button)
//    customToolBar.title = title
//    customToolBar.setNavigationOnClickListener {
//        parentVw.findNavController().navigate(targetResId)
//    }
//
//    (activity as MainActivity).onBackPressedDispatcher.addCallback(
//        object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                parentVw.findNavController().navigate(targetResId)
//            }
//        }
//    )
//}

//configureToolbarBackPress(
//            customToolbar as Toolbar,
//            view,
//            requireActivity(),
//            getString(R.string.title),
//            R.id.mainFragment
//        )