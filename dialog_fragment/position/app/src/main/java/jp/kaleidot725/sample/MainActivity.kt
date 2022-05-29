package jp.kaleidot725.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show_left_top_button.setOnClickListener {
            MainDialogFragment(Gravity.LEFT or Gravity.TOP, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_center_top_button.setOnClickListener {
            MainDialogFragment(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_right_top_button.setOnClickListener {
            MainDialogFragment(Gravity.RIGHT or Gravity.TOP, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_left_center_button.setOnClickListener {
            MainDialogFragment(Gravity.LEFT or Gravity.CENTER, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_center_button.setOnClickListener {
            MainDialogFragment(Gravity.CENTER, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_right_center_button.setOnClickListener {
            MainDialogFragment(Gravity.RIGHT or Gravity.CENTER, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_left_bottom_button.setOnClickListener {
            MainDialogFragment(Gravity.LEFT or Gravity.BOTTOM, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_center_bottom_button.setOnClickListener {
            MainDialogFragment(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_right_bottom_button.setOnClickListener {
            MainDialogFragment(Gravity.RIGHT or Gravity.BOTTOM, 0f, 0f).show(supportFragmentManager,"TAG")
        }
    }
}
