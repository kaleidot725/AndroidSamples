## Jetpack Compose の AndroidView で WebView を利用する

## はじめに

Jetpack Compose で WebView を使いたいのであれば AndroidView を利用して WebView を生成する必要がある。AndroidView を利用した WebView の生成方法について少し調べたのでまとめる。

## セットアップ

本記事では以下の環境で動作確認をしています。

- Kotlin  v1.5.21
- Jetpack Compose v1.0.1
- Android Studio Bublebee | 2021.1.1 Canary 7

## AndroidView とは

Jetpack Compose では AndroidView というコンポーザブルが用意されている。AndroidView を利用することで TextView や EditView などの View 要素を Jetpack Compose の UI 階層に埋め込むことができる。この AndroidView コンポーザブルを使うと Jetpack Compose でも WebView を利用できるようになる。

```kotlin
<T : View> AndroidView(
    factory: (Context) -> T,
    modifier: Modifier,
    update: (T) -> Unit
)
```

## AndroidView コンポーザブルで WebView を生成する

AndroidView コンポーザブルで WebView を生成するのは簡単です。以下のように AndroidView を呼び出して factory と update メソッドを定義してやるだけで WebView を生成して制御することができます。

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MyWebClient(url = "https://kaleidot.net") }
    }
}

// MEMO
// factory は View を生成するときに一度だけ呼ばれるメソッド
// 　今回は WebView を生成したいので factory には WebView のコンストラクターを参照して渡す
// 
// update は UI 階層の再構成時に複数回呼ばれるメソッド
// 　factory で生成した View に対して update で表示更新するような仕組みになっている
// 　今回は生成された WebView に対して特定の URL の Web ページを表示するようにしてみる
@Composable
fun MyWebClient(url: String) {
    AndroidView(
        factory = ::WebView,
        update = { webView ->
            webView.webViewClient = WebViewClient()
            webView.loadUrl(url)
        }
    )
}
```

![Image](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/AE00E7DA-2A62-4D75-A7C6-2F2E356A71F2/294A1AC5-84FE-4A21-BC3E-D25D9BF0F0EF_2/Image)

## おわりに

というように Jetpack Compose では AndroidView を利用すれば WebView や MapView や AdView など既存 SDK で実装された View が利用できます。Jetpack Compose の UI 要素で未対応のものがあれば AndroidView で切り抜けられるようになっていて大変便利ですね。
