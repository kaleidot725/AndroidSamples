package jp.kaleidot725.sample

import android.os.Bundle
import android.view.ActionMode
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val primaryActionModeController = ActionModeController(R.menu.action_menus, ActionMode.TYPE_PRIMARY) {
            when (it.itemId) {
                R.id.first -> {
                    Toast.makeText(context, "FIRST", Toast.LENGTH_SHORT).show()
                }
                R.id.second -> {
                    Toast.makeText(context, "SECOND", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val floatingActionModeController = ActionModeController(R.menu.action_menus, ActionMode.TYPE_FLOATING) {
            when (it.itemId) {
                R.id.first -> {
                    Toast.makeText(context, "FIRST", Toast.LENGTH_SHORT).show()
                }
                R.id.second -> {
                    Toast.makeText(context, "SECOND", Toast.LENGTH_SHORT).show()
                }
            }
        }

        primary_action_mode_activity_button.setOnClickListener {
            primaryActionModeController.startActionMode(requireActivity())
        }

        primary_action_mode_view_button.setOnClickListener {
            primaryActionModeController.startActionMode(it)
        }

        floating_action_mode_activity_button.setOnClickListener {
            floatingActionModeController.startActionMode(requireActivity())
        }

        floating_action_mode_view_button.setOnClickListener {
            floatingActionModeController.startActionMode(it)
        }
    }
}
