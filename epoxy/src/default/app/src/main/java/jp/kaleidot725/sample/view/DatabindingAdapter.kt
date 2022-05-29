package jp.kaleidot725.sample.view

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:setOnClickListener")
fun setOnClickListener(view: View, listener: View.OnClickListener) {
    view.setOnClickListener(listener)
}