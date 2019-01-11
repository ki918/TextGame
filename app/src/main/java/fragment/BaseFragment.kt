package fragment

import android.content.Context
import android.support.v4.app.Fragment

open class BaseFragment : Fragment() {
    var mContext : Context? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }
}