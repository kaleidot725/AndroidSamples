# ［Android］Coil の Video Frames を使ってみる

# はじめに

Coil の Video Frames を使うことで動画ファイルのあるフレーム画像を ImageView に表示できるらしい。
今回はどのような感じで取得できるのか試してみたいと思う。

# 準備

Video Frames を使う場合には Core ライブラリに加えて Video Frames ライブラリを依存関係に追加する必要があります。
次のように build.gradle に追記して Video Frames ライブラリを依存関係として追加します。

```groovy
dependencies {
      ︙
    implementation "io.coil-kt:coil:1.0.0"
    implementation "io.coil-kt:coil-video:1.0.0"
      ︙
}
```

# 実装

## raw フォルダに動画を格納する

動画ファイルが必要になるので次のように raw フォルダを作成し格納しておきます。
(対応している動画ファイルの拡張子→[サポートされているメディア形式](https://developer.android.com/guide/topics/media/media-formats#video-formats))

[![Image from Gyazo](https://i.gyazo.com/76f7e6451f96c64e42379fd0c02d90c4.png)](https://gyazo.com/76f7e6451f96c64e42379fd0c02d90c4)
[![Image from Gyazo](https://i.gyazo.com/5f8d6a50628a5de598358fc7f5329d4a.gif)](https://gyazo.com/5f8d6a50628a5de598358fc7f5329d4a)

## raw フォルダから動画ファイルを読み込む

まずは raw フォルダに格納したファイルの URL を生成し、load にてその url を読み込んでやります。あとは videoFrameMillis にて動画のどの秒数のフレームを表示するか指定してやれば動作します。

下記のサンプルだと fetcher で VideoFrameUriFetcher を指定してやっています。
本来であれば Coil 側で動画ファイルであるか自動的に判定してくれるので指定する必要は
ないみたいなのですが URL を指定した場合だと上手く動作しないので追加しています。

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // RAWリソースのURLを生成するa
        val url = Uri.parse("android.resource://" + packageName + "/" + R.raw.movie)
        
        // fetcher に VideoFrameUriFetcher を指定してやると動画からフレームを切り出してくれる
        image_view.load(url) {
            videoFrameMillis(1000)
            fetcher(VideoFrameUriFetcher(applicationContext))
        }
    }
}
```

[![Image from Gyazo](https://i.gyazo.com/387b2852c3ad8672a151fcbd107b30cc.png)](https://gyazo.com/387b2852c3ad8672a151fcbd107b30cc)

また URL だけではなく File での読み込みもできるようになっているみたいです。
URLの場合には VideoFrameUriFetcher を指定しましたが File の場合は VideoFrameFileFetcher を指定してやります。

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ファイル
        val file = File("任意のファイルパス")
        
        // fetcher に VideoFrameFileFetcher を指定してやると動画からフレームを切り出してくれる
        image_view.load(file) {
            videoFrameMillis(1000)
            fetcher(VideoFrameFileFetcher(applicationContext))
        }
    }
}
```

# おわりに

Coil の Video Frames では特定の動画ファイルから特定のフレームを画像として切り出すことができます。これを使えば動画ファイルの読み込み中に表示するサムネイルの取得などができそうです。使う機会が多いとは言えない機能ですが頭の片隅に覚えていれておけばどこかで使えそうだと思いました。

# 参考文献

- [Coil - Video Frames](https://coil-kt.github.io/coil/videos/)
