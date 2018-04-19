package com.application.scrollertest

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import org.jetbrains.anko.dip
import org.jetbrains.anko.sp


/**
 * Created by EdgeDi
 * 2018/4/16 15:52
 */
@Suppress("NAME_SHADOWING")
@SuppressLint("DrawAllocation")
class RatioLineView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    private val paint by lazy { Paint() }
    private val text_paint by lazy { Paint() }
    private val circle_paint by lazy { Paint() }
    private val window_paint by lazy { Paint() }
    private val window_text_paint by lazy { Paint() }
    private val paths = arrayListOf<Path>()
    private val click_list = arrayListOf<PointF>()
    private var list_size = 0
    var dataclicklistener: OnDataClickListener? = null
    private var line_length = 0
    private var is_window_draw = false
    private val line_paint by lazy { Paint() }

    private var lists = arrayListOf<ArrayList<Float>>()
    private var unit_hei = 0f

    constructor(context: Context) : this(context, null)

    init {
        if (attributeSet != null) {

        }
        line_length = dip(60)
        isClickable = true
        initUtil()
    }

    fun setInitData(unit_hei: Float, lists: ArrayList<ArrayList<Float>>) {
        paths.clear()
        (0 until lists.size).forEach { paths.add(Path()) }
        (0 until lists[0].size).forEach { i ->
            val x = line_length * i.toFloat()
            val y = (-lists[0][i] * unit_hei)
            click_list.add(PointF(x, y))
        }
        list_size = lists[0].size
        this.unit_hei = unit_hei
        this.lists = lists
        requestLayout()
        invalidate()
    }

    private fun initUtil() {
        paint.strokeWidth = dip(2f).toFloat()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true

        circle_paint.style = Paint.Style.FILL
        circle_paint.color = Color.BLUE

        text_paint.textSize = sp(13).toFloat()
        text_paint.color = Color.BLACK
        text_paint.isAntiAlias = true
        text_paint.style = Paint.Style.FILL

        window_paint.color = Color.parseColor("#30000000")
        window_paint.isAntiAlias = true
        window_paint.style = Paint.Style.FILL

        window_text_paint.textSize = sp(11).toFloat()
        window_text_paint.color = Color.WHITE
        window_text_paint.isAntiAlias = true
        window_text_paint.style = Paint.Style.FILL

        line_paint.color = Color.GRAY
        line_paint.isAntiAlias = true
        line_paint.strokeWidth = dip(2).toFloat()
        line_paint.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wid = (list_size - 1) * line_length + dip(15)
        setMeasuredDimension(wid, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.translate(0f, (bottom - dip(20f)).toFloat())

        paint.color = Color.BLACK
        canvas?.drawLine(0f, 0f, width.toFloat(), 0f, paint)

        if (is_window_draw) {
            val x = line_length * click_item
            canvas?.drawLine(x, 0f, x, -bottom.toFloat(), line_paint)
        }

        for (i in 0 until paths.size) {
            val color = when (i) {
                0 -> Color.BLUE
                1 -> Color.RED
                else -> Color.GREEN
            }
            paint.color = color
            circle_paint.color = color
            val path = paths[i]
            path.rewind()
            val list = lists[i]
            (0 until list.size).forEach { i ->
                val x = line_length * i.toFloat()
                val y = -list[i] * unit_hei
                if (i == 0) {
                    path.moveTo(0f, y)
                } else {
                    path.lineTo(x, y)
                }
                if (i.toFloat() == click_item) {
                    if (is_window_draw) {
                        canvas?.drawCircle(x, y, dip(5).toFloat(), circle_paint)
                    } else {
                        canvas?.drawCircle(x, y, dip(3).toFloat(), circle_paint)
                    }
                } else {
                    canvas?.drawCircle(x, y, dip(3).toFloat(), circle_paint)
                }
                val text_length = text_paint.measureText(i.toString())
                if (x < text_length / 2) {
                    canvas?.drawText(i.toString(), x, dip(13).toFloat(), text_paint)
                } else {
                    canvas?.drawText(i.toString(), x - text_length / 2, dip(13).toFloat(), text_paint)
                }

            }
            canvas?.drawPath(path, paint)
        }

        DrawWindow(canvas)
    }

    private var click_item = -1f

    fun DrawWindow(canvas: Canvas?) {
        if (is_window_draw) {
            val left = line_length * click_item + dip(5)
            val top = -bottom.toFloat() + dip(25)
            val bottom = top + dip(72)
            val right = left + dip(80)
            val radius = dip(3).toFloat()
            val recf = RectF(left, top, right, bottom)
            val list = dataclicklistener?.getData(click_item.toInt())!!
            canvas?.drawRoundRect(recf, radius, radius, window_paint)
            canvas?.drawText("时间", left + dip(5), top + dip(16), window_text_paint)
            canvas?.drawText(list[0].city, left + dip(5), top + dip(16) * 2, window_text_paint)
            canvas?.drawText(list[1].city, left + dip(5), top + dip(16) * 3, window_text_paint)
            canvas?.drawText(list[2].city, left + dip(5), top + dip(16) * 4, window_text_paint)
            val value_1 = list[0].value
            val value_2 = list[1].value
            val value_3 = list[2].value
            canvas?.drawText(dataclicklistener?.getTime(), right - window_text_paint.measureText(dataclicklistener?.getTime() + " "), top + dip(16), window_text_paint)
            canvas?.drawText(value_1, right - window_text_paint.measureText("$value_1 "), top + dip(16) * 2, window_text_paint)
            canvas?.drawText(value_2, right - window_text_paint.measureText("$value_2 "), top + dip(16) * 3, window_text_paint)
            canvas?.drawText(value_3, right - window_text_paint.measureText("$value_3 "), top + dip(16) * 4, window_text_paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_UP -> {
                val point = PointF(x!!, -y!!)
                isLinePoint(point, click_list)
            }
        }
        return true
    }

    private fun isLinePoint(pointF: PointF, pointFs: ArrayList<PointF>) {
        var isLine = false
        (0 until pointFs.size).forEach {
            val circle = click_list[it]
            if (Math.abs(pointF.x - circle.x) <= dip(7)) {
                click_item = it.toFloat()
                isLine = true
                click_item = it.toFloat()
                return@forEach
            }
        }
        is_window_draw = isLine
        invalidate()
    }

    interface OnDataClickListener {

        fun getData(position: Int): ArrayList<RatioData>

        fun getTime(): String
    }

    data class RatioData(val city: String, val value: String)

}