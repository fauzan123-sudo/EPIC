package com.example.epic.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.epic.ui.activity.MainActivity

class Contract : ActivityResultContract<String, String>() {
    override fun createIntent(context: Context, input: String): Intent {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("Request_Key", input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return "From activity to another activity"
    }

}