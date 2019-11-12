package com.lly.snp.example

import android.app.Application
import android.content.Context

open class Application : Application() {
    companion object {
        var  _context: Application? = null
        fun getContext(): Context {
            return _context!!
        }
    }
    override fun onCreate() {
        super.onCreate()
        _context = this
    }

}