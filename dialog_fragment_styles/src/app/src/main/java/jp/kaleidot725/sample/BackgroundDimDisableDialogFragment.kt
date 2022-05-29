package jp.kaleidot725.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class BackgroundDimDisableDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_background_dim_disabled, container,  false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(getStyle(), getThemeResId())
    }

    private fun getStyle(): Int = STYLE_NORMAL
    private fun getThemeResId(): Int = R.style.Theme_Sample_BackgroundDimDisabledDialog
}