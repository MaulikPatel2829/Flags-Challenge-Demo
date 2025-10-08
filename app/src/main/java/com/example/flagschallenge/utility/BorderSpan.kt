package com.example.flagschallenge.utility

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.ReplacementSpan

class BorderSpan(
    private val borderColor: Int,
    private val borderWidth: Float,
    private val padding: Float
) : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return (paint.measureText(text, start, end) + 2 * padding).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        // Draw the background color if needed
        // val backgroundColor = paint.color
        // paint.color = yourBackgroundColor
        // canvas.drawRect(x, top.toFloat(), x + width, bottom.toFloat(), paint)
        // paint.color = backgroundColor

        // Draw the border
        val borderPaint = Paint(paint)
        borderPaint.style = Paint.Style.STROKE
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth

        val textWidth = paint.measureText(text, start, end)
        val rect = android.graphics.RectF(x, top.toFloat(), x + textWidth + 2 * padding, bottom.toFloat())

        canvas.drawRect(rect.left + borderWidth / 2, rect.top + borderWidth / 2, rect.right - borderWidth / 2, rect.bottom - borderWidth / 2, borderPaint)

        // Draw the text inside the border
        canvas.drawText(text!!, start, end, x + padding, y.toFloat(), paint)
    }
}
