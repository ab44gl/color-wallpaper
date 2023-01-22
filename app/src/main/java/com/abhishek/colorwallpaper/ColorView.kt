package com.abhishek.colorwallpaper

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ColorView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var isTextShow: Boolean = true
    private var isBorderShow: Boolean = false
    private var fillColor: Int = Color.WHITE
    private val textRect = Rect()
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
    }
    private val borderPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    fun showText(isTextShow: Boolean) {
        this.isTextShow = isTextShow
        invalidate()
    }

    fun showBorder(isBorderShow: Boolean) {
        this.isBorderShow = isBorderShow
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val w = measuredWidth
        val h = measuredHeight
        canvas.drawColor(fillColor)
        if (isTextShow) {
            //draw text at center of canvas
            val text =
                "(${Color.red(fillColor)},${Color.green(fillColor)},${Color.blue(fillColor)})"
            textPaint.getTextBounds(text, 0, text.length, textRect)
            canvas.drawText(
                text,
                w * 0.5f - textRect.width() * 0.5f,
                h * 0.5f - textRect.height() * 0.5f,
                textPaint
            )
        }
        if (isBorderShow) {
            canvas.drawRect(
                0f, 0f, width.toFloat(), height.toFloat(), borderPaint
            )
        }

    }

    fun setColor(rgba: Int) {
        fillColor = rgba
        invalidate()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }
}