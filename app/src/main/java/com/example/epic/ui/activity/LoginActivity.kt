package com.example.epic.ui.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.epic.data.model.user.login.RequestLogin
import com.example.epic.databinding.ActivityLoginBinding
import com.example.epic.ui.viewModel.AuthViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var sweetAlertDialog: SweetAlertDialog

    @Inject
    lateinit var tokenManager: TokenManager
    override fun onViewCreated(binding: ActivityLoginBinding) {

        initLoading()
        checkLogin()

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "username must be filled", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "password must be filled", Toast.LENGTH_SHORT).show()
            } else if (username.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "password must be filled", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.requestUserLogin(RequestLogin(username, password))
                viewModel.userResponse.observe(this) {
//                    binding.progressBar.isVisible = false
                    when (it) {
                        is NetworkResult.Success -> {
                            sweetAlertDialog.dismiss()
                            val response = it.data!!
                            if (response.status) {
                                tokenManager.saveToken(response.access_token)
                                startActivity(Intent(this, MainActivity::class.java))
                            } else {
                                showErrorMessage(response.message)
                            }
                        }

                        is NetworkResult.Loading -> {
//                            binding.progressBar.isVisible = true
                            sweetAlertDialog.show()
                        }

                        is NetworkResult.Error -> {
                            sweetAlertDialog.dismiss()
                            showErrorMessage(it.message!!)
//                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }

    private fun checkLogin() {
        Log.d("tokenya", "${tokenManager.getToken()}")
        if (tokenManager.getToken() != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initLoading() {
        sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        sweetAlertDialog.titleText = "Loading..."
    }

    private fun showErrorMessage(message: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText(message)
            .show()
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

}