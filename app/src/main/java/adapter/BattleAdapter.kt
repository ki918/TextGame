package adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.kong.fsk.textgame.R
import data.BattleFieldData
import util.Utils

class BattleAdapter(val context: Context?) : RecyclerView.Adapter<BattleAdapter.BattleDescHolder>() {
    var mList = ArrayList<BattleFieldData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattleDescHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.i_battle, parent, false)
        return BattleDescHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: BattleDescHolder, position: Int) {
        holder.bind(position)
    }

    fun setList(list : ArrayList<BattleFieldData>) {
        mList = list
        notifyDataSetChanged()
    }

    fun addItem(item : BattleFieldData?) {
        if (item != null) {
            mList.add(item)
            notifyItemInserted(mList.size - 1)
        }
    }

    fun addAll(list : ArrayList<BattleFieldData>) {
        val start = if(mList.size <= 0) 0 else mList.size
        mList.addAll(list)
        notifyItemRangeInserted(start, list.size)
    }

    inner class BattleDescHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root = itemView.findViewById<LinearLayout>(R.id.i_battle_root)

        fun bind(position: Int) {
            val item = mList[position]

            root.removeAllViews()

            if(item.mIsMap) {
                for (i in 0..(item.mMapText.size - 1)) {
                    val textView = TextView(context)
                    val params = LinearLayout.LayoutParams(Utils.dpToPx(context, 20.0f), LinearLayout.LayoutParams.MATCH_PARENT)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textView.text = Html.fromHtml(item.mMapText[i], Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        textView.text = Html.fromHtml(item.mMapText[i])
                    }

                    textView.layoutParams = params
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f)

                    if(context != null) {
                        textView.setTextColor(context.resources.getColor(android.R.color.white))
                    }

                    root.addView(textView)
                }
            }
            else {
                val textView = TextView(context)
                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textView.text = Html.fromHtml(item.mText, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    textView.text = Html.fromHtml(item.mText)
                }

                textView.layoutParams = params
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.0f)

                if(context != null) {
                    textView.setTextColor(context.resources.getColor(android.R.color.white))
                }

                root.addView(textView)
            }
        }
    }
}