package jp.kaleidot725.resourceselector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        ok_button.setOnClickListener {
            Toast.makeText(applicationContext, "OK", Toast.LENGTH_SHORT).show()
        }

        ng_image_button.setOnClickListener {
            Toast.makeText(applicationContext, "NG", Toast.LENGTH_SHORT).show()
        }

        color_text_enable_button.setOnClickListener {
            ok_button.isEnabled = !ok_button.isEnabled
            ng_image_button.isEnabled = !ng_image_button.isEnabled
        }
    }
}
