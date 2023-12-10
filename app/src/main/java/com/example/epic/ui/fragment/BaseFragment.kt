package com.example.epic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.epic.util.readLoginResponse

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {

    private var _binding: VB? = null

    val binding: VB
        get() = _binding as VB

    lateinit var sweetAlertDialog: SweetAlertDialog
    val savedUser = readLoginResponse()?.user

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater)
        if (_binding == null) {
            throw IllegalArgumentException("Binding cannot be null")
        }

        initLoading()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initLoading() {
        sweetAlertDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.titleText = "Loading..."
    }

    fun showLoading() {
        sweetAlertDialog.show()
    }

    fun hideLoading() {
        sweetAlertDialog.dismiss()
    }

    fun showWarningMessage(message: String, confirmAction: () -> Unit) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Apakah Anda yakin?")
            .setContentText("menghapus data $message")
            .setConfirmText("Ya, Hapus")
            .setCancelText("Batal")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                confirmAction.invoke()
            }
            .setCancelClickListener {
                it.dismissWithAnimation()
            }
            .show()
    }

    fun showSuccessMessage(message: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Sukses")
            .setContentText(message)
            .setConfirmClickListener {
                it.dismissWithAnimation()
                findNavController().popBackStack()
            }
            .show()
    }

    fun showSuccessDelete(message: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Sukses")
            .setContentText(message)
            .setConfirmClickListener {
                it.dismissWithAnimation()
//                findNavController().popBackStack()
            }
            .show()
    }

    fun showErrorMessage(message: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText(message)
            .show()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}