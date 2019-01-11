package com.kong.fsk.textgame.activity

import adapter.BattleAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.kong.fsk.textgame.R
import data.BattleFieldData
import data.MonsterStatus
import data.NextFieldQ
import data.UserStatus
import kotlinx.android.synthetic.main.a_battle.*
import util.ToastUtil
import util.Utils
import java.util.*

class BattleActivity : AppCompatActivity() {
    lateinit var mBattleAdapter : BattleAdapter

    val LEFT = 1
    val UP = 10
    val RIGHT = 100
    val DOWN = 1000
    val NEXT = 10000
    val PLAYER = 20000
    val ATK = 101
    val DEF = 102
    val HELP_TEXT = arrayOf("도움말", "도움", "help", "h", "왼쪽으로")
    val MAP_TEXT = arrayOf("맵", "지도", "map", "m", "어디")
    val LEFT_MOVE_TEXT = arrayOf("왼쪽", "왼", "좌측", "left", "l")
    val UP_MOVE_TEXT = arrayOf("위", "위쪽", "앞으로", "앞", "앞쪽", "up", "u", "top", "t")
    val RIGHT_MOVE_TEXT = arrayOf("오른쪽으로", "오른쪽", "오", "우측", "right", "r")
    val DOWN_MOVE_TEXT = arrayOf("밑", "아래", "아래로", "뒤", "뒤로", "down", "d", "bottom", "b")
    val NEXT_TEXT = arrayOf("이동", "들어가기", "다음층으로", "다음", "next", "n", "in")
    val ATK_TEXT = arrayOf("공격", "쳐", "처", "때려", "ㄸ", "atk", "a", "때리기")
    val DOUBLE_PRESS_INTERVAL = 2000L
    val mButtonListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            if(v is TextView) inputText(v.text.toString())
        }
    }

    var lastPressTime = 0L
    var mCurrentMap = Array(5, { IntArray(5) })
    var mClearMap = Array(5, { BooleanArray(5) })
    var mPlayerX = 0
    var mPlayerY = 0
    var isBattleMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_battle)
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
        val layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)

        mBattleAdapter = BattleAdapter(baseContext)
        layoutManager.stackFromEnd = true

        a_battle_main.adapter = mBattleAdapter
        a_battle_main.layoutManager = layoutManager

        mBattleAdapter.addItem(BattleFieldData(getString(R.string.battle_help)))

        do {
            createMap(1)
        } while (!checkMap())

        a_battle_left.setOnClickListener(mButtonListener)
        a_battle_up.setOnClickListener(mButtonListener)
        a_battle_right.setOnClickListener(mButtonListener)
        a_battle_back.setOnClickListener(mButtonListener)
        a_battle_next.setOnClickListener(mButtonListener)

        a_battle_input.setOnEditorActionListener { v, actionId, event ->

            if(actionId == EditorInfo.IME_ACTION_DONE) {
                inputText(v.text.toString())
                true
            }
            else {
                false
            }
        }
    }

    fun isDoubleClick(): Boolean {
        val pressTime = System.currentTimeMillis()
        val count = pressTime - lastPressTime
        if (count <= DOUBLE_PRESS_INTERVAL) return true

        lastPressTime = pressTime

        return false
    }

    fun filterText(text: String, filter : Array<String>): Boolean {
        filter.forEach {
            if(it.equals(text, true)) return true
        }

        return false
    }

    fun createMap(level : Int) {
        val arrayLength = Math.floor(5 + (level * 0.1)).toInt()
        val size = arrayLength * arrayLength
        val blockMap = Math.floor(size * 0.3).toInt()
        val maxX = arrayLength - 1
        val maxY = arrayLength - 1
        mCurrentMap = Array(arrayLength, { IntArray(arrayLength) })
        mClearMap = Array(arrayLength, { BooleanArray(arrayLength) })

        for (i in 0..(blockMap - 1)) {
            val x = Utils.rand(0, maxX)
            val y = Utils.rand(0, maxY)

            mCurrentMap[y][x] = -1
        }

        for (y in 0..maxY) {
            var mapValueY = LEFT + UP + RIGHT + DOWN

            if(y == 0) mapValueY -= UP
            else if(y == maxY) mapValueY -= DOWN

            for (x in 0..maxX) {
                if(mCurrentMap[y][x] == -1) continue

                var mapValue = mapValueY

                if(y > 0 && mCurrentMap[y - 1][x] == -1) mapValue -= UP
                if(y < maxY && mCurrentMap[y + 1][x] == -1) mapValue -= DOWN
                if(x > 0 && mCurrentMap[y][x - 1] == -1) mapValue -= LEFT
                if(x < maxX && mCurrentMap[y][x + 1] == -1) mapValue -= RIGHT

                if(x == 0) mapValue -= LEFT
                else if(x == maxX) mapValue -= RIGHT

                mCurrentMap[y][x] = mapValue
            }
        }

        while (true) {
            mPlayerX = Utils.rand(0, maxX)
            mPlayerY = Utils.rand(0, maxY)

            if(mCurrentMap[mPlayerY][mPlayerX] != -1) {
                mCurrentMap[mPlayerY][mPlayerX] += PLAYER
                break
            }
        }

        while (true) {
            val x = Utils.rand(0, maxX)
            val y = Utils.rand(0, maxY)
            val value = mCurrentMap.size / 5
            val searchMinX = mPlayerX - value
            val searchMinY = mPlayerY - value
            val searchMaxX = mPlayerX + value
            val searchMaxY = mPlayerY + value

            if(mCurrentMap[y][x] != -1 && mCurrentMap[y][x] < PLAYER && (!(x in searchMinX..searchMaxX) || !(y in searchMinY..searchMaxY))) {
                mCurrentMap[y][x] += NEXT
                break
            }
        }
    }

    fun checkMap() :Boolean {
        val queue = LinkedList<NextFieldQ>()
        val start = NextFieldQ(mPlayerX, mPlayerY)
        val checkvisit = BooleanArray(mCurrentMap.size * mCurrentMap.size)
        queue.add(start)

        while (!queue.isEmpty()) {
            val node = queue.removeLast()
            val position = (node.y * 5) + node.x

            if(mCurrentMap[node.y][node.x] in NEXT..(NEXT + LEFT + UP + RIGHT +  DOWN)) {
                checkButton()
                return true
            }
            else if(mCurrentMap[node.y][node.x] != -1 && !checkvisit[position]) {
                checkvisit[position] = true
                for (i in 0..3) {
                    when(i) {
                        0 -> if(node.x > 0 && mCurrentMap[node.y][node.x - 1] != -1) queue.add(NextFieldQ(node.x - 1, node.y))
                        1 -> if(node.y > 0 && mCurrentMap[node.y  - 1][node.x] != -1) queue.add(NextFieldQ(node.x, node.y - 1))
                        2 -> if(node.x < (mCurrentMap.size - 1) && mCurrentMap[node.y][node.x + 1] != -1) queue.add(NextFieldQ(node.x + 1, node.y))
                        3 -> if(node.y < (mCurrentMap.size - 1) && mCurrentMap[node.y  + 1][node.x] != -1) queue.add(NextFieldQ(node.x, node.y + 1))
                    }
                }
            }
        }

        return false
    }

    fun printMap() : Array<Array<String>> {
        var tile : String
        val printMap = Array(mCurrentMap.size) { Array(mCurrentMap.size) {""}}

        for (y in 0..(printMap.size - 1)) {
            for (x in 0..(printMap.size - 1)) {
                when(mCurrentMap[y][x]) {
                    LEFT -> tile = baseContext.getString(R.string.field_l_r)
                    RIGHT -> tile = baseContext.getString(R.string.field_l_r)
                    UP -> tile = baseContext.getString(R.string.field_u_d)
                    DOWN -> tile = baseContext.getString(R.string.field_u_d)
                    LEFT + RIGHT -> tile = baseContext.getString(R.string.field_l_r)
                    LEFT + UP -> tile = baseContext.getString(R.string.field_l_u)
                    LEFT + DOWN -> tile = baseContext.getString(R.string.field_l_d)
                    LEFT + UP + RIGHT -> tile = baseContext.getString(R.string.field_l_u_r)
                    LEFT + UP + DOWN -> tile = baseContext.getString(R.string.field_l_u_d)
                    LEFT + RIGHT + DOWN -> tile = baseContext.getString(R.string.field_l_r_d)
                    UP + RIGHT -> tile = baseContext.getString(R.string.field_u_r)
                    UP + DOWN -> tile = baseContext.getString(R.string.field_u_d)
                    UP + RIGHT + DOWN -> tile = baseContext.getString(R.string.field_u_r_d)
                    RIGHT + DOWN -> tile = baseContext.getString(R.string.field_r_d)
                    LEFT + UP + RIGHT + DOWN -> tile = baseContext.getString(R.string.field_l_u_r_d)
                    NEXT + LEFT -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_r))
                    NEXT + RIGHT -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_r))
                    NEXT + UP -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_u_d))
                    NEXT + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_u_d))
                    NEXT + LEFT + RIGHT -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_r))
                    NEXT + LEFT + UP -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_u))
                    NEXT + LEFT + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_d))
                    NEXT + LEFT + UP + RIGHT -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_u_r))
                    NEXT + LEFT + UP + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_u_d))
                    NEXT + LEFT + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_r_d))
                    NEXT + UP + RIGHT -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_u_r))
                    NEXT + UP + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_u_d))
                    NEXT + UP + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_u_r_d))
                    NEXT + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_r_d))
                    NEXT + LEFT + UP + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_next), baseContext.getString(R.string.field_l_u_r_d))
                    PLAYER + LEFT, NEXT + PLAYER + LEFT -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_r))
                    PLAYER + RIGHT, NEXT + PLAYER + RIGHT -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_r))
                    PLAYER + UP, NEXT + PLAYER + UP -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_u_d))
                    PLAYER + DOWN, NEXT + PLAYER + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_u_d))
                    PLAYER + LEFT + RIGHT, NEXT + PLAYER + LEFT + RIGHT -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_r))
                    PLAYER + LEFT + UP, NEXT + PLAYER + LEFT + UP -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_u))
                    PLAYER + LEFT + DOWN, NEXT + PLAYER + LEFT + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_d))
                    PLAYER + LEFT + UP + RIGHT, NEXT + PLAYER + LEFT + UP + RIGHT -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_u_r))
                    PLAYER + LEFT + UP + DOWN, NEXT + PLAYER + LEFT + UP + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_u_d))
                    PLAYER + LEFT + RIGHT + DOWN, NEXT + PLAYER + LEFT + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_r_d))
                    PLAYER + UP + RIGHT, NEXT + PLAYER + UP + RIGHT -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_u_r))
                    PLAYER + UP + DOWN, NEXT + PLAYER + UP + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_u_d))
                    PLAYER + UP + RIGHT + DOWN, NEXT + PLAYER + UP + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_u_r_d))
                    PLAYER + RIGHT + DOWN, NEXT + PLAYER + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_r_d))
                    PLAYER + LEFT + UP + RIGHT + DOWN, NEXT + PLAYER + LEFT + UP + RIGHT + DOWN -> tile = String.format(baseContext.getString(R.string.field_my), baseContext.getString(R.string.field_l_u_r_d))
                    else -> tile = ""
                }

                printMap[y][x] = tile
            }
        }

        return printMap
    }

    fun inputText(text : String) {
        val item = BattleFieldData(text)
        val list = ArrayList<BattleFieldData>()

        list.add(item)

        if(isBattleMode) {
            if(filterText(item.mText, HELP_TEXT)) {
                list.add(BattleFieldData(baseContext.getString(R.string.battle_help_desc)))
            }
            else if(filterText(item.mText, ATK_TEXT)) {
                battleProcess(ATK, true)
                inputText("적 턴")
                battleProcess(ATK, false)
            }
        }
        else {
            if(filterText(item.mText, MAP_TEXT)) {
                val map = printMap()
                for(y in 0..(map.size - 1)) {
                    val line = Array(5) {""}

                    for(x in 0..(map[0].size - 1)) {
                        line[x] = map[y][x]
                    }

                    list.add(BattleFieldData(line))
                }
            }
            else if (filterText(item.mText, LEFT_MOVE_TEXT)) {
                checkMove(LEFT)
            } else if (filterText(item.mText, UP_MOVE_TEXT)) {
                checkMove(UP)
            } else if (filterText(item.mText, RIGHT_MOVE_TEXT)) {
                checkMove(RIGHT)
            } else if (filterText(item.mText, DOWN_MOVE_TEXT)) {
                checkMove(DOWN)
            } else if (filterText(item.mText, NEXT_TEXT)) {
                do {
                    createMap(1)
                } while (!checkMap())
            }
        }

        mBattleAdapter.addAll(list)
        a_battle_input.setText("")
        a_battle_main.scrollToPosition(mBattleAdapter.itemCount - 1)
    }

    fun checkMove(move : Int) {
        var isMove = false

        when(move) {
            LEFT -> if(mPlayerX > 0 && mCurrentMap[mPlayerY][mPlayerX - 1] != -1) {
                mCurrentMap[mPlayerY][mPlayerX] -= PLAYER
                mPlayerX--
                isMove = true
            }
            UP -> if(mPlayerY > 0 && mCurrentMap[mPlayerY - 1][mPlayerX] != -1) {
                mCurrentMap[mPlayerY][mPlayerX] -= PLAYER
                mPlayerY--
                isMove = true
            }
            RIGHT -> if(mPlayerX < (mCurrentMap.size - 1) && mCurrentMap[mPlayerY][mPlayerX + 1] != -1) {
                mCurrentMap[mPlayerY][mPlayerX] -= PLAYER
                mPlayerX++
                isMove = true
            }
            DOWN -> if(mPlayerY < (mCurrentMap.size - 1) && mCurrentMap[mPlayerY + 1][mPlayerX] != -1) {
                mCurrentMap[mPlayerY][mPlayerX] -= PLAYER
                mPlayerY++
                isMove = true
            }
        }

        if(isMove) {
            movePlayer()
        }
        else {
            mBattleAdapter.addItem(BattleFieldData(getString(R.string.move_block)))
        }
    }

    fun movePlayer() {
        mCurrentMap[mPlayerY][mPlayerX] += PLAYER

        if(!createMonster()) checkButton()
    }

    fun checkButton() {
        when(mCurrentMap[mPlayerY][mPlayerX]) {
            PLAYER + LEFT -> buttonVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE)
            PLAYER + RIGHT -> buttonVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE)
            PLAYER + UP -> buttonVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE)
            PLAYER + DOWN -> buttonVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE)
            PLAYER + LEFT + RIGHT -> buttonVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE)
            PLAYER + LEFT + UP -> buttonVisibility(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE)
            PLAYER + LEFT + DOWN -> buttonVisibility(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE)
            PLAYER + LEFT + UP + RIGHT -> buttonVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE)
            PLAYER + LEFT + UP + DOWN -> buttonVisibility(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE)
            PLAYER + LEFT + RIGHT + DOWN -> buttonVisibility(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE)
            PLAYER + UP + RIGHT -> buttonVisibility(View.GONE, View.VISIBLE, View.VISIBLE, View.GONE)
            PLAYER + UP + DOWN -> buttonVisibility(View.GONE, View.VISIBLE, View.GONE, View.VISIBLE)
            PLAYER + UP + RIGHT + DOWN -> buttonVisibility(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
            PLAYER + RIGHT + DOWN -> buttonVisibility(View.GONE, View.GONE, View.VISIBLE, View.VISIBLE)
            PLAYER + LEFT + UP + RIGHT + DOWN -> buttonVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
        }

        if(mCurrentMap[mPlayerY][mPlayerX] > (PLAYER + NEXT)) a_battle_next.visibility = View.VISIBLE else a_battle_next.visibility = View.GONE
    }

    fun buttonVisibility(left : Int, up : Int, right: Int, down: Int) {
        a_battle_left.visibility = left
        a_battle_up.visibility = up
        a_battle_right.visibility = right
        a_battle_back.visibility = down
    }

    fun createMonster() : Boolean {
        val isCreate = if(Utils.rand(1, 100) > 30) true else false

        if(isCreate) {
            changeMode()
            MonsterStatus.createMonster(2)
            printStatus()
        }

        return isCreate
    }

    fun printStatus() {
        a_battle_status_player_hp.text = "${UserStatus.currHp}/${UserStatus.maxHp}"
        a_battle_status_player_mp.text = "${UserStatus.currMp}/${UserStatus.maxMp}"

        a_battle_status_monster_hp.text = "${MonsterStatus.currHp}/${MonsterStatus.maxHp}"
        a_battle_status_monster_mp.text = "${MonsterStatus.currMp}/${MonsterStatus.currMp}"
    }

    fun changeMode() {
        isBattleMode = !isBattleMode

        if(isBattleMode) {
            a_battle_status_root.visibility = View.VISIBLE
            a_battle_input_button_vs.visibility = View.VISIBLE
            a_battle_input_button_move.visibility = View.GONE
        }
        else {
            a_battle_status_root.visibility = View.GONE
            a_battle_input_button_vs.visibility = View.GONE
            a_battle_input_button_move.visibility = View.VISIBLE
        }
    }

    fun battleProcess(cmd : Int, isPlayerTurn : Boolean) {
        var deal = 1

        if(isPlayerTurn) {
            when(cmd) {
                ATK -> {
                    deal = (Utils.rand((UserStatus.realStr * UserStatus.minStr).toInt(), UserStatus.realStr.toInt()) - (MonsterStatus.realDef * MonsterStatus.minDef)).toInt()

                    if(deal <= 0) deal = 1

                    MonsterStatus.currHp -= deal
                }
            }
        }
        else {
            when(cmd) {
                ATK -> {
                    deal = (Utils.rand((MonsterStatus.realStr * MonsterStatus.minStr).toInt(), MonsterStatus.realStr.toInt()) - (UserStatus.realDef * UserStatus.minDef)).toInt()

                    if(deal <= 0) deal = 1

                    UserStatus.currHp -= deal
                }
            }
        }

        inputText("${deal}의 데미지를 입힘")
        printStatus()
    }
}
