package jp.kaleidot725.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create & Setup LayoutManager
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler_view.layoutManager = layoutManager

        // Create & Setup Adapter
        val adapter = NumberAdapter()
        recycler_view.adapter = adapter

        // Create & Setup Tracker
        adapter.tracker = buildTracker(recycler_view, adapter)

        // Update
        adapter.data = createNumbers()
        adapter.notifyDataSetChanged()
    }

    private fun createNumbers() = (0..100).mapIndexed { index, number ->
        Number(index, number.toString())
    }

    private fun buildTracker(v: RecyclerView, a: NumberAdapter): SelectionTracker<Number> {
        return SelectionTracker.Builder<Number>(
            SELECTION_TRACKER, v, NumberKeyProvider(a),
            NumberDetailsLookup(v), StorageStrategy.createParcelableStorage(Number::class.java)
        ).build()
    }

    companion object {
        private const val SELECTION_TRACKER = "NUMBER_SELECTION_TRACKER"
    }
}
