package jp.kaleidot725.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment(R.layout.fragment_start) {
    private val navController get() = findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start_image_view.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                start_image_view to "end_image_view_transition",
                start_text_view to "end_text_view_transition"
            )
            navController.navigate(R.id.action_startFragment_to_endFragment, null, null, extras)
        }
    }
}
