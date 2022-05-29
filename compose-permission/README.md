## Jetpack Compose で Permission を要求する方法

Jetpack Compose で Permission を要求するには [rememberLauncherForActivityResult](https://developer.android.com/reference/kotlin/androidx/activity/compose/package-summary#rememberlauncherforactivityresult) を利用するか、

または [Accompanist](https://google.github.io/accompanist/) の [Permissions](https://google.github.io/accompanist/permissions/#jetpack-compose-permissions) を利用する方法の2通りがある。

## セットアップ

本記事では以下の環境で動作確認をしています。

- Kotlin  v1.5.21
- Jetpack Compose v1.0.1
- Android Studio Bublebee | 2021.1.1 Canary 7

また Accompanist の Permission を利用するので以下の Dependencies を追加しています。

```other
dependencies {
		︙
    implementation "com.google.accompanist:accompanist-permissions:0.16.1"
		︙
}
```

## 特定の画面が表示されたら Permission を要求する

特定の画面が表示されたら Permission を要求する場合は rememberLauncherForActivityResult を利用する。

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RequestPermission()
        }
    }
}

@Composable
private fun RequestPermission() {
    // LocalComposition より提供される Context を取得する
    val context = LocalContext.current

    // LocalComposition より提供される Lifecycle を取得する
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // Permission Request を実行する Permission を定義する
    val permission = Manifest.permission.READ_EXTERNAL_STORAGE

    // Permission Request を実行するための Launcher を作成する
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> Log.v("TEST", "PERMISSION REQUEST RESULT ${isGranted}") }
    )

    // もし Permission が許可されていなければ、 Activity が onStart に遷移したとき、
    // Launcher を利用して Permission Request を実行する LifecycleObserver を作成する。
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                if (!permission.isGrantedPermission(context)) {
                    launcher.launch(permission)
                }
            }
        }
    }

    // lifecycle または lifecycleObserver が変化した、また破棄されたら呼び出される
    DisposableEffect(lifecycle, lifecycleObserver) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

private fun String.isGrantedPermission(context: Context): Boolean {
    // checkSelfPermission は PERMISSION_GRANTED or PERMISSION_DENIED のどちらかを返す
    // そのため checkSelfPermission の戻り値が PERMISSION_GRANTED であれば許可済みになる。
    return context.checkSelfPermission(this) == PackageManager.PERMISSION_GRANTED
}
```

起動する以下のように MainActivity が表示されたら Permission 要求が表示される

![demo1-1.gif](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/40EA9784-8761-428A-9595-90B07391B2BA/667B8170-9A98-4C17-97A8-FBD8B0337340_2/demo1-1.gif)

## 特定のボタンを押したら Permission を要求する

特定のボタンを押したら Permission を要求する場合でも rememberLauncherForActivityResult を利用して実装もできる。

だが Accompanist の Permissions を利用したほうが以下のように楽に実装ができる。(ですが v0.16.1 だと Experimental です)

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RequestPermissionUsingAccompanist()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun RequestPermissionUsingAccompanist() {
    // Permission Request を実行する Permission を定義する
    val permission = Manifest.permission.READ_EXTERNAL_STORAGE

    // Permission Request の実行を制御する State クラス
    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotAvailableContent = {
            // Permission を拒否し、表示しないを押したときの View
            Text("Permission Denied.")
        }, permissionNotGrantedContent = {
            // Permission の許可を促すときの View
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Request permission.")
            }
        }, content = {
            // Permission が許可されたときの View
            Text("Permission Granted.")
        }
    )
}
```

起動すると permissionNotGrantedContent の Button が表示される、Button を押すと Permission 要求が表示される。

Permission 要求にて Allow を選択すると Permission が許可されたので content の View が表示される。

![demo-2.gif](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/40EA9784-8761-428A-9595-90B07391B2BA/441064DA-ACF2-4351-84EF-3805752AC1CA_2/demo-2.gif)

もし Permissiyon 要求にて Deny & dont’t ask again を押したときには permissionNotAvailableContent の View が表示される。

![demo-3.gif](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/40EA9784-8761-428A-9595-90B07391B2BA/8EC03919-FF34-47D1-8C29-F69501164DAD_2/demo-3.gif)

## 参考文献

[Y.A.M の 雑記帳: Jetpack Compose で Runtime Permission をリクエストする](http://y-anz-m.blogspot.com/2021/06/jetpack-compose-runtime-permission.html)

[Guide - Accompanist](https://google.github.io/accompanist/permissions/)