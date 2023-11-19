package com.example.epic.ui.fragment.report.pdf

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class AppPermission {
    companion object {
        const val REQUEST_PERMISSION: Int = 123

        fun permissionGranted(fragment: Fragment) =
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

        fun requestPermission(fragment: Fragment) {
            ActivityCompat.requestPermissions(
                fragment.requireActivity(),
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION
            )
        }

//        fun permissionGranted(context: Context) =
//            ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
//
//        fun requestPermission(activity: MainActivity) {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
//                REQUEST_PERMISSION
//            )
//        }
    }
}