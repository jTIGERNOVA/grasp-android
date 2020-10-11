package com.jtigernova.designpatterns

import android.app.Application
import android.util.Log

class DesignPatterns : Application() {

    /**
     * Called when the operating system has determined that it is a good time for a process to
     * trim unneeded memory from its process. This will happen for example when it goes in the
     * background and there is not enough memory to keep as many background processes running as
     * desired. You should never compare to exact values of the level, since new intermediate
     * values may be added -- you will typically want to compare if the value is greater or equal
     * to a level you are interested in.
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        Dep.clear()

        Log.d("Application", "onTrimMemory called. Memory trimmed")
    }
}