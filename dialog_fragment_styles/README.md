# はじめに

DialogFragment の表示方法と Theme の変更方法を解説してみようと思います。本記事ではまずはじめにダイアログの表示方法について解説し、そのあとにダイアログの Theme の変更方法について解説しようと思います。ダイアログの表示方法に詳しい方は Theme の変更方法だけ見ていただいても有益な情報が得られるかと思います。

# DialogFragment を表示する

はじめに DialogFragment を使ったダイアログの作成方法について解説します。

## 1. DialogFragment を継承したクラスを作成する

まず次のように DialogFragment を継承したクラス（NormalDialogFragment）を作成します。

```kotlin
class NormalDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_normal, container,  false)
    }
}
```

## 2. DialogFragment で表示するレイアウトを変更する

上記で作成した DialogFragment では onCreateView で読み込んだ XML ファイルのレイアウトをダイアログに表示するようになっています。なので XML ファイルを編集して DialogFragment に表示するレイアウトを変更します。今回は次のような ConstraintLayout の中に TextView を配置したレイアウトを表示してみます。

![Screen Shot 2020-12-06 at 15.20.50.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/9c5cfacb-87ba-f63a-4021-2cf32ddcaf9a.png)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="400dp"
    android:layout_height="300dp"
    android:background="#50ff0000">

    <TextView
        android:layout_width="300dp"
        android:layout_height="200dp"
        android:background="@color/white"
        android:gravity="center"
        android:text="@string/normal_dialog_fragment"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## 3. DialogFragment を表示する

DialogFragment を生成するには DialogFragment を生成したあとに [show（）](https://developer.android.com/reference/android/app/DialogFragment#show(android.app.FragmentManager,%20java.lang.String))を呼び出すだけで表示できます。


```kotlin
DialogFragment().show(fragmentManager, tag)
```

今回は MainActivity に配置した Button をタップしたときに DialogFragment を表示するようにします。次のようなレイアウトを定義して、MainActivity で Button をタップしたときに DialogFragment を生成して、[show（）](https://developer.android.com/reference/android/app/DialogFragment#show(android.app.FragmentManager,%20java.lang.String)) を呼び出すようにしてやります。

![Screen Shot 2020-12-06 at 16.20.34.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/402e6814-d06b-1d4e-3f7a-502e687d3816.png)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    android:orientation="vertical"
    android:padding="8dp"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/normal_dialog_fragment_button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/normal_dialog_fragment" />
</LinearLayout>
```

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val normalDialogFragmentButton = findViewById<Button>(R.id.normal_dialog_fragment_button)
        normalDialogFragmentButton.setOnClickListener {
            // Activity で show を呼び出す場合には
            // 第1引数の fragmentManager に supportFragmentManager をセットしてやります。
            // 第2引数の tag は fragmentManager からタグ指定で Fragment を取得するためのものです。
            // なので第2引数の tag には任意の文字列をタグとして指定してあげればOKです。
            NormalDialogFragment().show(supportFragmentManager, "TAG")
        }
    }
}
```

## 4. 動作を確認してみる

### DialogFragment の動作を確認する
ここまで実装したらあとはアプリを起動して動作確認してみましょう。アプリを起動してボタンをタップすると次のような感じでダイアログが表示されます。

![Dec-06-2020 16-31-48.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/fc87689e-bc62-bfe3-defa-95de42710445.gif)

このように作成したレイアウトが中心に表示され、レイアウトの領域外は半透明の黒背景となります。
ちなみにこの半透明の黒背景をタップすることでダイアログを閉じれます。
（この表示や動作はカスタマイズできます、デフォルトでは上記の表示・動作するようになっています。）

![Screen Shot 2020-12-06 at 16.54.39.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/ba658433-8f60-21d3-e86f-e4691996bcd6.png)

#### 上手く表示できてそうだがルートビューの layout_width と layout_height が変わる
ダイアログが上手く表示できているかのように見えるのですが、よく見ると作成したレイアウトと異なっています。以下のように赤色背景が表示される想定でしたが、赤色背景が表示される領域が確保されていないため表示できていません。

| XMLファイルに定義したもの                                    | 実際に表示されたもの                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![Screen Shot 2020-12-06 at 15.20.50.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/9c5cfacb-87ba-f63a-4021-2cf32ddcaf9a.png) | ![image.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/d2ac301b-641e-9806-9e9c-0ea2d88f75b1.png) |

なぜこのような仕組みになっているかまではわからないのですが DialogFragment が XML ファイルから View を生成したあとルートビューである ConstraintLayout の layout_width と layout_height に match_parent が設定されるためです。ですので DialogFragment ではルートビューの layout_width や layout_height でダイアログの大きさを調整しようとすると上手くいかないので注意が必要です。

![image.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/5d03c284-63db-5264-370e-9bf91397ef79.png)

# DialogFragment の Theme を変更する

先程はシンプルな DialogFragment を表示してみましたが実際の開発ではもう少し手の混んだレイアウトのDialogFragment を実装するかなと思います。DialogFragment では Theme を変更することで表示や動作を変更できるようになっています。今回はよく使いそうな Theme　をまとめて紹介します。

# 変更方法

Theme は onCreate で setStyle を呼び出すことで変更できます。
今回は以下のクラスとレイアウトを用意し Theme を変更していきます。

```kotlin
class NormalDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_normal, container,  false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(getStyle(), getThemeResId())
    }

    private fun getStyle(): Int = STYLE_NORMAL
    private fun getThemeResId(): Int = R.style.Theme_AppCompat_Dialog
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#50ff0000">

    <TextView
        android:layout_width="300dp"
        android:layout_height="200dp"
        android:background="@color/white"
        android:gravity="center"
        android:text="@string/normal_dialog_fragment"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## ダイアログの背景色を変える

```xml
    <style name="Theme.Sample.BlackDialog" parent="Theme.MaterialComponents.Light.Dialog">
        <item name="android:windowBackground">@android:color/black</item>
    </style>
```

![image.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/78a177e9-7532-fc00-41eb-4c208ca4c3d6.png)

## 角丸のダイアログを表示する

```xml
    <style name="Theme.Sample.RoundedDialog" parent="Theme.MaterialComponents.Light.Dialog">
        <item name="dialogCornerRadius">16dp</item>
    </style>
```

![image.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/c6c2a3c3-633f-c7a1-505f-bb38fd194707.png)

# 領域外の半透明の黒背景を非表示にする

```xml
    <style name="Theme.Sample.BackgroundDimDisabledDialog" parent="Theme.MaterialComponents.Light.Dialog">
        <item name="android:backgroundDimEnabled">false</item>
    </style>
```

![image.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/43c66bad-03b7-18f5-b098-180552618fce.png)

# 外部タッチで消えないダイアログを表示する

```xml
    <style name="Theme.Sample.WindowCloseOnTouchOutsideDisableDialogFragment" parent="Theme.MaterialComponents.Light.Dialog">
        <item name="android:windowCloseOnTouchOutside">false</item>
    </style>
```

![Dec-10-2020 08-15-44.gif](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/260059ce-6c38-d90c-7578-ddf3f4ee7db3.gif)

## 画面全体（フルスクリーン）にダイアログを表示する

```xml
    <style name="Theme.Sample.FullScreenDialog" parent="Theme.MaterialComponents.Light.Dialog">
        <item name="android:windowBackground">@android:color/white</item>
        <item name="android:windowIsFloating">false</item>
    </style>
```

![image.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/567d8bb6-21a6-c030-6906-07bc600b417b.png)

# おわりに

DialogFragment を使う際には本記事で紹介したポイントを押させておけばおおよそのダイアログを作成できると思います。ポイントを押さえれば難しいことはないのでサクッとダイアログを実装できるようになりましょう。今回作成した DialogFragment はこちらです。興味があるかたはこちらのリポジトリを確認してください。

<a href="https://github.com/kaleidot725-android/dialog_fragment_styles"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/dialog_fragment_styles.png" width="460px"></a>