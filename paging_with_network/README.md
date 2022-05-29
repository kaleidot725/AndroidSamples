# 2020/03/31 ［Android］RecyclerView と Paging を組み合わせサンプルと解説

# はじめに

ページ毎にデータを読み込む RecyclerView を作ってみたいと思います。
アーキテクチャは Google が推奨している MVVM を利用して、次の手順で作成を進めます。

| No | タイトル |
| -------- | ------- |
| Step 0  | 必要なライブラリをセットアップする |
| Step 1  | Retorift でページ毎のデータを取得する |
| Step 2  | PagedList でページ毎のデータを管理する |
| Step 3  | RecyclerView に PagedList のデータを表示する |
| Step 4  | 作成したクラスの動作を確認する |

作成したときの完成イメージは次のような感じになります。

[![Image from Gyazo](https://i.gyazo.com/4b32170ba52e3513a0dd8918366ef2ff.png)](https://gyazo.com/4b32170ba52e3513a0dd8918366ef2ff)

# TL;DR

- PagedList がページ毎のデータの管理をする。
- PagedList を生成するには DataSource・DataSource.Factory が必要になる。
- PagedList を RecyclerView で表示するには PagedListAdapter が必要になる。
- ページごとのデータ管理は PageList が中心となって行う、  
  そのため PagedList の生成や表示に必要となる周辺のクラスを実装していく必要がある。    
  それらの作成するクラスをまとめると次の表や図のような感じになる。  

| 分類 | 名称 | 説明 |
| ------ | ------- | ------- |
| View | PagedListAdapter | RecyclerView に PagedList を表示するための Adapter クラスの実装 |
| ViewModel | LiveData\<PagedList\<Item>> | ページ毎のデータ管理を行うためのクラスの実装 |
| Model | PageKeyedDataSource | PagedListを生成するのに必要となるクラスの実装 |
| Model | Data Source Factory | PagedListを生成するのに必要となるクラスの実装|
| Model | Service Class | ページ毎のデータ取得するクラスの実装 |

![image.png](https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/79387/5f561913-25c2-ec2a-d650-15dd129edbb1.png)

# Step 0 必要なライブラリをセットアップする

アプリケーションの作成に必要となる、Koin・Retrofit・Paging・RecyclerView・CardView などのライブラリをインストールする。

| ライブラリ | バージョン | 説明 |
| ------- | ------- | ------- |
| Koin | 2.1.3 | DI ライブラリ |
| Retrofit | 2.2.4 | HTTP クライアントライブラリ |
| Paging | 2.1.2 | Paging ライブラリ |
| RecyclerView | 1.1.0 | RecyclerView を利用するために必要 |
| CardView | 1.0.0 | CardView を利用するために必要 |


```groovy
dependencies {
    ︙
    ︙
    def koin_version = "2.1.3"
    implementation "org.koin:koin-android:$koin_version"
    implementation "org.koin:koin-android-scope:$koin_version"
    implementation "org.koin:koin-android-viewmodel:$koin_version"
    implementation "org.koin:koin-android-ext:$koin_version"

    def retrofit_version ="2.1.0"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"

    def lifecycle_version = "2.2.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"

    def paging_version = "2.1.2"
    implementation "androidx.paging:paging-runtime:$paging_version"

    def recycler_view_version = "1.1.0"
    implementation "androidx.recyclerview:recyclerview:$recycler_view_version"

    def card_view_version = "1.0.0"
    implementation "androidx.cardview:cardview:$card_view_version"
    ︙
    ︙
}
```
# Step 1 Retorift でページ毎のデータを取得する

今回はページ単位の情報を取得する API として Qiita の記事一覧の取得 API  ([ GET /api/v2/items ](https://qiita.com/api/v2/docs#get-apiv2items)) を利用します。HTTP Client には Retrofit を利用します。なので次のクラスを作成して API を呼び出せるようにしていきます。

| 役割 | クラス名 | 役割 |
| ------- | ------- | -------- |
| Entity | Item | Qiitaの記事データを表すデータクラス |
| Service | QiitaService | Qiita API を利用するためのサービスクラス |
| Data Source | ItemDataSource | QiitaService を利用してページ毎のデータを取得するクラス |
| Data Source Factory | ItemDataSourceFactory | ItemDataSource を生成するクラス |

**Item**
Qiitaの記事データを表すデータクラスを宣言します。Item には Group や Tag や User といった情報も含まれるのであわせて定義していきます。API で利用するデータクラスは[JSON To Kotlin Class](https://plugins.jetbrains.com/plugin/9960-json-to-kotlin-class-jsontokotlinclass-)を利用すると JSON を入力するだけで自動生成してくれるので楽です。

```kotlin
data class Item(
    val body: String, val editing: Boolean, val comments_count: Int, val created_at: String,
    val group: Group, val id: String, val likes_count: Int, val page_views_count: Int, val `private`: Boolean,
    val reactions_count: Int, val rendered_body: String, val tags: List<Tag>, val title: String, val updated_at: String,
    val url: String, val user: User
)

data class Group(
    val created_at: String, val id: Int, val name: String,
    val `private`: Boolean, val updated_at: String, val url_name: String
)

data class Tag(val name: String, val versions: List<String>)

data class User(
    val description: String, val facebook_id: String, val followees_count: Int,
    val followers_count: Int, val github_login_name: String, val id: String, val items_count: Int,
    val linkedin_id: String, val location: String, val name: String, val organization: String, val permanent_id: Int,
    val profile_image_url: String, val team_only: Boolean, val twitter_screen_name: String, val website_url: String
)
```

**QiitaService**

Retrofit で Qiita API の [GET /api/v2/items ](https://qiita.com/api/v2/docs#get-apiv2items)を利用できるようにします。今回はページ毎に取得したいので page や per_page のクエリを追加しておきます。Retrofit の実装方法の詳細については[公式ドキュメント](https://square.github.io/retrofit/) を閲覧してください。

```kotlin
interface QiitaService {
    @GET("/api/v2/items")
    fun getItems(@Query("page")page: Int, @Query("per_page") perPage: Int): Call<List<Item>>
}
```

# Step2 PagedList でページ毎のデータを管理する

**PagedListの生成方法**

ページ毎に API からデータを取得する場合は PagedList を生成します。 PagedList の生成は LivePagedListBuilder で行いますが、そのときに DataSource と DataSource.Factory が必要になるので実装します。

```kotlin
class MainViewModel(private val itemDataSourcefactory: ItemDataSourceFactory): ViewModel() {
    val items: LiveData<PagedList<Item>> = LivePagedListBuilder(DataSource.Factory, PagedList.Config).build()
}
```

**ItemDataSource**

DataSource で実際にどのようなデータをページ毎に生成するか決めます。今回は RecyclerView が表示されたら 1ページ目のデータ、末尾に到達したら次のページのデータを表示するように実装してみます。（データ取得には先程作成した、Qiitaの記事一覧取得処理を利用します。）

```kotlin
// API呼び出しをしているので、本来であればここで例外の対処を記述する必要がありますが省略しています。
class ItemDataSource(private val service: QiitaService) : PageKeyedDataSource<Int, Item>() {
    // RecyclerView の末尾にデータを追加するときに呼び出される関数
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {}

    // RecyclerView のデータを初期化するときに呼び出される関数 
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Item>) {
        // 1 ページ目のデータを取得する
        val page = 1

        // 1 ページに表示するデータ数
        val perPage = params.requestedLoadSize

        // ページに表示するデータを取得する
        val items = service.getItems(page, perPage).execute().body()

        // 次に表示するページの番号を計算する
        val nextPage = page + 1

        // 取得したデータ、次に表示するページの番号を結果として返す
        callback.onResult(items, null, nextPage)
    }

    // RecyclerView の先頭にデータを追加するときに呼び出される関数
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        // params.key には 前の loadInitial や loadAfter の呼び出しで返した nextPage が格納されている
        val page = params.key // 1 ページ目のデータを取得する

        // params.requestedLoadSize には 1ページに表示するデータ数が格納されている。
        val perPage = params.requestedLoadSize

        // ページに表示するデータを取得する
        val items = service.getItems(page, perPage).execute().body()

        // 次に表示するページの番号を計算する
        val nextPage = page + 1

        // 取得したデータ、次に表示するページの番号を結果として返す
        callback.onResult(items,   nextPage)
    }
}
```

**ItemDataSourceFactory**

DataSource にデータ取得の処理を記述しましたが、DataSource.Factory を通して生成するような仕組みになっています。
実際に LivePagedListBuilder で利用するのは DataSource.Factory になりますので定義してやります。

```kotlin
class ItemDataSourceFactory(service: QiitaService) : DataSource.Factory<Int, Item>() {
    val source = ItemDataSource(service)

    override fun create(): DataSource<Int, Item> {
        return source
    }
}
```

**MainViewModel**

ここまで準備できればあとは PagedList を作成するだけです。作成した ItemDataSourceFactory を DI して LivePagedListBuilder に渡します。そして build してやれば PagedList が作成されます。

```kotlin
class MainViewModel(private val itemDataSourcefactory: ItemDataSourceFactory): ViewModel() { 
    private val config = PagedList.Config.Builder().setInitialLoadSizeHint(10).setPageSize(10).build() 
    val items: LiveData<PagedList<Item>> = LivePagedListBuilder(itemDataSourcefactory, config).build() 
    val networkState: LiveData<NetworkState> = itemDataSourcefactory.source.networkState 
} 
```

# Step 3 RecyclerView に PagedList のデータを表示する

先程作成した PagedList を RecyclerView を表示するには PagedListAdapter が必要になります。
次のように PagedListAdapter を継承した Adapter を作成してやります。

**ItemRecyclerAdapter**

```kotlin
class ItemRecyclerAdapter() : PagedListAdapter<Item, ItemHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false) as View
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.title.text = getItem(position)?.title
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item) =
                oldItem.id == newItem.id // check uniqueness

            override fun areContentsTheSame(oldItem: Item, newItem: Item) =
                oldItem == newItem // check contents
        }
    }
}

class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val title = view.title
}
```

**recycler_view_item.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="75dp"
    android:layout_margin="8dp"
    app:cardCornerRadius="8dp">

    <TextView
        android:id="@+id/title"
        android:layout_gravity="center|top"
        android:layout_margin="16dp"
        android:textSize="14sp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="TEST" />

</androidx.cardview.widget.CardView>
```

# Step 4 作成したクラスの動作を確認する

あとは今まで作成したクラスを Koin で初期化してセットアップしてやるだけです。
次の定義で QiitaService・ItemDataSourceFactory・MainViewModelを生成できるようにしてやります。

**Koin**

```kotlin
val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://qiita.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(QiitaService::class.java)
    }

    single {
        ItemDataSourceFactory(get())
    }

    viewModel {
        MainViewModel(get())
    }
}
```

そして Koin と RecyclerView のセットアップをしてやればアプリは完成になります。

**MainAcitivity**

```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val itemRecyclerAdapter = ItemRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // koin 初期化
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(applicationContext).apply {
            orientation = RecyclerView.VERTICAL
        }
        binding.itemRecyclerView.adapter = itemRecyclerAdapter
        binding.itemRecyclerView.setHasFixedSize(true)
        viewModel.items.observe(this, androidx.lifecycle.Observer {
            itemRecyclerAdapter.submitList(it)
        })
    }
}
```

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

        <variable
            name="viewModel"
            type="jp.kaleidot725.sample.ui.MainViewModel" />
    </data>

    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".ui.MainActivity">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/item_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </FrameLayout>
</layout>

```


# おわりに

ページごとのデータ管理は PageList が中心となって行うようですね。なので PagedList の生成や表示に必要になる周辺クラスを実装していくのが主な作業になりますね。ページごとのデータ管理は難しいと考えていましたが、Androidのライブラリで手厚くサポートされているのでそこまで難しくないですね。

<a href="https://github.com/kaleidot725-android/paging_with_network"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/paging_with_network.png" width="460px"></a>
