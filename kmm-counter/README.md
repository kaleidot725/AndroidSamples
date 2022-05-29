# [KMM で最も簡単なカウンターアプリを作ってみる](https://kaleidot.net/kmm-%E3%81%A7%E6%9C%80%E3%82%82%E7%B0%A1%E5%8D%98%E3%81%AA%E3%82%AB%E3%82%A6%E3%83%B3%E3%82%BF%E3%83%BC%E3%82%A2%E3%83%97%E3%83%AA%E3%82%92%E4%BD%9C%E3%81%A3%E3%81%A6%E3%81%BF%E3%82%8B-2d7d0e1bd7dd)

# はじめに

KMM とは Kotlin Multiplatform Mobile の略で iOS・Android 共通のビジネスロジックを Kotlin コードで実現する SDK になります。今回はカウントのビジネスロジックを KMM で共通化して Android と iOS で動かして最も簡単なカウンターアプリを作ってみたいと思います。そして KMM を使ってみてどうところに良し悪しがあるか感想を述べたいと思います。

# 開発環境のセットアップ

本記事は以下に記載するソフトウェアとバージョンで開発を進めます。 Android Studio や XCode のバージョンが異なる場合、コードが動作しない可能性がありますのでアップデートしてください。

| 項目           | バージョン                    |
| -------------- | ----------------------------- |
| OS             | macOS Big Sur バージョン 11.1 |
| Android Studio | Arctic Fox - 2020.03 Beta3    |
| XCode          | Version 12.5 (12E262)         |

あと KMM プロジェクトを作成するには Android Studio に KMM プラグインをインストールする必要があります。以下の手順で KMM プラグインをインストールしてください。

1. Android Studio -> Preferences -> Plugins を開く
2. Kotlin Multiplatform Mobile を検索してインストールする
3. Android Studio を再起動する

[![Image from Gyazo](https://i.gyazo.com/b69c8a54f8dc23042e5bf8aa6c7182b6.png)](https://gyazo.com/b69c8a54f8dc23042e5bf8aa6c7182b6)

# KMM プロジェクト作成

KMM を利用するには専用のプロジェクトを作成する必要があります。なので以下の手順で KMM プロジェクトを作成します。

1. Android Studio で Select File -> New -> New Project を選択する
2. KMM Application を選択し、Next をクリックする

[![Image from Gyazo](https://i.gyazo.com/4118bc7f388007d332e21b482ac709b2.png)](https://gyazo.com/4118bc7f388007d332e21b482ac709b2)

3. Name や Package name に任意の値を設定して Next を選択する

[![Image from Gyazo](https://i.gyazo.com/b599df19b4586ee5f9d6ebec09af339f.png)](https://gyazo.com/b599df19b4586ee5f9d6ebec09af339f)

4. Android Application Name や iOS Application Name や Shared Module Name はデフォルトのままでよいです。 あとは iOS frameworkd distribution は Regular framework を選択し Finish を選択したらプロジェクトの作成は完了です。

[![Image from Gyazo](https://i.gyazo.com/bde3ebab6bb61164e16d3f961132eeba.png)](https://gyazo.com/bde3ebab6bb61164e16d3f961132eeba)

プロジェクトの作成が完了したらサンプルプログラムが動作するようになっています。右上の Configurations で androidApp を選択して RUN したときに以下のような Android アプリ、Configurations で iosApp を選択して RUN したときには以下のような iOS アプリがビルドされ起動します。

[![Image from Gyazo](https://i.gyazo.com/0b33b8b8e1149a73fcb732920f566a80.png)](https://gyazo.com/0b33b8b8e1149a73fcb732920f566a80)
[![Image from Gyazo](https://i.gyazo.com/371c17097320842d04ee6653d0a67fa4.png)](https://gyazo.com/371c17097320842d04ee6653d0a67fa4)

# iOS と Android で共通のクラスを作成する

KMM では iOS と Android で共通のビジネスロジックを定義できるようになっています。 KMM では shared module にコードを記述するクロスプラットフォーム向けにビルドされ iOS や Android のコードから参照できるようになっています。

[![Image from Gyazo](https://i.gyazo.com/bb9acb47c409ace5ee56a7b1e66b0da8.png)](https://gyazo.com/bb9acb47c409ace5ee56a7b1e66b0da8)
例えば commonMain に MyCounter という以下のクラスを定義します。これだけで MyCounter が iOS や Android のコード上で使えるようになります。

```kotlin
package jp.kaleidot725.counter.shared

class MyCounter(val min: Int, val max: Int) {
    var value: Int = 0
        private set

    init {
        check(min < max)
    }

    fun plus() {
        if (max < (value + 1)) {
            return
        }

        value++
    }

    fun minus() {
        if ((value - 1) < min) {
            return
        }

        value--
    }
}
```

Android Studio で androidApp モジュールを開いて MyCounter のインスタンスを作成するコードを書いてみます。すると問題なくビルドが通り Adnroid から MyCounter のインスタンスが取得できるようになっています。

```kotlin
import jp.kaleidot725.counter.shared.MyCounter

val counter = MyCounter(min = 0, max = 100)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 省略
    }
}
```

XCode で iosApp モジュールを開いて MyCounter のインスタンスを作成するコードをこちらでも書いてみます。するとこちらも問題なくビルドが通り iOS から MyCounter のインスタンスが取得できるようになっています。

```swift
import SwiftUI
import shared

@main
struct iOSApp: App {
    let counter = MyCounter(min: 0, max: 100)

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
```

# iOS の UI を作成する

MyCounter が共有できることがわかりましたので iOS で MyCounter を利用してカウンターアプリを作ってみます。iOS では Swift UI を使って MyCounter のインスタンスでインクリメント・デクリメントの制御、現在のカウント値を表示するようにしてみます。

```swift
let counter = MyCounter(min: 0, max: 100)

struct ContentView: View {
    @State var count = counter.value

    var body: some View {

        VStack {
            Text(String(count))

            Button(action: {
                counter.plus()
                count = counter.value
            }) {
                Text("PLUS")
            }

            Button(action: {
                counter.minus()
                count = counter.value
            }) {
                Text("MINUS")
            }
        }
    }
}
```

ビルドして動かしてみるしっかりインクリメントとデクリメントされた値が表示されます。MyCounter で実装した最小値と最大値の制御もうまく動作して問題なくコードが共有できていることがわかります。

[![Image from Gyazo](https://i.gyazo.com/693d2e11c1a14a66c31a6b6081379dba.gif)](https://gyazo.com/693d2e11c1a14a66c31a6b6081379dba)
[![Image from Gyazo](https://i.gyazo.com/ccda115620d73f2122d9881681028b31.gif)](https://gyazo.com/ccda115620d73f2122d9881681028b31)

# Android の UI を作成する

次に Android で MyCounter を利用してカウンターアプリを作ってみます。Android では Jetpack Compose を使って MyCounter のインスタンスでインクリメント・デクリメントの制御、現在のカウント値を表示するようにしてみます。

```kotlin
val counter = MyCounter(min = 0, max = 100)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var count by remember { mutableStateOf(counter.value) }

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.Center)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = count.toString(),
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            counter.plus()
                            count = counter.value
                        }
                    ) {
                        Text(text = "PLUS")
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            counter.minus()
                            count = counter.value
                        }
                    ) {
                        Text(text = "MINUS")
                    }
                }
            }
        }
    }
}

```

ビルドして動かしてみるしっかりインクリメントとデクリメントされた値が表示されます。MyCounter で実装した最小値と最大値の制御もうまく動作して問題なくコードが共有できていることがわかります。

[![Image from Gyazo](https://i.gyazo.com/8674d20e89215c8397b206009c1688e6.gif)](https://gyazo.com/8674d20e89215c8397b206009c1688e6)
[![Image from Gyazo](https://i.gyazo.com/47af737b4a080b867dbf51190509d737.gif)](https://gyazo.com/47af737b4a080b867dbf51190509d737)

# おわりに

今回は KMM を利用して最も簡単なカウンターアプリを iOS と Android 向けに作成してみました。私は Android エンジニアですので Kotlin で共通コードをかけるという体験はかなり良いなと感じましたが iOS エンジニア観点でみたらかなりやりずらいところはあるんじゃないかなと思っています。

例えば Android Studio だと共通コードに簡単に参照ができてブレークポイントも張れてデバッグしやくて良い感じです。 （デバッグ実行するとしっかりブレークポイントで止まってくれます）

[![Image from Gyazo](https://i.gyazo.com/0aa83cc74a266bdcb055029e8251c615.gif)](https://gyazo.com/0aa83cc74a266bdcb055029e8251c615)

ですが XCode だと共通コードの参照ができなかったりします。もちろんブレークポイントも張れないのでデバッグがしづらいのではないかなと思います。（参照する Kotlin ではなくクロスプラットフォーム向けに変換されたなにかがでてきます）

[![Image from Gyazo](https://i.gyazo.com/e570865faa7e31f08c833a76e72fbf08.gif)](https://gyazo.com/e570865faa7e31f08c833a76e72fbf08)

というように KMM でビジネスロジックを共通のコードで実現するというのはかなり良い体験ですが Android エンジニアと iOS エンジニアが快適に開発をすすめる環境を整えるのは結構大変そうだなと感じました。これからは KMM だぜって感じで Android エンジニアが突撃してしまうと iOS エンジニアが疲弊しかねないのでここらへんは慎重に共通化をすすめて行く必要があるのではないかんと思いました。
