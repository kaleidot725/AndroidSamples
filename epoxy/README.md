# 2020/04/25 ［Android］Epoxy のサンプルと解説 (Databinding から作成)

# はじめに

RecyclerView を使う際には Adapter などに独自で処理を実装する必要があり、ここが RecyelerView を利用する上でかなり面倒な実装だったりします。 Epoxy はこの面倒な処理を実装してくれるライブラリらしいです。Epoxy のサンプルを作成してどんなことができるのかざっくりと解説していきたいと思います。

# 準備する

Epoxy を利用できるようにセットアップを進めます。ライブラリとして epoxy、 epoxy-databinding, epoxy-processor を追加します。databinding を利用する際には epoxy-databinding が必要になります、databinding を利用しないのであれば追加する必要はないです。ライブラリを追加したら kapt と databinding を有効化するだけです。これでセットアップは完了です。

```groovy
// kapt 有効化
apply plugin: 'kotlin-kapt' 

android {
      ︙
    // databinding 有効化
    dataBinding {
        enabled = true 
    }
      ︙
}

dependencies {
      ︙
    def epoxy_version = "3.9.0"
    implementation "com.airbnb.android:epoxy:$epoxy_version"
    implementation "com.airbnb.android:epoxy-databinding:${epoxy_version}"
    kapt "com.airbnb.android:epoxy-processor:$epoxy_version"
      ︙
}
```

# 実装する

Epoxy を利用するには EpoxyModel とEpoxyController を作成する必要があります。これらの EpoxyModel と EpoxyController ですが次のような役割になっています。

| 名称| 説明 |
| ------- | ------- |
| EpoxyModel  | Epoxyが生成するクラスで RecylerView に表示する View を定義する。また Epoxy から View を操作するためのインタフェースを定義する。EpoxyModel は開発者が作成した CustomView や Layout ファイルから生成される。 |
| EpoxyController | RecyclerView に表示する EpoxyModel を生成し、EpoxyModel に定義されたインタフェースを使って、View の操作を行う。 |

## EpoxyModel を作成する

EpoxyModel を作成する方法は 3種類あるみたいです。CustomViewから作成する方法、Databinding から作成する方法、 ViewHolder から作成する方法とあります。今回は Databinding から作成するのを試してみたいと思います。

[![Image from Gyazo](https://i.gyazo.com/70e0179f24c4c212d917126f72f09d9a.png)](https://gyazo.com/70e0179f24c4c212d917126f72f09d9a)

Databinding から生成する場合には、まずは普通に Databinding 同様に Layout で囲った Layout ファイル を作成します。今回は TextView と Button を持った Layout を用意してみたいと思います。

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <import type="android.view.View" />

        <variable
            name="title"
            type="String" />

        <variable
            name="onClickListener"
            type="View.OnClickListener" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="5dp">

        <TextView
            android:id="@+id/title_text_view"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="@{title}"
            android:textSize="32sp"
            app:layout_constraintBottom_toBottomOf="@id/action_button"
            app:layout_constraintEnd_toStartOf="@id/action_button"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="@id/action_button"
            tools:text="XXXXXXXXXXXXXXX" />

        <Button
            android:id="@+id/action_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="ACTION"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            app:setOnClickListener="@{onClickListener}" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</layout>
```

そしてどこでもよいので package-info.java を作成してやります。そしてこのファイルに@EpoxyDataBindingLayouts({Layoutファイル名}) を追加してると、Layout ファイルから EpoxyModel を生成してくれるようになります。

```java
@EpoxyDataBindingLayouts({R.layout.header_data_binding_layout})
package jp.kaleidot725.sample;

import com.airbnb.epoxy.EpoxyDataBindingLayouts;
import jp.kaleidot725.sample.R;
```

Build ➔ Make Project からビルドしてみます。すると java ➔ generated ➔ project package ➔に HeaderDataBindingLayoutBindingModel_ が生成されます。この生成された HeaderDataBindingLayoutBindingModel_ が EpoxyModel になります。EpoxyController では この生成された HeaderDataBindingLayoutBindingModel_ を利用して View の生成や操作を実装していきます。

```java
public class HeaderDataBindingLayoutBindingModel_ extends DataBindingEpoxyModel implements GeneratedModel<DataBindingEpoxyModel.DataBindingHolder>, HeaderDataBindingLayoutBindingModelBuilder {
}
```

## EpoxyController を作成する

EpoxyModel の実装が完了したので、次は EpoxyController を作成していきます。EpoxyController の作成は簡単で Typed2EpoxyController を継承したクラスを作成してやります。あとは buildModels を override し EpoxyModel を使って View の生成をしてやるだけです。

```kotlin
class HeaderDatabindingViewController(
    private val selectListener: SelectListener
) : Typed2EpoxyController<List<String>, Boolean>() {

    override fun buildModels(names: List<String>, loadingMore: Boolean) {
        // 名前リストの数だけ、 EpoxyModel を作成してやる。
        names.forEach { item ->
            // Layout ファイルに記述した title や onClickListener の variable 定義を通じて　View の設定を変更する。
            headerDataBindingLayout {
                id("Content")
                title(item)
                onClickListener(View.OnClickListener { selectListener.onSelected(item) })
            }
        }
    }

    interface SelectListener {
        fun onSelected(item: String)
    }
}
```

# 動作確認する

まずは EpoxyController を生成していきます。EpoxyController を生成すると RecyclerView に設定する Adapter が用意されます。なので RecyclerView にその adapter をセットしてやります。あとは EpoxyControler の setData にてデータをセットしてやるだけです。これで setData にてセットしたデータが RecyclerView に表示されます。

```kotlin
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

        headerDatabindingViewController.setData(itemList, false)
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout
```

起動してみます、はいセットしたデータが RecyclerView に問題なく表示されています。

[![Image from Gyazo](https://i.gyazo.com/69cffcae65524e338d1a4565b9517fed.png)](https://gyazo.com/69cffcae65524e338d1a4565b9517fed)

# おわりに

ざっくり Epoxy をまとめると次のようになりますね。

- Epoxy を利用して RecyclerView にデータを表示するには EpoxyModel と EpoxyController が必要
- EpoxyModel には表示する View と View の操作を定義する。 EpoxyModel は CustomView や Databinding の定義から自動生成できる。
- EpoxyController は自分で作成する。EpoxyController には与えられたデータから EpoxyModel をどのように生成するか定義する。

Epoxy を使うと今まで大変だった ReyclerViewAdapter の作成作業が減るのがよいなと思いました。Adapter を作るのはわりと面倒だったので Epoxy など積極的に使って効率的に機能を実装しましょう。


<a href="https://github.com/kaleidot725-android/epoxy"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/epoxy.png" width="460px"></a>

# 参考文献

- [Epoxy | GitHub](https://github.com/airbnb/epoxy)

# 2020/04/25 ［Android］Epoxy のサンプルと解説 (CustomView から作成)

# はじめに

RecyclerView を使う際には Adapter などに独自で処理を実装する必要があり、ここが RecyelerView を利用する上でかなり面倒な実装だったりします。 Epoxy はこの面倒な処理を実装してくれるライブラリらしいです。Epoxy のサンプルを作成してどんなことができるのかざっくりと解説していきたいと思います。

# 準備する

Epoxy を利用できるようにセットアップを進めます。ライブラリとして epoxy、 epoxy-databinding, epoxy-processor を追加します。databinding を利用する際には epoxy-databinding が必要になります、databinding を利用しないのであれば追加する必要はないです。ライブラリを追加したら kapt と databinding を有効化するだけです。これでセットアップは完了です。

```groovy
// kapt 有効化
apply plugin: 'kotlin-kapt' 

android {
      ︙
    // databinding 有効化
    dataBinding {
        enabled = true 
    }
      ︙
}

dependencies {
      ︙
    def epoxy_version = "3.9.0"
    implementation "com.airbnb.android:epoxy:$epoxy_version"
    implementation "com.airbnb.android:epoxy-databinding:${epoxy_version}"
    kapt "com.airbnb.android:epoxy-processor:$epoxy_version"
      ︙
}
```

# 実装する

Epoxy を利用するには EpoxyModel とEpoxyController を作成する必要があります。
これら EpoxyModel と EpoxyController ですが次のような役割を担当します。

| 名称| 説明 |
| ------- | ------- |
| EpoxyModel  | RecylerView に表示する View を定義する。また Epoxy から View を操作するためのインタフェースを定義する。 |
| EpoxyController | RecyclerView に表示する EpoxyModel を生成し、EpoxyModelに定義されたインタフェースを使って、View の操作を行う。 |

## EpoxyModel を作成する

EpoxyModel を作成する方法は 3種類あるみたいです。CustomViewから作成する方法、Databinding から作成する方法、 ViewHolder から作成する方法とあります。今回は CustomView から作成するのを試してみたいと思います。

[![Image from Gyazo](https://i.gyazo.com/70e0179f24c4c212d917126f72f09d9a.png)](https://gyazo.com/70e0179f24c4c212d917126f72f09d9a)

CustomView から生成する場合には、まずは普通に CustomView を作成します。今回は TextView と Button を持った CustomView を用意しています。

```kotlin
class HeaderCustomView : LinearLayout {
    private lateinit var titleView: TextView
    private lateinit var buttonView: Button

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        titleView = TextView(context, attrs).apply {
            this.width = 500
            this.textSize = 32f
        }

        buttonView = Button(context, attrs).apply {
            buttonView.text = "ACTION"
        }

        this.addView(titleView)
        this.addView(buttonView)
    }
}
```

CustomView が出来たら EpoxyModel を作成に必要な記述を追加していきます。CustomView に@ModelView をつけます、するとこの CustomView から EpoxyModel が生成されるようになります。

```kotlin
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderCustomView : LinearLayout {

}
```

Build ➔ Make Project からビルドしてみましょう。すると java ➔ generated ➔ project package ➔ view に HeaderCustomViewModel_ が生成されます。この生成された HeaderCustomViewModel_ が EpoxyModel になります。EpoxyController では この生成された HeaderCustomViewModel_ を利用して View の生成や操作を実装していきます。

```kotlin
public class HeaderCustomViewModel_ extends EpoxyModel<HeaderCustomView> implements GeneratedModel<HeaderCustomView>, HeaderCustomViewModelBuilder {
        ︙
}
```

EpoxyModel の生成は完了しましたが、この EpoxyModel には View を操作するためのメソッドが定義されていないので、 TextView の Text や Button の onClick を変更できない状態になっています。このままでは固定値で生成された View を表示するだけになってしまうので、 View の内容を変更できるようにメソッドを追加していきます。

Text を変更するならば @TextProp をつけたメソッド、Event を受け取る Listener を変更するならば @CallbackProp をつけたメソッドを用意してあげます。そうすると EpoxyModel に View を操作するメソッドが自動生成され、あとから Text や onClick を変更できるようになります。（アノテーションの具体的な説明は[こっち](https://github.com/airbnb/epoxy/wiki/Generating-Models-from-View-Annotations)に説明がありますので確認してみてください。）

```kotlin
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HeaderCustomView : LinearLayout {
        ︙
    // Textを変更するならば @TextProp をつける
    @TextProp
    fun setTitle(text: CharSequence?) {
        titleView.text = text
    }

    // View.OnClickListener を変更するならば @CallbackProp をつける
    @CallbackProp
    fun onClickListener(listener: View.OnClickListener?) {
        buttonView.setOnClickListener(listener)
    }
        ︙
}
```

## EpoxyController を作成する

EpoxyModel の実装が完了したので、次は EpoxyController を作成していきます。EpoxyController の作成は簡単で Typed2EpoxyController を継承したクラスを作成してやります。あとは buildModels を override し EpoxyModel を使って View の生成をしてやるだけです。

```kotlin
class HeaderCustomViewController(
    private val selectListener: SelectListener
) : Typed2EpoxyController<List<String>, Boolean>() {

    override fun buildModels(names: List<String>, loadingMore: Boolean) {
        names.forEach { item ->
            headerCustomView{
                id("Content")
                title(item)
                onClickListener(View.OnClickListener { selectListener.onSelected(item) })
            }
        }
    }

    interface SelectListener {
        fun onSelected(item: String)
    }
}
```

# 動作確認する


まずは EpoxyController を生成していきます。EpoxyController を生成すると RecyclerView に設定する Adapter が用意されます。なので RecyclerView にその adapter をセットしてやります。あとは EpoxyControler の setData にてデータをセットしてやるだけです。これで setData にてセットしたデータが RecyclerView に表示されます。

```kotlin
class MainActivity : AppCompatActivity() {
    private val itemList = listOf(
        "ONE", "TWO", "THREE", "FOUR", "FIVE",
        "SIX", "SEVEN", "EIGHT", "NINE", "TEN"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val headerCustomViewController = HeaderCustomViewController(object :
            HeaderCustomViewController.SelectListener {
            override fun onSelected(item: String) {
                Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
            }
        })

        recycler_view.apply {
            this.adapter = headerCustomViewController.adapter
            this.layoutManager = LinearLayoutManager(applicationContext).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }

        headerCustomViewController.setData(itemList, true)
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="0dp"
        android:layout_height="match_parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout
```

起動してみます、はいセットしたデータが RecyclerView に問題なく表示されています。

[![Image from Gyazo](https://i.gyazo.com/69cffcae65524e338d1a4565b9517fed.png)](https://gyazo.com/69cffcae65524e338d1a4565b9517fed)

これで起動してみると、セットしたデータが RecyclerView に表示されるようになります。


# おわりに

CustomView から EpoxyModel を作成するパターンでは次の実装が必要

- CustomView から EpoxyModel を作成するには @ModelView アノテーションをつける
- CustomView から EpoxyModel を作成するときは @TextProp @CallbackProp をつけたメソッドを用意して EpoxyModel を生成した際したあとに View の Text や Callback リスナーなどを登録できるようにする必要がある。
- EpoxyController には与えられたデータから EpoxyModel をどのように生成するか定義する。

<a href="https://github.com/kaleidot725-android/epoxy"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/epoxy.png" width="460px"></a>

# 参考文献

- [Epoxy | GitHub](https://github.com/airbnb/epoxy)



# 2020/07/12 ［Android］Epoxy が StickyHeader に対応しているらしいので試してみた

# はじめに

RecyclerView の Adapter の実装の部分を楽にしてくれるライブラリの Epoxy ですが、
どうやら Sticky Header にも対応してくれているらしいです。今回は Epoxy で Sticky Header をどのような感じで利用できるのか紹介したいと思います。

# 準備する

Sticky Header が利用できるようになったのは [3.10.0](https://github.com/airbnb/epoxy/releases/tag/3.10.0) からみたいです。なので次の内容を build.gradle に記述して、3.10.0 以上の Epoxy を使えるようにします。

[![Image from Gyazo](https://i.gyazo.com/c4e14fdb146c8dd17e8fbc3f057553a5.png)](https://gyazo.com/c4e14fdb146c8dd17e8fbc3f057553a5)

```groovy
// kapt 有効化
apply plugin: 'kotlin-kapt' 

android {
      ︙
    // databinding 有効化
    dataBinding {
        enabled = true 
    }
      ︙
}

dependencies {
      ︙
    def epoxy_version = "3.11.0"
    implementation "com.airbnb.android:epoxy:$epoxy_version"
    implementation "com.airbnb.android:epoxy-databinding:${epoxy_version}"
    kapt "com.airbnb.android:epoxy-processor:$epoxy_version"
      ︙
}
```

# 実装する


## EpoxyModel を作成する

まずは EpoxyModel を作成していきます。今回は StickyHeader になる HeaderLayout と StickyHeader にならない ContentLayout と分けて作成していきます。

**HeaderLayout**

[![Image from Gyazo](https://i.gyazo.com/e52e0a0f6b50fa199f06179c4973cc79.png)](https://gyazo.com/e52e0a0f6b50fa199f06179c4973cc79)

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="title"
            type="String" />
    </data>

    <androidx.appcompat.widget.LinearLayoutCompat
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{title}"
            android:textColor="@android:color/white"
            android:textSize="32sp"
            android:background="@color/colorPrimary"
            tools:text="Sticky Header" />

    </androidx.appcompat.widget.LinearLayoutCompat>
</layout>
```

**ContentLayout**

[![Image from Gyazo](https://i.gyazo.com/24bf8d67818ab791dcfed8a459fc3e04.png)](https://gyazo.com/24bf8d67818ab791dcfed8a459fc3e04)

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="title"
            type="String" />
    </data>

    <androidx.appcompat.widget.LinearLayoutCompat
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="@{title}"
            tools:text="Content" />

    </androidx.appcompat.widget.LinearLayoutCompat>
</layout>
```

**package-info.java**
そしてこのままだと Epoxy は　EpoxyModel を生成してくれません。ですので package-info.java を作成して Epoxy が EpoxyModel を生成してくれるようにしてやります。

```java
@EpoxyDataBindingLayouts({R.layout.content_layout, R.layout.header_layout})
package jp.kaleidot725.sample;
import com.airbnb.epoxy.EpoxyDataBindingLayouts;
```

## EpoxyController を作成する

次に EpoxyController を作成してやります、今回は単純に先程作成した HeaderLayout と ContentLayout を交互に表示する StickyHeaderController というクラスを作成してやります。

```kotlin
data class Content(val uuid: String, val value: String)
data class Header(val uuid: String,val value: String)

class StickyHeaderController : Typed2EpoxyController<List<Header>, List<Content>>() {
    override fun buildModels(headers: List<Header>, contents: List<Content>) {
        headers.forEach { header ->
            headerLayout {
                id(header.uuid)
                title(header.value)
            }

            contents.forEach { content ->
                contentLayout {
                    id(content.uuid)
                    title(content.value)
                }
            }
        }
    }
}
```

## EpoxyRecyclerView をセットアップする

あとは EpoxyRecyclerView を定義してセットアップしてやります。

**MainActivity**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.airbnb.epoxy.EpoxyRecyclerView
        android:id="@+id/epoxy_recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stickyHeaderController = StickyHeaderController()
        epoxy_recycler_view.adapter = stickyHeaderController.adapter
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
```

ここまで実装したものだと通常の Epoxy と同じで StickyHeader の動作になりません。 StickyHeader として使うには次の実装を追加してやる必要があります。

<a href="https://gyazo.com/8a4a9f2a5d50779936690c6f4645cebe"><img src="https://i.gyazo.com/8a4a9f2a5d50779936690c6f4645cebe.gif" alt="Image from Gyazo" width="256"/></a>


## StickyHeader として動作するようにする

StickyHeader として動作させるには次の 2つの実装を追加してやります。

1. EpoxyRecyclerView の layoutManager として StickyHeaderLinearLayoutManager をセットしてやる
2. EpoxyRecyclerView の adpter としてセットされる EpoxyController の isStickyHeader を override してやる

**EpoxyRecyclerView の layoutManager として StickyHeaderLinearLayoutManager をセットしてやる**

StickyHeader を利用するにはまず RecyclerView の layoutManager に StickyHeaderLinearLayoutManager をセットしてやる必要があります。この StickyHeaderLinearLayoutManager が StickyHeader である EpoxyModel を見つけてくれるようになっていて、RecyclerView のスクロール状況に応じて StickyHeader を貼り付けてくれます。

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stickyHeaderController = StickyHeaderController()
        epoxy_recycler_view.adapter = stickyHeaderController.adapter
        epoxy_recycler_view.layoutManager = StickyHeaderLinearLayoutManager(applicationContext)
        stickyHeaderController.setData(createHeaders(10), createContents(100))
    }
}
```

**EpoxyRecyclerView の adpter としてセットされる EpoxyController の isStickyHeader を override してやる**

StickyHeaderLinearLayoutManager では EpoxyController の isStickyHeader を利用して StickyHeader を識別するような仕組みになっています。ですので isStickyHeader を override して自身が定義した EpoxyModel を StickyHeader として認識されるようにします。（isStickyHeader の引数として Position が渡されます、なので Position にある EpoxyModel のクラスを取得して、StickyHeaderとして扱いたいクラスであるか判定してやります。）

```kotlin
class StickyHeaderController : Typed2EpoxyController<List<Header>, List<Content>>() {
        ︙
    override fun isStickyHeader(position: Int): Boolean {
        return adapter.getModelAtPosition(position)::class == HeaderLayoutBindingModel_::class
    }
}
```

この実装を加えると EpoxyModel が StickyHeader として扱われ RecyclerView に StickyHeader が表示されるようになります。

<a href="https://gyazo.com/396045ac8a1da098c95ff5fe244b7109"><img src="https://i.gyazo.com/396045ac8a1da098c95ff5fe244b7109.gif" alt="Image from Gyazo" width="256"/></a>

# おわりに

という感じで Epoxy でも StickyHeader が使えるようになっているらしいです。使い方をまとめると次のようになりますね。

- 通常の利用方法と同じで EpoxyModel と EpoxyController を実装してやる
- RecyclerView の adapter に EpoxyController.Adapter、layoutManager に StickyHeaderLinearLayoutManager をセットしてやる
- StickyHeaderLinearLayoutManager が StickyHeader である EpoxyModel を識別できるように EpoxyController.isStickyHeader を実装してやる。

<a href="https://github.com/kaleidot725-android/epoxy"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/epoxy.png" width="460px"></a>

# 参考文献

StickyHeader は現時点（2020/07/12）でまだドキュメントが整備されていないので、詳しく知りたい方は次のリンクを見てみると良いかなと思います。

- [Epoxy | GitHub](https://github.com/airbnb/epoxy)
- [Epoxy StickyHeader のサンプル](https://github.com/airbnb/epoxy/tree/master/kotlinsample/src/main/java/com/airbnb/epoxy/kotlinsample)
- [Epoxy StickyHeader をサポートした時のプルリクエスト](https://github.com/airbnb/epoxy/pull/842)