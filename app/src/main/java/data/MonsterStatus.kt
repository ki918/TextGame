package data

import util.LogUtil
import util.Utils


object MonsterStatus {
    val TAG = "MonsterStatus"
    var level : Int = 0
    var str : Long = 0
    var def : Long = 0
    var hp : Long = 0
    var mp : Long = 0
    var currHp : Long = 0
    var maxHp : Long = 0
    var currMp : Long = 0
    var maxMp : Long = 0
    var realStr : Long = 0
    var realDef : Long = 0
    var point : Int = 0
    var exp : Long = 0
    var minStr = 0.9
    var minDef = 0.6

    override fun toString(): String {
        return """
            |
            |level $level
            |str $str
            |def $def
            |hp $hp
            |mp $mp
            |HP $currHp/$maxHp
            |MP $currMp/$maxMp
            |ATK $realStr
            |DEF $realDef
            |exp $exp
        """.trimMargin()
    }

    fun setRealStr() {
        var value = 0.0

        for (i in 1..str) {
            when(i) {
                in 1..10 -> value += (1 * 2.2f)
                in 11..20 -> value += (1 * 2.0f)
                in 21..30 -> value += (1 * 1.8f)
                in 31..40 -> value += (1 * 1.6f)
                in 41..50 -> value += (1 * 1.4f)
                in 51..60 -> value++
            }
        }

        var v = (level / 10)
        v++
        val minAtk = Utils.rand(v, v + Math.ceil(v * 0.5).toInt())

        realStr = value.toLong() + minAtk
    }

    fun setRealDef() {
        var value = 0.0

        for (i in 1..def) {
            when(i) {
                in 1..10 -> value += (1 * 1.2f)
                in 11..20 -> value += (1 * 1.0f)
                in 21..30 -> value += (1 * 0.8f)
                in 31..40 -> value += (1 * 0.6f)
                in 41..50 -> value += (1 * 0.4f)
                else -> value += 0.5
            }
        }

        realDef = value.toLong()
    }

    fun createMonster(level: Int) {
        var i = 0
        var addFlag = false
        this.level = level
        point = 6 + level
        while (true) {
            setValue(i++, addFlag)

            if(i == 4) {
                if(point == 0) {
                    break
                }
                else {
                    i = 0
                    addFlag = true
                }
            }
        }
        maxHp = hp * 10
        maxMp = mp * 5
        currHp = maxHp
        currMp = maxMp
        setRealStr()
        setRealDef()
        setExp()

        LogUtil.debug(TAG, "create$MonsterStatus")
    }

    fun destroyMonster() {
        str = 0
        def = 0
        hp = 0
        mp = 0
        maxHp = 0
        currHp = 0
        maxMp = 0
        currMp = 0
        realDef = 0
        realStr = 0

        LogUtil.debug(TAG, "destory$MonsterStatus")
    }

    fun setExp() {
        val value = (level * 0.1)
        val default = 6 + level
        exp = if(value > 1) default * value.toLong() else default.toLong()
    }

    fun setValue(applyCount : Int, addFlag : Boolean) {
        val maxPoint = when (applyCount) {
            0 -> Math.round(point.toLong() / 4.0f)
            1 -> Math.round(point / 3.0f)
            2 -> Math.round(point / 2.0f)
            else -> point
        }

        if(maxPoint <= 0) return

        val min = if(level == 1 || Math.round(point.toLong() / 4.0f) <= 4) 1 else Utils.rand(1, Math.round(point.toLong() / 4.0f))
        val value = if(maxPoint <= 1 || (level <= 5 && applyCount ==3)) 1 else Utils.rand(min, maxPoint).toLong()

        when(applyCount) {
            0 ->
                if(addFlag) hp += value else hp = value
            1 ->
                if(addFlag) str += value else str = value
            2 ->
                if(addFlag) def += value else def = value
            else ->
                if(addFlag) mp += value else mp = value
        }

        point -= value.toInt()
    }
}