package com.example.epic.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onCreateBinding()
        setContentView(binding.root)
        onViewCreated(binding)
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
