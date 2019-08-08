package com.moneyburndown.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.moneyburndown.R
import com.moneyburndown.extensions.dayOfMonthString
import com.moneyburndown.extensions.getColor
import com.moneyburndown.extensions.plusDays
import com.moneyburndown.extensions.toSmallString
import com.moneyburndown.model.Change
import com.moneyburndown.model.Limit
import org.koin.core.KoinComponent
import java.util.*

class BurndownView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), KoinComponent {

    private val dayCaptions = mutableListOf<String>()
    private var values = arrayOf<String>()

    private var daySpace = resources.getDimensionPixelOffset(R.dimen.day_space)
    private val textSpace = resources.getDimension(R.dimen.text_space)

    private var now = Date()

    var start = Date()
        set(value) {
            field = value
            generateDayCaptions()
        }

    var end = Date()
        set(value) {
            field = value
            generateDayCaptions()
        }

    var limit: Limit = Limit()
        set(value) {
            field = value
            now = Date()
            start = Date(limit.start)
            end = Date(limit.end)
            values = arrayOf("0", (limit.value / 2).toSmallString(), limit.value.toSmallString())
            requestLayout()
            invalidate()
        }

    var changes = listOf<Change>()
        set(value) {
            now = Date()
            field = value.filter { it.date >= start.time && it.date <= now.time }
                .sortedBy { it.date }
            requestLayout()
            invalidate()
        }

    fun update() {
        now = Date()
        invalidate()
    }

    private fun generateDayCaptions() {
        dayCaptions.clear()
        var currentDate = start
        while (currentDate < end) {
            dayCaptions.add(currentDate.dayOfMonthString())
            currentDate = currentDate.plusDays(1)
        }
    }

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bottomTextPaint = Paint(basePaint).apply {
        color = getColor(R.color.colorGreyLine)
        textSize = resources.getDimension(R.dimen.text_size)
        textAlign = Paint.Align.LEFT
    }

    private val leftTextPaint = Paint(bottomTextPaint).apply {
        textAlign = Paint.Align.RIGHT
    }

    private val axesLinePaint = Paint(basePaint).apply {
        color = getColor(R.color.colorGreyLine)
        strokeWidth = resources.getDimension(R.dimen.line_width)
    }

    private val guideLinePaint = Paint(basePaint).apply {
        color = getColor(R.color.colorGreyLine)
        strokeWidth = resources.getDimension(R.dimen.guide_line_width)
    }

    private val mainLinePaint = Paint(guideLinePaint).apply {
        color = getColor(R.color.colorPrimaryDark)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val currentWidth = MeasureSpec.getSize(widthMeasureSpec)

        daySpace = resources.getDimensionPixelOffset(R.dimen.day_space)
        val width = dayCaptions.size * daySpace
        val newWidthMeasureSpec = if (width > currentWidth) {
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        } else {
            daySpace = currentWidth / dayCaptions.size
            MeasureSpec.makeMeasureSpec(currentWidth, MeasureSpec.EXACTLY)
        }

        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas == null) {
            return
        }

        val topY = paddingTop.toFloat()
        val bottomY = (height - paddingBottom).toFloat()
        val startX = paddingStart.toFloat()
        val endX = (width - paddingRight).toFloat()
        val heightClean = (height - paddingTop - paddingBottom).toFloat()
        val widthClean = (width - paddingStart - paddingEnd).toFloat()
        val dayWidth = widthClean / dayCaptions.size.toFloat()

        // bottom line
        canvas.drawLine(startX, bottomY, endX, bottomY, axesLinePaint)

        // left line
        canvas.drawLine(startX, topY, startX, bottomY, axesLinePaint)

        // guide line
        canvas.drawLine(startX, topY, endX, bottomY, guideLinePaint)

        // values at start
        canvas.drawText(values[0], startX - textSpace, bottomY, leftTextPaint)
        canvas.drawText(values[1], startX - textSpace, bottomY / 2, leftTextPaint)
        canvas.drawText(values[2], startX - textSpace, paddingTop.toFloat() + leftTextPaint.textSize, leftTextPaint)

        // day names at the bottom
        var currentXText = dayWidth / 2f
        for (day in dayCaptions) {
            canvas.drawText(day, currentXText, bottomY + textSpace + bottomTextPaint.textSize, bottomTextPaint)

            currentXText += dayWidth
        }

        // change line
        var currentX = startX
        var currentY = topY
        var nextX: Float
        var nextY = currentY
        var previousChange = start.time
        val timeFrame = (end.time - start.time).toFloat()

        for (change in changes) {
            // time delta till next change
            nextX = currentX + (change.date - previousChange).toFloat() / timeFrame * widthClean
            previousChange = change.date
            canvas.drawLine(currentX, currentY, nextX, nextY, mainLinePaint)
            currentX = nextX

            // change delta
            nextY = currentY + change.value.toFloat() / limit.value.toFloat() * heightClean
            canvas.drawLine(currentX, currentY, nextX, nextY, mainLinePaint)
            currentY = nextY
        }

        // draw timeline till now
        nextX = currentX + (now.time - previousChange).toFloat() / timeFrame * widthClean
        canvas.drawLine(currentX, currentY, nextX, nextY, mainLinePaint)
    }
}