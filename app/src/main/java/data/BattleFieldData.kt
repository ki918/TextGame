package data

class BattleFieldData {
    lateinit var mText : String
    lateinit var mMapText : Array<String>
    var mIsMap = false

    constructor(text : String) {
        mText = text
        mIsMap = false
    }

    constructor(mapText : Array<String>) {
        mMapText = mapText
        mIsMap = true
    }
}