package util

import android.util.Log
import data.ConstData

object LogUtil {
    fun debug(tag : String, msg : String) {
        if(ConstData.DEBUG) Log.d(tag, msg)
    }
}