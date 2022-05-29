package jp.kaleidot725.gif

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.decode.Decoder
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uri = getGifUri(R.raw.circle)
        val loader = ImageLoader.Builder(applicationContext).componentRegistry { add(getGifDecoder()) }.build()

        val imageView = findViewById<ImageView>(R.id.gif_image_view)
        imageView.load(uri = uri, imageLoader = loader)
    }

    // RawResId から Uri を取得する
    private fun getGifUri(@RawRes rawResId: Int): Uri? {
        return Uri.parse("android.resource://$packageName/$rawResId")
    }

    // API 28 以上だと ImageDecoderDecoder が Android より提供されている
    // GifDecoder を使うよりは ImageDecoderDecoder を使うのが早いらしく API 28 以上は使うことを推奨している
    private fun getGifDecoder(): Decoder {
        return if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder() else GifDecoder()
    }
}
