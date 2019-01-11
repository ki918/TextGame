package util

import android.os.Bundle
import android.support.v4.app.FragmentManager
import com.kong.fsk.textgame.R
import fragment.StatusFragment

object FragmentController {
    const val STATUS_FRAGMENT = "STATUS_FRAGMENT"

    fun show(type: String, bundle: Bundle?, fragmentManager: FragmentManager) {
        val currentFragment = fragmentManager.findFragmentById(R.id.activity_main_fragment)

        if(currentFragment?.tag.equals(type)) {
            return
        }

        val transaction = fragmentManager.beginTransaction()

        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        when(type) {
            STATUS_FRAGMENT -> transaction.add(R.id.activity_main_fragment, StatusFragment.newInstance(bundle), type)
        }

        transaction.addToBackStack(type)
        transaction.commit()
    }
}