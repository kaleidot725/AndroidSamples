package jp.kaleidot725.sample.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderCustomView : LinearLayout {
    private lateinit var titleView: TextView
    private lateinit var buttonView: Button

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        titleView = TextView(context, attrs).apply {
            this.width = 500
            this.textSize = 32f
        }

        buttonView = Button(context, attrs).apply {
            buttonView.text = "ACTION"
        }

        this.addView(titleView)
        this.addView(buttonView)
    }

    @TextProp
    fun setTitle(text: CharSequence?) {
        titleView.text = text
    }

    @CallbackProp
    fun onClickListener(listener: View.OnClickListener?) {
        buttonView.setOnClickListener(listener)
    }
}

