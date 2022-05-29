package jp.kaleidot725.sample

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class MainDialogFragment(
    private val gravity: Int,
    private val verticalMargin: Float,
    private val horizontalMargin: Float
): DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adjustDialogPosition()
    }

    private fun adjustDialogPosition() {
        dialog?.window?.attributes?.also { attributes ->
            attributes.gravity = this.gravity
            attributes.verticalMargin = this.verticalMargin
            attributes.horizontalMargin = this.horizontalMargin
        }
    }
}
