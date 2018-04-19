package com.application.scrollertest

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.dip
import java.util.*

/**
 * Created by EdgeDi
 * 2018/4/17 15:45
 */
class CityRatioLineView(context: Context, attributeSet: AttributeSet?) : LinearLayout(context, attributeSet) {

    private var title = ""
    private var value_string = arrayListOf<Float>()
    private var isDraw = false
    private var m_height = 0

    constructor(context: Context) : this(context, null)

    init {
        if (attributeSet != null) {
            val type = context.obtainStyledAttributes(attributeSet, R.styleable.CityRatioLineView)
            title = type.getString(R.styleable.CityRatioLineView_left_title) ?: ""
            type.recycle()
        }
        orientation = HORIZONTAL
        initChild()
    }

    private var title_view: TextView? = null
    private var lable_view: LableXView? = null
    private var scroll: HorizontalScrollView? = null

    private var test_path: RatioLineView? = null

    private fun initChild() {
        title_view = TextView(context)
        title_view?.maxEms = 1
        title_view?.gravity = Gravity.CENTER_VERTICAL
        title_view?.textSize = 15f
        title_view?.setTextColor(Color.BLACK)
        title_view?.text = title
        val title_lp = LayoutParams(WRAP_CONTENT, MATCH_PARENT)
        title_view?.layoutParams = title_lp
        lable_view = LableXView(context)
        lable_view?.setBackgroundColor(Color.BLACK)
        val lable_lp = LayoutParams(context.dip(10), MATCH_PARENT)
        lable_view?.layoutParams = lable_lp
        scroll = HorizontalScrollView(context)
        val scroll_view = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        scroll?.layoutParams = scroll_view
        scroll?.isHorizontalScrollBarEnabled = false
        test_path = RatioLineView(context)
        scroll?.addView(test_path)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(title_view)
        addView(lable_view)
        addView(scroll)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            isDraw = changed
            m_height = b
            val unit = (m_height - dip(20f)) / max_value
            lable_view?.setValue(value_string, max_value, unit)
            val lists = arrayListOf<ArrayList<Float>>()
            (0 until 3).forEach {
                val list = arrayListOf<Float>()
                (0 until 40).forEach { list.add(Random().nextInt(100).toFloat()) }
                lists.add(list)
            }
            test_path?.setInitData(unit, lists)
        }
    }

    fun setTitle(title: String) {
        this.title = title
        title_view?.text = title
    }

    private var max_value = 0f

    fun setValueString(value_string: ArrayList<Float>) {
        this.value_string = value_string
        (0 until value_string.size).forEach {
            val i1 = value_string[it]
            if (i1 > max_value) {
                max_value = i1
            }
        }
        if (isDraw) {
            val unit = (m_height - dip(20f)) / max_value
            lable_view?.setValue(value_string, max_value, unit)
            val lists = arrayListOf<ArrayList<Float>>()
            (0 until 3).forEach {
                val list = arrayListOf<Float>()
                (0 until 40).forEach { list.add(Random().nextInt(100).toFloat()) }
                lists.add(list)
            }
            test_path?.setInitData(unit, lists)
        }
    }

    fun setItemListener(ItemListener: RatioLineView.OnDataClickListener) {
        test_path?.dataclicklistener = ItemListener
    }

}