package com.pinslog.ww.presentation.viewmodel

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


class SingleLiveEvent<T>: MutableLiveData<T>() {
    private val mPending = AtomicBoolean(false)

    /**
     * isPending 를 관찰하여 true 일 경우 false 로 바꾸고 이벤트 호출 알림
     */
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(
                javaClass.simpleName,
                "Multiple observers registered but only one will be notified of changes."
            )
        }
        super.observe(
            owner
        ) { t: T ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * T가 Void 일 경우 호출을 편하게 하기 위해 있는 메소드
     */
    @MainThread
    fun call() {
        value = null
    }

}