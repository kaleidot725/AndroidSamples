package jp.kaleidot725.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(R.layout.fragment_first) {
    private val navController: NavController get() = findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.textview_first)
        textView.text = navController.currentDestination?.label ?: "NULL"

        val nextButton = view.findViewById<Button>(R.id.button_next)
        nextButton.setOnClickListener {
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
            textView.text = navController.currentDestination?.label ?: "NULL"
        }

        val backButton = view.findViewById<Button>(R.id.button_pop_back_stack)
        backButton.setOnClickListener {
            navController.popBackStack()
            textView.text = navController.currentDestination?.label ?: "NULL"
        }
    }
}