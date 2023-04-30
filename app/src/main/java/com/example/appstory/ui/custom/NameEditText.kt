package com.example.appstory.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class NameEditText : AppCompatEditText{
    private lateinit var clearButtonImage: Drawable

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }
    private fun init(){
        hint = "Masukkan Username"
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

}