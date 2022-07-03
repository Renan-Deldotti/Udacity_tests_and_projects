package com.test.minipaint

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

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
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private lateinit var frame: Rect

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        event?.let {
            motionTouchEventX = it.x
            motionTouchEventY = it.y

            when (it.action) {
                MotionEvent.ACTION_DOWN -> touchStart()
                MotionEvent.ACTION_UP -> touchUp()
                MotionEvent.ACTION_MOVE -> touchMove()
            }
        }
        return true
    }

    // When user put the finger on screen
    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    // When user removes the finger from screen
    private fun touchUp() {
        path.reset()
    }

    // When user moves the finger on screen
    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        val inset = 40
        frame = Rect(inset, inset, width - inset, height - inset)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(extraBitmap, 0f, 0f, null)
        canvas?.drawRect(frame, paint)
    }
}