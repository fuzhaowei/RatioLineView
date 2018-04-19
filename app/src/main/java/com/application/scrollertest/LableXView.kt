package com.application.scrollertest

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp

/**
 * Created by EdgeDi
 * 2018/4/18 9:22
 */
class LableXView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    private var view_height = 0
    private val paint by lazy { Paint() }
    private var value_string: ArrayList<Float>? = null
    private var max_value = 0f
    private var string_gap = 0f
    private var unit = 0f

    constructor(context: Context) : this(context, null)

    init {
        init()
    }

    fun setValue(value_string: ArrayList<Float>, max_value: Float, unit: Float) {
        this.value_string = value_string
        this.max_value = max_value
        this.unit = unit
        requestLayout()
        invalidate()
    }

    private fun init() {
        paint.isAntiAlias = true
        paint.textSize = sp(12).toFloat()
        paint.style = Paint.Style.FILL
        paint.strokeWidth = dip(2f).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        view_height = MeasureSpec.getSize(heightMeasureSpec) - dip(20)
        string_gap = view_height / (value_string?.size?.toFloat() ?: 0f)
        paint.measureText(max_value.toString())
        setMeasuredDimension((max_value + dip(5)).toInt(), view_height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.translate(0f, view_height.toFloat())
        canvas?.drawColor(Color.WHITE)
        paint.color = Color.BLACK
        canvas?.drawLine(max_value + dip(5), 0f, max_value + dip(5), -view_height.toFloat(), paint)
        paint.color = Color.GRAY
        value_string?.forEach {
            canvas?.drawText(it.toString(), dip(2).toFloat(), -it * unit, paint)
        }
    }
}