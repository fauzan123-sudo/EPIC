package com.example.epic.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.epic.R
import com.example.epic.databinding.ActivityMainBinding
import com.example.epic.util.Constants.FirebaseToken
import com.example.epic.util.TokenManager
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        checkLogin()
        backPress()
        tokenFirebase()

        if (intent.action == "OPEN_HOME_FRAGMENT") {
            findNavController(R.id.fragmentContainerView2).navigate(R.id.listWarningRefillFragment)
        }

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

//        binding.bottomNavigationView.setupWithNavController(navController)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("MainActivity", "FCM registration token: $token")
        }
    }

    private fun tokenFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(FirebaseToken, "$FirebaseToken $token")
                Log.d("fcm founded", "")
            } else {
                Log.d("no fcm founded", "")
            }
        }
    }

    private fun checkLogin() {
        if (tokenManager.getToken() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            Log.d("TAG", "checkLogin: ")
//            Toast.makeText(this, "anda sudah login ${tokenManager.getToken()}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun backPress() {
        val dispatcher = onBackPressedDispatcher
        dispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                SweetAlertDialog(this@MainActivity, SweetAlertDialog.WARNING_TYPE)
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
}