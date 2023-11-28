package com.example.epic.util

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MenuRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.epic.R
import com.example.epic.ui.activity.MainActivity
import com.google.android.material.appbar.MaterialToolbar
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ApiError(val status: Boolean, val message: String, val data: ErrorData)
data class ErrorData(val error: String)

fun getMonth(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.MONTH) + 1
}

fun getYear(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.YEAR)
}

fun Fragment.pickImage(
    imageView: ImageView,
    fromGallery: Boolean = true,
    onResult: (Uri?) -> Unit
) {
    val intent = if (fromGallery) {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            setDataAndType(null, "image/*")
        }
    } else {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }

    val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            onResult(result.data?.data)
        }
    }

    resultLauncher.launch(intent)
}


fun Context.showErrorAlert(errorCode: String, messages: String) {
    val message = when (errorCode) {
        "422" -> messages
        "" -> messages
        "500" -> messages
        else -> {
            "Terjadi kesalahan: $messages"
        }
    }

    val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        .setTitleText("Error")
        .setContentText(message)
        .setConfirmClickListener { it.dismissWithAnimation() }
    sweetAlertDialog.show()
}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentTime = Date()
    return dateFormat.format(currentTime)
}

fun Date.toFormattedString(): String {
    val locale = Locale("id", "ID")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", locale)
    return dateFormat.format(this)
}

fun Date.getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(this)
}


fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun hideKeyboard(view: View) {
    try {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun CircleImageView.loadRoundedImage(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
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

fun Fragment.setupMenu(
    @MenuRes menuId: Int,
    onMenuSelected: (MenuItem) -> Boolean,
    showAsAction: Int
) =
    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuId, menu)
        }

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
    val navController = Navigation.findNavController(parentVw)
    customToolBar.setNavigationIcon(R.drawable.ic_arrow_back_button)
    customToolBar.title = title
    customToolBar.setNavigationOnClickListener {
        navController.popBackStack()
    }

    (activity as MainActivity).onBackPressedDispatcher.addCallback(
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
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