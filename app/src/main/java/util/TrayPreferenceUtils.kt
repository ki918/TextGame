package util

import android.content.Context
import net.grandcentrix.tray.AppPreferences

/**
 * SharedPreference 관리용 클래스
 */
object TrayPreferenceUtils {

    private val DEFAULT_STRING = ""
    private val DEFAULT_BOOLEAN = false
    private val DEFAULT_INT = 0
    private val DEFAULT_LONG = 0L
    private val DEFAULT_FLOAT = 0f

    /**
     * Preferences를 얻어온다.
     *
     * @param context
     * @return
     */
    private fun getPreferences(context: Context): AppPreferences {
        return AppPreferences(context)
    }

    /**
     * Preferences 초기화.
     *
     * @param context
     */
    fun clearPreferences(context: Context) {
        val prefs = getPreferences(context)
        prefs.clear()
    }
    /*-----------------------------------
     * Set Preference
     ------------------------------------*/

    /**
     * String 저장
     */
    fun setString(context: Context, key: String, value: String) {
        val prefs = getPreferences(context)
        prefs.put(key, value)
    }

    /**
     * boolean 저장
     */
    fun setBoolean(context: Context, key: String, value: Boolean) {
        val prefs = getPreferences(context)
        prefs.put(key, value)
    }

    /**
     * int 저장
     */
    fun setInt(context: Context, key: String, value: Int) {
        val prefs = getPreferences(context)
        prefs.put(key, value)
    }

    /**
     * long 저장
     */
    fun setLong(context: Context, key: String, value: Long) {
        val prefs = getPreferences(context)
        prefs.put(key, value)
    }

    /**
     * float 저장
     */
    fun setFloat(context: Context, key: String, value: Float) {
        val prefs = getPreferences(context)
        prefs.put(key, value)
    }
    /*-----------------------------------
     * Get Preference
     ------------------------------------*/

    /**
     * String 가져오기
     */
    @JvmOverloads
    fun getString(context: Context, key: String, defaultValue: String = DEFAULT_STRING): String? {
        val prefs = getPreferences(context)
        return prefs.getString(key, defaultValue)
    }

    /**
     * boolean 가져오기
     */
    @JvmOverloads
    fun getBoolean(context: Context, key: String, defaultValue: Boolean = DEFAULT_BOOLEAN): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(key, defaultValue)
    }

    /**
     * int 가져오기
     */
    @JvmOverloads
    fun getInt(context: Context, key: String, defaultValue: Int = DEFAULT_INT): Int {
        val prefs = getPreferences(context)
        return prefs.getInt(key, defaultValue)
    }

    /**
     * long 가져오기
     */
    @JvmOverloads
    fun getLong(context: Context, key: String, defaultValue: Long = DEFAULT_LONG): Long {
        val prefs = getPreferences(context)
        return prefs.getLong(key, defaultValue)
    }

    /**
     * float 가져오기
     */
    @JvmOverloads
    fun getFloat(context: Context, key: String, defaultValue: Float = DEFAULT_FLOAT): Float {
        val prefs = getPreferences(context)
        return prefs.getFloat(key, defaultValue)
    }

    /**
     * 키에 맞는 값의 존재여부를 반환한다.
     */
    fun contains(context: Context, key: String): Boolean {
        val prefs = getPreferences(context)
        return prefs.contains(key)
    }
}
