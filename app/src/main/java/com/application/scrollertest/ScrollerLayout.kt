package com.application.scrollertest

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Scroller

/**
 * Created by EdgeDi
 * 2018/4/16 9:19
 */
class ScrollerLayout(context: Context, attributeSet: AttributeSet?) : ViewGroup(context, attributeSet) {

    /**
     * 用于滚动的实例
     */
    private val mScroller by lazy { Scroller(context) }

    /**
     * 拖动的最小像素
     */
    private var mTouchSlop = 0

    /**
     * 按下的坐标
     */
    private var mXDown = 0f

    /**
     * 现在所处的坐标
     */
    private var mXMove = 0f

    /**
     * 上次触发ACTION_MOVE时的坐标
     */
    private var mXLastMove = 0f

    /**
     * 可滚动的左界面
     */
    private var leftBorder = 0

    /**
     * 可滚动的右界面
     */
    private var rightBorder = 0

    constructor(context: Context) : this(context, null)

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledPagingTouchSlop
        initChild()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            if (childCount == 0) {
                initChild()
                addView(line)
            }
            for (i in 0 until childCount) {
                val child_view = getChildAt(i)
                child_view.layout(i * child_view.measuredWidth, 0, (i + 1) * child_view.measuredWidth, child_view.measuredHeight)
            }
            leftBorder = getChildAt(0).left
            rightBorder = getChildAt(childCount - 1).right
        }
    }

    private var line: RatioLineView? = null

    private fun initChild() {
        line = RatioLineView(context)
        val lp = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        line?.layoutParams = lp
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(line)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?) =
            when (ev?.action) {
                MotionEvent.ACTION_DOWN -> {
                    mXDown = ev.rawX
                    mXLastMove = mXDown
                    super.onInterceptTouchEvent(ev)
                }
                MotionEvent.ACTION_MOVE -> {
                    mXMove = ev.rawX
                    val diff = Math.abs(mXMove - mXDown)
                    mXLastMove = mXMove
                    if (diff > mTouchSlop) {
                        true
                    } else {
                        super.onInterceptTouchEvent(ev)
                    }
                }
                else -> super.onInterceptTouchEvent(ev)
            }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                mXMove = event.rawX
                val scrolledX = (mXLastMove - mXMove).toInt()
                if (scrollX + scrolledX < leftBorder) {
                    scrollTo(leftBorder, 0)
                    return true
                } else if (scrollX + width + scrolledX > rightBorder) {
                    scrollTo(rightBorder - width, 0)
                    return true
                }
                scrollBy(scrolledX, 0)
                mXLastMove = mXMove
            }
            MotionEvent.ACTION_UP -> {
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                val targetIndex = (scrollX + width / 2) / width
                val dx = targetIndex * width - scrollX
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mScroller.startScroll(scrollX, 0, Math.abs(dx) / 10, 0)
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }
}