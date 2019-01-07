package fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kong.fsk.textgame.R
import data.ConstData
import data.MonsterStatus
import data.UserStatus
import kotlinx.android.synthetic.main.f_status.view.*
import util.TrayPreferenceUtils
import util.Utils

class StatusFragment : BaseFragment() {
    companion object {
        fun newInstance(bundle: Bundle?) : StatusFragment {
            val fragment = StatusFragment()

            if(bundle != null) {
                fragment.arguments = bundle
            }

            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.f_status, container, false)

        val displayHp = "${Utils.withSuffix(UserStatus.currHp)}/${Utils.withSuffix(UserStatus.maxHp)}"
        val displayMp = "${Utils.withSuffix(UserStatus.currMp)}/${Utils.withSuffix(UserStatus.maxMp)}"
        val displayExp = "${Utils.withSuffix(UserStatus.currExp)}/${Utils.withSuffix(UserStatus.nextExp)}"
        val progressHp = UserStatus.currHp.toDouble() / UserStatus.maxHp.toDouble() * 100
        val progressMp = UserStatus.currMp.toDouble() / UserStatus.maxMp.toDouble() * 100
        val progressExp = UserStatus.currExp.toDouble() / UserStatus.nextExp.toDouble() * 100

        root.f_status_level.setText("Lv.${UserStatus.level}")
        root.f_status_hp_text.setText(displayHp)
        root.f_status_mp_text.setText(displayMp)
        root.f_status_exp_text.setText(displayExp)
        root.f_status_hp_progress.progress = progressHp.toInt()
        root.f_status_mp_progress.progress = progressMp.toInt()
        root.f_status_exp_progress.progress = progressExp.toInt()

        root.f_status_str_value.setText(UserStatus.str.toString())
        root.f_status_def_value.setText(UserStatus.def.toString())
        root.f_status_hp_value.setText(UserStatus.hp.toString())
        root.f_status_mp_value.setText(UserStatus.mp.toString())

        when(progressHp.toInt()) {
            in 80..100 -> root.f_status_icon.setImageResource(R.drawable.ic_status_80)
            in 60..79 -> root.f_status_icon.setImageResource(R.drawable.ic_status_60)
            in 40..59 -> root.f_status_icon.setImageResource(R.drawable.ic_status_40)
            in 20..39 -> root.f_status_icon.setImageResource(R.drawable.ic_status_20)
            in 0..19 -> root.f_status_icon.setImageResource(R.drawable.ic_status_0)
        }

        if(UserStatus.point > 0) {
            root.f_status_str_plus.visibility = View.VISIBLE
            root.f_status_def_plus.visibility = View.VISIBLE
            root.f_status_hp_plus.visibility = View.VISIBLE
            root.f_status_mp_plus.visibility = View.VISIBLE
        }

        return root
    }
}