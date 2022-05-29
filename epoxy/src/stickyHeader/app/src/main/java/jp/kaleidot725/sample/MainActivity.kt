package jp.kaleidot725.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.epoxy.stickyheader.StickyHeaderLinearLayoutManager
import jp.kaleidot725.sample.controller.Content
import jp.kaleidot725.sample.controller.Header
import jp.kaleidot725.sample.controller.StickyHeaderController
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stickyHeaderController = StickyHeaderController()
        epoxy_recycler_view.adapter = stickyHeaderController.adapter
        epoxy_recycler_view.layoutManager = StickyHeaderLinearLayoutManager(applicationContext)
        stickyHeaderController.setData(createHeaders(10), createContents(100))
    }

    private fun randomUUIDString(): String {
        return UUID.randomUUID().toString()
    }

    private fun createHeaders(max: Long): List<Header> {
        return (0..max).map { count -> Header(randomUUIDString(), "Header $count") }
    }

    private fun createContents(max: Long): List<Content> {
        return (0..max).map { count -> Content(randomUUIDString(), "Content $count") }
    }
}
