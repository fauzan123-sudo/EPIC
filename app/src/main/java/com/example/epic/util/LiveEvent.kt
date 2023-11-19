package com.example.epic.util

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class LiveEvent<T> : MutableLiveData<T>() {
    private val observers = ArraySet<ObserverWrapper<T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        val wrapper = observers.find { it.observer == observer }
        wrapper?.let {
            observers.remove(it)
            super.removeObserver(it)
        } ?: run {
            super.removeObserver(observer)
        }
    }

    @MainThread
    override fun setValue(value: T?) {
        observers.forEach { it.newValue() }
        super.setValue(value)
    }

    private class ObserverWrapper<T>(val observer: Observer<in T>) : Observer<T> {
        private val handled = AtomicBoolean(false)

//        override fun onChanged(t: T?) {
//            if (handled.compareAndSet(false, true)) {
//                observer.onChanged(t)
//            }
//        }

        fun newValue() {
            handled.set(false)
        }

        override fun onChanged(value: T) {
            if (handled.compareAndSet(false,true)){
                observer.onChanged(value)
            }
        }
    }
}

//// Contoh penggunaan di ViewModel
//class MyViewModel : ViewModel() {
//    val liveEvent = LiveEvent<String>()
//
//    fun sendDataToLiveEvent(data: String) {
//        liveEvent.value = data
//    }
//}
