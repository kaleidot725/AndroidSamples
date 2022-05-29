package jp.kaleidot725.movie

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import coil.load
import coil.request.videoFrameMillis
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = Uri.parse("android.resource://" + packageName + "/" + R.raw.movie)
        image_view.load(url) {
            videoFrameMillis(1000)
            fetcher(VideoFrameUriFetcher(applicationContext))
        }
    }
}