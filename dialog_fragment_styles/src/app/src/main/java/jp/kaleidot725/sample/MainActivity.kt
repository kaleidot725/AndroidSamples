package jp.kaleidot725.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val normalDialogFragmentButton = findViewById<Button>(R.id.normal_dialog_fragment_button)
        normalDialogFragmentButton.setOnClickListener {
            NormalDialogFragment().show(supportFragmentManager, "")
        }

        val fullScreenDialogButton = findViewById<Button>(R.id.fullscreen_dialog_fragment_button)
        fullScreenDialogButton.setOnClickListener {
            FullScreenDialogFragment().show(supportFragmentManager, "")
        }

        val backgroundDialogFragmentButton = findViewById<Button>(R.id.background_dialog_fragment)
        backgroundDialogFragmentButton.setOnClickListener {
            BackgroundDialogFragment().show(supportFragmentManager, "")
        }

        val roundedDialogFragmentButton = findViewById<Button>(R.id.rounded_dialog_fragment)
        roundedDialogFragmentButton.setOnClickListener {
            RoundedDialogFragment().show(supportFragmentManager, "")
        }

        val backgroundDimDisableDialogFragmentButton = findViewById<Button>(R.id.background_dim_disabled_dialog_fragment)
        backgroundDimDisableDialogFragmentButton.setOnClickListener {
            BackgroundDimDisableDialogFragment().show(supportFragmentManager, "")
        }

        val windowCloseOnTouchOutsideDisableDialogFragmentButton = findViewById<Button>(R.id.window_close_on_touch_outside_disable_dialog_fragment)
        windowCloseOnTouchOutsideDisableDialogFragmentButton.setOnClickListener {
            WindowCloseOnTouchOutsideDisableDialogFragment().show(supportFragmentManager, "")
        }
    }
}
