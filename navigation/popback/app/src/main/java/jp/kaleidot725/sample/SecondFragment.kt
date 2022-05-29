package jp.kaleidot725.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import jp.kaleidot725.sample.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(R.layout.fragment_second) {
    private val navController: NavController get() = findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.textview_second)
        textView.text = navController.currentDestination?.label ?: "NULL"

        val button = view.findViewById<Button>(R.id.button_pop_back_stack)
        button.setOnClickListener {
            navController.popBackStack()
            textView.text = navController.currentDestination?.label ?: "NULL"
        }
    }
}