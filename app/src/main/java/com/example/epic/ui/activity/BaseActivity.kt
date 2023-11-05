package com.example.epic.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import cn.pedant.SweetAlert.SweetAlertDialog
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
    private var progressDialogDialog: SweetAlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onCreateBinding()
        setContentView(binding.root)
        onViewCreated(binding)
    }

    fun showLoading() {
        val progressDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            .setTitleText("Loading...")

        progressDialog.setCancelable(false)
        progressDialog.show()
        progressDialogDialog = progressDialog
    }

    fun hideLoading() {
        progressDialogDialog?.dismiss()
    }


    fun showWarningMessage(message: String, confirmAction: () -> Unit) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
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
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Sukses")
            .setContentText(message)
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .show()
    }

    fun showErrorMessage(message: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText(message)
            .show()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewBindingClass(): Class<VB> {
        val type = javaClass.genericSuperclass as ParameterizedType
        return type.actualTypeArguments[0] as Class<VB>
    }

    private fun createViewBindingInstance(): VB {
        val inflate = getViewBindingClass()
            .getMethod("inflate", LayoutInflater::class.java)
        return inflate.invoke(null, layoutInflater) as VB
    }

    abstract fun onViewCreated(binding: VB)

    private fun onCreateBinding(): VB {
        return createViewBindingInstance()
    }
}
