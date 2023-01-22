package com.abhishek.colorwallpaper

import android.util.Log


class Utils {
    companion object {
        fun log(msg: String, e: Throwable? = null) {
            Log.d("@@@", msg, e)
        }
    }
}
