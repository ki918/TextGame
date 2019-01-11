package util

import android.content.Context
import android.util.TypedValue
import java.util.*

object Utils {
    fun withSuffix(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format(
            "%.1f %c",
            count / Math.pow(1000.0, exp.toDouble()),
            "kMGTPE"[exp - 1]
        )
    }

    fun dpToPx(context: Context?, dp: Float): Int {
        if(context != null) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
        }
        else {
            return dp.toInt()
        }
    }

    fun rand(from : Int, to : Int) = Random().nextInt(to - from) + from
}