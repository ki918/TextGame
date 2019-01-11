package data

object UserStatus {
    var level : Int = 0
    var point : Int = 0
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
    var currExp : Long = 0
    var nextExp : Long = 0
    var minStr = 0.7
    var minDef = 0.4
    var weaponAtk = 5
    var weaponDef = 5

    fun setRealStr() {
        var value = 0.0

        for (i in 1..str) {
            when(i) {
                in 1..10 -> value += (1 * 2.0f)
                in 11..20 -> value += (1 * 1.8f)
                in 21..30 -> value += (1 * 1.6f)
                in 31..40 -> value += (1 * 1.4f)
                in 41..50 -> value += (1 * 1.2f)
                in 51..60 -> value++
                else -> value += 0.5
            }
        }

        realStr = value.toLong() + weaponAtk
    }

    fun setRealDef() {
        var value = 0.0

        for (i in 1..def) {
            when(i) {
                in 1..10 -> value += (1 * 1.0f)
                in 11..20 -> value += (1 * 0.8f)
                in 21..30 -> value += (1 * 0.6f)
                in 31..40 -> value += (1 * 0.4f)
                in 41..50 -> value += (1 * 0.2f)
                else -> value += 0.1
            }
        }

        realDef = value.toLong() +  weaponDef
    }

    fun setNextExp() {
        nextExp = (Math.pow(level.toDouble(), 2.0) * (Math.pow(level.toDouble() + 1, 2.0) - 15 * level + 60 )).toLong()
    }
}