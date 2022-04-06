package com.cs.clothesstore

import android.app.Application
import android.content.Context

open class BasicApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {

        var mContext: Context? = null
        fun getContext(): Context? {
            return mContext
        }
    }
}