package jp.kaleidot725.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import jp.kaleidot725.sample.view.HeaderCustomViewController
import jp.kaleidot725.sample.view.HeaderDatabindingViewController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val itemList = listOf(
        "ONE", "TWO", "THREE", "FOUR", "FIVE",
        "SIX", "SEVEN", "EIGHT", "NINE", "TEN"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val headerDatabindingViewController = HeaderDatabindingViewController(object :
            HeaderDatabindingViewController.SelectListener {
            override fun onSelected(item: String) {
                Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
            }
        })

        recycler_view.apply {
            this.adapter = headerDatabindingViewController.adapter
            this.layoutManager = LinearLayoutManager(applicationContext).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }

        headerDatabindingViewController.setData(itemList, true)
    }
}
