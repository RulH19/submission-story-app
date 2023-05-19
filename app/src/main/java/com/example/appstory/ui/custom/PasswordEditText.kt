package com.example.appstory.ui.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.appstory.R

class PasswordEditText : AppCompatEditText  {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

        hint = resources.getString(R.string.hint_password)
        background = ContextCompat.getDrawable(context, R.drawable.background_edt)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!text.isNullOrBlank()) {
                    error = if (text!!.length < 8) {
                        resources.getString(R.string.minimal_password)
                    } else {
                        null
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }
}