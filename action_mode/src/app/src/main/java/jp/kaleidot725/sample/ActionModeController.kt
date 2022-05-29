package jp.kaleidot725.sample

import android.app.Activity
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes

class ActionModeController(
    @MenuRes private val resId: Int,
    private val type: Int,
    private val onAction : (MenuItem) -> Unit
) {
    fun startActionMode(activity: Activity) {
        activity.startActionMode(createActionModeCallback(), type)
    }

    fun startActionMode(view: View) {
        view.startActionMode(createActionModeCallback(), type)
    }

    private fun createActionModeCallback(): ActionMode.Callback {
        return object: ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                Log.v("TAG", "onCreateActionMode")
                mode.menuInflater.inflate(resId, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                Log.v("TAG", "onPrepareActionMode")
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                Log.v("TAG", "onDetroyActionMode")
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                Log.v("TAG", "onActionItemClicked")
                onAction.invoke(item)
                mode.finish()
                return true
            }
        }
    }
}
