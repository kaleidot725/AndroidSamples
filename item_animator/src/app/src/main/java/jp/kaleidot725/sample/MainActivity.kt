package jp.kaleidot725.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.itemanimators.ScaleUpAnimator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val min = 0
    private var max = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layoutManager = LinearLayoutManager(applicationContext)
        val customAdapter =  CustomRecyclerAdapter().apply { data = createSampleData(min, max) }
        val scaleUpAnimator = ScaleUpAnimator().apply {
            addDuration = 2000
            removeDuration = 4000
        }

        recycler_view.also { view ->
            view.adapter = customAdapter
            view.layoutManager = layoutManager
            view.itemAnimator = scaleUpAnimator
            view.setHasFixedSize(true)
        }

        plus_button.setOnClickListener {
            max++

            val new = createSampleData(min, max)
            customAdapter.data = new
            customAdapter.notifyItemInserted(max)
        }

        minus_button.setOnClickListener {
            max--
            val new = createSampleData(min, max)
            customAdapter.data = new
            customAdapter.notifyItemRemoved(max + 1)
        }
    }

    private fun createSampleData(min: Int, max: Int) = (min..max).map { it.toString() }
}
