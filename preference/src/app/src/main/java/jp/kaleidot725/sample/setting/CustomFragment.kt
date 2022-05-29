package jp.kaleidot725.sample.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import coil.api.load
import jp.kaleidot725.sample.R
import kotlinx.android.synthetic.main.fragment_custom.*

class CustomFragment : Fragment(R.layout.fragment_custom) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        custom_image_view.load("https://via.placeholder.com/150")
    }
}
