package com.test.minipaint

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import androidx.core.content.res.ResourcesCompat

private const val STROKE_WIDTH: Float = 12f

class MyCanvasView(context: Context) : View(context) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.teal_200, null)
    private val drawColor = ResourcesCompat.getColor(resources, R.color.purple_500, null)

    private val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true // smooth movement
        isDither = true // colors with better precision
        style = Paint.Style.STROKE // make an outlined line
        strokeJoin = Paint.Join.ROUND // How lines and curve join a stroked path
        strokeCap = Paint.Cap.ROUND // Shape of the end of the line
        strokeWidth = STROKE_WIDTH // Stroke width
    }

    private var path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
    }
}