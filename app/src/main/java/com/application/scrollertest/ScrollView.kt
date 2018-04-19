package com.application.scrollertest

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import java.util.*

/**
 * Created by EdgeDi
 * 2018/4/17 14:09
 */
class ScrollView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    /**
     * 用于滚动的实例
     */
    private val mScroller by lazy { Scroller(context) }

    private var list_1 = arrayListOf<Float>()

    private var leftBorder = 0f
    private var rightBorder = 0f

    private val paint by lazy { Paint() }
    private val paths = arrayListOf(Path())

    private val lists = arrayListOf<ArrayList<Float>>()
    private var distance = 0f
    private var pos = floatArrayOf(0f, 0f)
    private var tan = floatArrayOf(0f, 0f)
    private var step = 4f

    private var measure: PathMeasure? = null

    constructor(context: Context) : this(context, null)

    init {
        if (attributeSet != null) {

        }
        isClickable = true
        (0 until 80).forEach {
            val it = Random().nextInt(100)
            if (it > 10) {
                list_1.add(it.toFloat())
            }
        }
        lists.add(list_1)
        initUtil()
    }

    private fun initUtil() {
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLUE
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            leftBorder = left.toFloat()
            rightBorder = right.toFloat()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(list_1.size * 40, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.translate(0f, bottom.toFloat())
        for (i in 0 until paths.size) {
            val path = paths[i]
            path.rewind()
            path.moveTo(0f, 0f)
            val list = lists[i]
            (0 until list.size).forEach { i -> path.lineTo(40f * (i + 1), -list[i]) }
            canvas?.drawPath(path, paint)
            measure = PathMeasure(path, false)
//            val path_length = measure?.length ?: 0f
//            if (distance < path_length) {
//                measure?.getPosTan(distance, pos, tan)
//                canvas?.drawCircle(pos[0], pos[1], 10f, paint)
//                distance += step
//            } else {
//                distance = 0f
//            }
        }
    }

    private var start_x = 0f
    private var move_x = 0f
    private var last_start_x = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                start_x = event.rawX
                last_start_x = start_x
            }
            MotionEvent.ACTION_MOVE -> {
                move_x = event.rawX
                val scrolledx = last_start_x - move_x
                if (scaleX + scrolledx < leftBorder) {
                    scrollTo(leftBorder.toInt(), 0)
                } else if (scaleX + width + scrolledx > rightBorder) {
                    scrollTo((rightBorder - width).toInt(), 0)
                }
                scrollBy(scrolledx.toInt(), 0)
                last_start_x = move_x
            }
            MotionEvent.ACTION_UP -> {
//                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                val dx = width - scrollX
//                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mScroller.startScroll(scrollX, 0, Math.abs(dx) / 8, 0)
                invalidate()
            }
        }
        return true
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }
}