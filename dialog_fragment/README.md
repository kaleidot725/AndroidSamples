# 2020/06/20 ［Android］DialogFragment の表示位置を調整する

DilaogFragment の表示位置を調整するのに苦戦したので、備忘録として表示位置の調整方法についてまとめます。例えば次のような DialogFragment があるとします。

```kotlin
class MainDialogFragment(
    private val gravity: Int,
    private val verticalMargin: Float,
    private val horizontalMargin: Float
): DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_main, container, false)
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:minWidth="250dp"
    android:minHeight="250dp">
    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:text="DIALOG"/>
</androidx.constraintlayout.widget.ConstraintLayout>
```


この DialogFragment の表示位置を調整するには、DialogFragment の Window の attributes の gravity と verticalMargin、horizontalMagin を調整します。

| 名称| 内容 |
| ------- | ------- |
| [gravity](https://developer.android.com/reference/android/view/WindowManager.LayoutParams#gravity) | 画面内のウィンドウの配置 |
| [verticalMargin](https://developer.android.com/reference/android/view/WindowManager.LayoutParams#verticalMargin) | コンテナーの幅をパーセンテージとした、コンテナーとウィジェット間の水平マージン、[Gravity#apply()](https://developer.android.com/reference/android/view/Gravity)のyAdjとしてセットされる値で、gravity が TOP ならば TOP、BOTTOM ならば BOTTOM にマージンが入る。CENTER_VERTICAL ならば TOP か BOTTOM のどっちかにマージンが入る。 |
| [horizontalMargin](https://developer.android.com/reference/android/view/WindowManager.LayoutParams#horizontalMargin) | コンテナーの高さをパーセンテージとした、コンテナーとウィジェット間の垂直マージン、[Gravity#apply()](https://developer.android.com/reference/android/view/Gravity)のxAdjとしてセットされる値で、gravity が LEFT ならば LEFT、RIGHT ならば RIGHT　にマージンが入る。CENTER_VERTICAL だと LEFT か RIGHT にマージンが入る。 |


```kotlin
class MainDialogFragment(
    private val gravity: Int,
    private val verticalMargin: Float,
    private val horizontalMargin: Float
): DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adjustDialogPosition()
    }

    private fun adjustDialogPosition() {
        dialog?.window?.attributes?.also { attributes ->
            attributes.gravity = this.gravity
            attributes.verticalMargin = this.verticalMargin
            attributes.horizontalMargin = this.horizontalMargin
        }
    }
}
```


次のような Layout を用意して、MainDialogFragment の Gravity の設定を変えて表示してみます。verticalMargin や horizontalMargin は本来であれば 0f 〜 1f で設定が必要ですが 0f に設定しています。必要に応じて手元で値を変更して動作確認してみてください。

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        show_left_top_button.setOnClickListener {
            MainDialogFragment(Gravity.LEFT or Gravity.TOP, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_center_top_button.setOnClickListener {
            MainDialogFragment(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_right_top_button.setOnClickListener {
            MainDialogFragment(Gravity.RIGHT or Gravity.TOP, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_left_center_button.setOnClickListener {
            MainDialogFragment(Gravity.LEFT or Gravity.CENTER, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_center_button.setOnClickListener {
            MainDialogFragment(Gravity.CENTER, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_right_center_button.setOnClickListener {
            MainDialogFragment(Gravity.RIGHT or Gravity.CENTER, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_left_bottom_button.setOnClickListener {
            MainDialogFragment(Gravity.LEFT or Gravity.BOTTOM, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_center_bottom_button.setOnClickListener {
            MainDialogFragment(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM, 0f, 0f).show(supportFragmentManager,"TAG")
        }

        show_right_bottom_button.setOnClickListener {
            MainDialogFragment(Gravity.RIGHT or Gravity.BOTTOM, 0f, 0f).show(supportFragmentManager,"TAG")
        }
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

    <Button
        android:id="@+id/show_left_top_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="LEFT TOP"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/show_left_center_button"
        app:layout_constraintEnd_toStartOf="@id/show_center_top_button"/>

    <Button
        android:id="@+id/show_center_top_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="CENTER TOP"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toEndOf="@id/show_left_top_button"
        app:layout_constraintBottom_toTopOf="@id/show_center_button"
        app:layout_constraintEnd_toStartOf="@id/show_right_top_button"/>

    <Button
        android:id="@+id/show_right_top_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="RIGHT TOP"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/show_right_center_button"
        app:layout_constraintStart_toEndOf="@id/show_center_top_button"
        app:layout_constraintEnd_toEndOf="parent"/>

    <Button
        android:id="@+id/show_left_center_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="LEFT CENTER"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/show_left_top_button"
        app:layout_constraintEnd_toStartOf="@id/show_center_button"
        app:layout_constraintBottom_toTopOf="@id/show_left_bottom_button"/>

    <Button
        android:id="@+id/show_center_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="CENTER"
        app:layout_constraintStart_toEndOf="@id/show_left_center_button"
        app:layout_constraintTop_toBottomOf="@id/show_center_top_button"
        app:layout_constraintEnd_toStartOf="@id/show_right_center_button"
        app:layout_constraintBottom_toTopOf="@id/show_center_bottom_button"/>

    <Button
        android:id="@+id/show_right_center_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="RIGHT CENTER"
        app:layout_constraintStart_toEndOf="@id/show_center_button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/show_right_top_button"
        app:layout_constraintBottom_toTopOf="@id/show_right_bottom_button"
        />

    <Button
        android:id="@+id/show_left_bottom_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="LEFT BOTTOM"
        app:layout_constraintTop_toBottomOf="@id/show_left_center_button"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@id/show_center_bottom_button" />

    <Button
        android:id="@+id/show_center_bottom_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="CENTER BOTTOM"
        app:layout_constraintStart_toEndOf="@id/show_left_bottom_button"
        app:layout_constraintTop_toBottomOf="@id/show_center_button"
        app:layout_constraintEnd_toStartOf="@id/show_right_bottom_button"
        app:layout_constraintBottom_toBottomOf="parent"/>


    <Button
        android:id="@+id/show_right_bottom_button"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:text="RIGHT BOTTOM"
        app:layout_constraintTop_toBottomOf="@id/show_right_center_button"
        app:layout_constraintStart_toEndOf="@id/show_center_button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"/>
</androidx.constraintlayout.widget.ConstraintLayout>
```

アプリを起動して実装したボタンを押すと、次のような感じでそれぞれのボタンに応じた位置にダイアログが表示できます。

[![Image from Gyazo](https://i.gyazo.com/bef109280afc6df40790280be92dc0cb.gif)](https://gyazo.com/bef109280afc6df40790280be92dc0cb)