package jp.kaleidot725.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerViewAdapter = CustomAdapter(SAMPLE_DATA).also { adapter ->
            adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
        }
        recycler_view.adapter = recyclerViewAdapter

        val recyclerViewLayoutManager =  LinearLayoutManager(applicationContext).also {
            lm -> lm.orientation = RecyclerView.VERTICAL
        }
        recycler_view.layoutManager = recyclerViewLayoutManager
    }

    companion object {
        private val SAMPLE_DATA = listOf("A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    }
}
