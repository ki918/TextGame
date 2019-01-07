package util

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast

object ToastUtil {
    fun showToast(context: Context?, stringResId: Int) {
        if (context == null || stringResId < 0) return
        showToast(context, context.resources.getString(stringResId))
    }

    fun showToast(context: Context, text: String) {
        showToast(context, text, Toast.LENGTH_SHORT)
    }

    fun showToast(context: Context?, msg: String, length: Int) {
        Toast.makeText(context, msg, length).show()
    }
}