# Koin と Navigation Composeを組み合わせたときに画面のライフサイクルに合わせて ViewModel  を生成する方法

# **はじめに**

**Koin と Navigatio Compose を組み合わせたときに画面のライフサイクルに従って ViewModel を生成する方法についてまとめます。本記事では以下のバージョンのライブラリを利用して動作確認しています。**

```groovy
dependencies {
			︙
    implementation 'androidx.core:core-ktx:1.7.0'
    implementation "androidx.compose.ui:ui:1.0.5"
    implementation "androidx.compose.material:material:1.0.5"
    implementation "androidx.compose.ui:ui-tooling-preview:1.0.5"
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.4.0'
    implementation 'androidx.activity:activity-compose:1.4.0'
    implementation "io.insert-koin:koin-android:3.1.5"
    implementation "androidx.navigation:navigation-compose:2.4.0-rc01"
			︙
}
```

# **実装方法**

**画面のライフサイクルに従ってViewModel を生成できるようにするため以下の拡張関数を定義する。**

```kotlin
@Composable
fun getComposeViewModelOwner(): ViewModelOwner {
    return ViewModelOwner.from(
        LocalViewModelStoreOwner.current!!,
        LocalSavedStateRegistryOwner.current
    )
}

@Composable
inline fun <reified T : ViewModel> getNavComposeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val viewModelOwner = getComposeViewModelOwner()
    return getKoin().getViewModel(qualifier, { viewModelOwner }, parameters)
}
```

**あとはNavHost の composable で定義した拡張関数を呼び出して ViewModel を生成するようにする。**

```kotlin
@Composable
fun getComposeViewModelOwner(): ViewModelOwner {
    return ViewModelOwner.from(
        LocalViewModelStoreOwner.current!!,
        LocalSavedStateRegistryOwner.current
    )
}

@Composable
inline fun <reified T : ViewModel> getNavComposeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val viewModelOwner = getComposeViewModelOwner()
    return KoinJavaComponent.getKoin().getViewModel(qualifier, { viewModelOwner }, parameters)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinNavigationComposeTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            viewModel = getNavComposeViewModel(),
                            onNext = {
                                navController.navigate("sub")
                            }
                        )
                    }
                    composable("sub") {
                        SubScreen(
                            viewModel = getNavComposeViewModel(),
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}

val appModule = module {
    viewModel {
        MainViewModel()
    }

    viewModel {
        SubViewModel()
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, onNext: () -> Unit) {
    val count by viewModel.count.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.wrapContentSize(align = Alignment.Center)) {
            Text("TITLE: ${viewModel.title}")
            Text("CREATED AT: ${viewModel.createdAt}")
            Text("COUNT: $count")
            Button(onClick = { viewModel.increment() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "INCREMENT")
            }
            Button(onClick = { viewModel.decrement() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "DECREMENT")
            }
            Button(onClick = { onNext() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "Next Screen")
            }
        }
    }
}

class MainViewModel : ViewModel() {
    val title = "HOME"
    val createdAt = Date().time

    private val _count: MutableStateFlow<Int> = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun increment() {
        _count.value = _count.value + 1
    }

    fun decrement() {
        _count.value = _count.value - 1
    }
}

@Composable
fun SubScreen(viewModel: SubViewModel, onBack: () -> Unit) {
    val count by viewModel.count.collectAsState()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.wrapContentSize(align = Alignment.Center)) {
            Text("TITLE: ${viewModel.title}")
            Text("CREATED AT: ${viewModel.createdAt}")
            Text("COUNT: $count")
            Button(onClick = { viewModel.increment() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "INCREMENT")
            }
            Button(onClick = { viewModel.decrement() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "DECREMENT")
            }
            Button(onClick = { onBack() }, modifier = Modifier.padding(8.dp)) {
                Text(text = "Back Screen")
            }
        }
    }
}

class SubViewModel : ViewModel() {
    val title = "SUB"
    val createdAt = Date().time

    private val _count: MutableStateFlow<Int> = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    fun increment() {
        _count.value = _count.value + 1
    }

    fun decrement() {
        _count.value = _count.value - 1
    }
}
```

# **動作**

**このような実装をすると画面のライフサイクルにあわせて ViewModel が生成されるようになる。**

- **メイン画面→サブ画面に遷移したら新しいViewModel が生成される**
- **サブ画面→メイン画面→サブ画面と遷移しても新しい ViewModel が生成される**
- **メイン画面はバックスタックから消えていないので最初に生成された ViewModel が使われ続ける**

![Videotogif.gif](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/941B43A3-5C84-40CA-8471-AB53CDD2736A/2B4B70A2-A3D1-43BA-8EC5-4D75D96B8142_2/alO1ZQHZzRXo2UbAlvfscH9tfUCJdV1dmbjYHdL5yowz/Videotogif.gif)

# **おわりに**

**今回の記事では拡張関数の詳細については触れていません。拡張機能で何をやっているのかは以下の記事を読み進めていくとわかると思います。詳細を知りたい方は以下の記事を熟読してみてください。**

- [navigation-compose は ViewModel のライフサイクルをどう管理しているのか](https://y-anz-m.blogspot.com/2021/08/navigation-compose-viewmodel.html)
- [Koin | ViewModel for @Composable](https://insert-koin.io/docs/reference/koin-android/compose/#viewmodel-for-composable)
- [Koin 3.0.1 + Jetpack Compose Navigation - getViewModel() create new ViewModel instance instead of return existing one](https://github.com/InsertKoinIO/koin/issues/1079)
- [今回作成したサンプル](https://github.com/kaleidot725-android/koin-navigation-compose)

