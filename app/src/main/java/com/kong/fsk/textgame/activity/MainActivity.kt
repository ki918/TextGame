package com.kong.fsk.textgame.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kong.fsk.textgame.R
import data.ConstData
import data.UserStatus
import kotlinx.android.synthetic.main.activity_main.*
import util.FragmentController
import util.ToastUtil
import util.TrayPreferenceUtils

class MainActivity : AppCompatActivity() {
    val DOUBLE_PRESS_INTERVAL = 2000L
    var lastPressTime = 0L
    val mButtonClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.activity_main_bottom_status ->
                    FragmentController.show(FragmentController.STATUS_FRAGMENT, null, supportFragmentManager)
                R.id.activity_main_bottom_skill -> {
                    startActivity(Intent(baseContext, BattleActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentController.show(FragmentController.STATUS_FRAGMENT, null, supportFragmentManager)

        init()
    }

    override fun onBackPressed() {
        if(isDoubleClick()) {
            finish()
        }
        else {
            ToastUtil.showToast(baseContext, R.string.finish)
        }
    }

    fun init() {
        activity_main_bottom_status.setOnClickListener(mButtonClickListener)
        activity_main_bottom_skill.setOnClickListener(mButtonClickListener)

        UserStatus.level = TrayPreferenceUtils.getInt(baseContext, ConstData.LEVEL, 1)
        UserStatus.str = TrayPreferenceUtils.getLong(baseContext, ConstData.STATUS_STR, 1)
        UserStatus.def = TrayPreferenceUtils.getLong(baseContext, ConstData.STATUS_DEF, 1)
        UserStatus.hp = TrayPreferenceUtils.getLong(baseContext, ConstData.STATUS_HP, 1)
        UserStatus.mp = TrayPreferenceUtils.getLong(baseContext, ConstData.STATUS_MP, 1)
        UserStatus.maxHp = UserStatus.hp * 10
        UserStatus.currHp = TrayPreferenceUtils.getLong(baseContext, ConstData.CURRENT_HP, UserStatus.maxHp)
        UserStatus.maxMp = UserStatus.mp * 5
        UserStatus.currMp = TrayPreferenceUtils.getLong(baseContext, ConstData.CURRENT_MP, UserStatus.maxMp)
        UserStatus.point = TrayPreferenceUtils.getInt(baseContext, ConstData.STATUS_POINT, 0)
        UserStatus.currExp = TrayPreferenceUtils.getLong(baseContext, ConstData.CURRENT_EXP, 0)
        UserStatus.setRealStr()
        UserStatus.setRealDef()
        UserStatus.setNextExp()
    }

    fun isDoubleClick(): Boolean {
        val pressTime = System.currentTimeMillis()
        val count = pressTime - lastPressTime
        if (count <= DOUBLE_PRESS_INTERVAL) return true

        lastPressTime = pressTime

        return false
    }
}
