package com.example.epic.ui.activity

import android.content.Intent
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.epic.data.model.user.login.RequestLogin
import com.example.epic.databinding.ActivityLoginBinding
import com.example.epic.ui.viewModel.AuthViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.TokenManager
import com.example.epic.util.saveUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onViewCreated(binding: ActivityLoginBinding) {

        checkLogin()
        backPress()

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                username.isEmpty() && password.isEmpty() -> {
                    showErrorMessage("Harap isi username dan password dulu!!")
                }

                username.isEmpty() -> {
                    showErrorMessage("Harap isi username dulu!!")
                }

                password.isEmpty() -> {
                    showErrorMessage("Harap isi password dulu!!")
                }

                else -> {
                    viewModel.requestUserLogin(RequestLogin(username, password))
                    viewModel.userResponse.observe(this) {
//                    binding.progressBar.isVisible = false
                        when (it) {
                            is NetworkResult.Success -> {
                                hideLoading()
                                val response = it.data!!
                                if (response.status) {
                                    tokenManager.saveToken(response.access_token ?: "")
                                    saveUser(response)
                                    startActivity(Intent(this, MainActivity::class.java))
                                } else {
                                    showErrorMessage(response.message)
                                }
                            }

                            is NetworkResult.Loading -> {
                                showLoading()
                            }

                            is NetworkResult.Error -> {
                                hideLoading()
                                showErrorMessage(it.message!!)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun backPress() {
        val dispatcher = onBackPressedDispatcher
        dispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                SweetAlertDialog(this@LoginActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Apakah Anda yakin?")
                    .setContentText("")
                    .setConfirmText("Ya, Keluar")
                    .setCancelText("Batal")
                    .setConfirmClickListener {
                        it.dismissWithAnimation()
                        finish()
                        exitProcess(0)
                    }
                    .setCancelClickListener {
                        it.dismissWithAnimation()
                    }
                    .show()
            }
        })
    }

    private fun checkLogin() {
        showLoading()
        Log.d("tokenya", "${tokenManager.getToken()}")
        if (tokenManager.getToken() != null) {
            hideLoading()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            hideLoading()
        }
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }


//    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
//        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//            .setTitleText("Apakah Anda yakin?")
//            .setContentText("")
//            .setConfirmText("Ya, Keluar")
//            .setCancelText("Batal")
//            .setConfirmClickListener {
//                it.dismissWithAnimation()
//                finish()
//                exitProcess(0)
//            }
//            .setCancelClickListener {
//                it.dismissWithAnimation()
//            }
//            .show()
//        return super.getOnBackInvokedDispatcher()
//    }

}