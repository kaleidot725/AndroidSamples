# Dagger Hilt

## 何をしてくれるのか？

- 今までの Dagger だと Android アプリケーション用に 独自の Component  を設計する必要があった。

- この設計がわりと面倒で Application ・ Activity ・ Fragment に DI できるようにするだけでも大変だった。

  （特に ViewModel に SavedStateHandle を渡すのはかなり大変だった毎回どうやるか調べては忘れる。。。）

- Dagger Hilt では Component  の標準セットを定義してくれて、かつ簡単に DI できるような仕組みを用意してくれている。（ViewModel に SavedStateHandle を渡すのもの簡単にできるようになっている。）

# 用語

Dagger Hilt の説明に入る前に Dagger でよく使われる用語についてまとめる。（[Master of Dagger](https://booth.pm/ja/items/1577764) から引用)

| 用語               | 説明                                                         |
| ------------------ | ------------------------------------------------------------ |
| binding            | 型とその方のインスタンスを取得する方法の組のこと             |
| dependency         | binding が必要する他の型のインスタンスのこと                 |
| オブジェクトグラフ | binding の集合、簡単にいうと、Key が型で、Value がその型のインスタンスを返す Factory である Map のようなもの |
| Module             | binding 定義をグループ化し、再利用しやすくするためのもの     |
| Component          | オブジェクトグラフを使ってある型のインスタンスを取得する役割のもの |
| inject             | 指定された binding 型の引数やメンバー変数にオブジェクトグラフから取得したインスタンスを代入する |
| @Module            | Module を定義するには @Module アノテーションをつけてやる必要がある |
| @Provides          | @Provides アノテーションをつけたメソッドを定義すると、そのメソッドの戻り値とそのメソッドの戻り値の型が組である binding を定義できる。 |
| @Binds             | @Binds アノテーションをつけたメソッドを定義すると、そのメソッドの引数とそのメソッドの戻り値の型が組である binding が定義できる。 |

# 実装

## セットアップ

- Dagger Hilt を使うためには、hilt-android-gradle-plugin と hilt-android と hilt-android-compiler を依存関係に追加する必要がある。
- Dagger Hilt を使うときに ViewModel を使いたいときには hilt-lifecycler-viewmodel と hilt-compiler を依存関係に追加する必要がある。

**build.gradle(.)**

```groovy
buildscript {
  			︙
    dependencies {
      		︙
        def dagger_hilt_version = "2.28-alpha"
        classpath "com.google.dagger:hilt-android-gradle-plugin:${dagger_hilt_version}"
    			︙
    }
}
```

**build.gralde(.app)**

```groovy
apply plugin: 'kotlin-kapt'
apply plugin: 'dagger.hilt.android.plugin'

android {
  			︙
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
        ︙
}

dependencies {
  			︙
    def dagger_hilt_version = "2.28-alpha"
    implementation "com.google.dagger:hilt-android:${dagger_hilt_version}"
    kapt "com.google.dagger:hilt-android-compiler:${dagger_hilt_version}"
    
    def dagger_hilt_view_model_version = "1.0.0-alpha01"
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:${dagger_hilt_view_model_version}"
    kapt "androidx.hilt:hilt-compiler:${dagger_hilt_view_model_version}"

  			︙
}
```

## Application に @HiltAndroidApp をつける

- Dagger Hilt を使う際には必ず Application を定義する必要があるので次の Application を定義する
- Application には @HiltAndroidApp をつける、これで Hilt Component が Application にアタッチされるようになる。

```kotlin
@HiltAndroidApp
class MainApplication : Application()	
```

##  Activity や Fragment に 依存関係を注入できるようにする

- Dagger Hilt では Application・Activity・Fragment・View・Service・BroadcastReceiver に対応している。
- しかし Fragment に @AndroidEntryPoint つけるときには Fragment を所持する Activity にもつける必要がある。
- ＠AndroidEntryPoint をつけたらあとは @Inject をつけたフィールドを定義すればフィールドインジェクションできる。

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    @Inject 
    lateinit var usecase: FooUsecase
}
```

```kotlin
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    @Inject
    lateinit var usecase: FooUsecase
}
```

- 依存関係を注入するかは決めましたが、どのように依存関係のインスタンスを取得する決めていません。

- 依存関係のインスタンスを取得できるように、@Module, @Binds, @Provides を使った Module を定義してやります。

- Module は binding の集合でしかなく、実際に型のインスタンスを返したり、インジェクトするのは  Component の仕事です。

  定義した Module を @InstallIn にて Component に紐づけてやるとインジェクションができるようになります。

```kotlin
@Module
@InstallIn(ApplicationComponent::class)
abstract class ApplicationBindsModule {
    @Binds
    abstract fun bindFooService(fooServiceImpl: FooServiceImpl) : FooService
}

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationProvidesModule {
    @Provides
    fun provideFooUseCase(fooService: FooService) : FooUsecase {
        return FooUsecase(fooService)
    }
}
```

- あと インスタンスを生成するクラスには @Inject construct() をつけておく必要があります。
- つけてない場合には Dagger を経由して生成できないので注意です。

```kotlin
interface FooService {
    fun print(tag: String, message: String)
}

class FooServiceImpl @Inject constructor():
    FooService {
    override fun print(tag: String, message: String) {
        Log.v(tag, message)
    }
}

class FooUsecase @Inject constructor(private val service: FooService) {
    fun print(tag: String, message: String) {
        service.print(tag, message)
    }
}
```

# ViewModel  を依存関係として注入できるようにする

- ViewModel を依存関係の注入は Dagger Hilt ではなく hilt-lifecycle-viewmodel にてサポートしています。

  （[Hilt and Jetpack integrations](https://developer.android.com/training/dependency-injection/hilt-jetpack)に詳細が記述されています。現時点(2020/06/27)では 1.0.0-alpha01 になっているみたいですね。）

-  ViewModel には @ViewModelInject をつければ Hilt で使えるようになるみたいです、

   SavedStateHandle を使う場合は @Assisted をつけてやれば良いみたいです。

- あとは ViewModel を利用したところで by viewModels() をつけてやれば使えるようになります。

```kotlin
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
     private val viewModel: MainViewModel by viewModels()
}
```

```kotlin
class MainViewModel @ViewModelInject constructor(
    private val usecase: FooUsecase,
    @Assisted private val savedState: SavedStateHandle
): ViewModel() {
    fun print() {
        usecase.print("MainViewModel", "Event")
    }
}
```

# Dagger Hilt の Component について

- 最初に Dagger Hilt では Component が標準セットが用意されていると説明しましたが、 

  Dagger Hilt では次のように階層に Component が定義されています。

- これらの Component は Android のライフサイクルに応じて生成されたら破棄されるようになっている。また Component ごとに Scope も定義されているのでこのインスタンスは Android のライフサイクルの変化に応じてインスタンスを生成したりできるようになっているらしい。(詳しくは[Androidクラス用に生成されたコンポーネント](https://developer.android.com/training/dependency-injection/hilt-android#generated-components)に記述がある)

![](https://developer.android.com/images/training/dependency-injection/hilt-hierarchy.svg)

