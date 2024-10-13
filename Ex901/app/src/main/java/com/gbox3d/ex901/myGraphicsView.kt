package com.gbox3d.ex901

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import java.io.File

class myGraphicsView(context: Context) : View(context) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val stroke_paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    //리스트에 원의 위치를 저장한다.
    private val circleList = mutableListOf<Pair<Float, Float>>()

    //원을 모두 지우기 위한 함수
    public fun clear() {
        circleList.clear()
        invalidate()
    }

    private var imageBitmap: Bitmap? = null

    // 외부 저장소에서 이미지를 로드하여 Bitmap으로 변환
    fun loadImage(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            imageBitmap = BitmapFactory.decodeFile(filePath)
            invalidate() // 화면을 갱신하여 onDraw를 호출한다.
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 이미지가 있다면 그리기
        imageBitmap?.let {
            //화면크기에 맞게 이미지를 스케일링한다.
            val dstRect = Rect(0, 0, width, height)
            canvas.drawBitmap(it, null, dstRect, null)
            
            // canvas.drawBitmap(it, 0f, 0f, null)
        }

        //원을 그린다.
        circleList.forEach {
            canvas.drawCircle(it.first, it.second, 50f, paint) // 원의 위치를 받아서 그린다.
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    circleList.add(Pair(it.x, it.y)) // 리스트에 원의 위치를 저장한다.
                    invalidate() // 화면을 갱신한다.
                }
            }
        }
        return super.onTouchEvent(event)
    }
}