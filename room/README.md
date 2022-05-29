# 2020/05/14 [Android] Room (SQLite) でテーブルからビューを生成する

# はじめに

SQLite のラッパーライブラリである Room を利用して、あるテーブルからビューを生成してみたいと思います。今回は 2つのテーブルから、1つのビューを生成しようと思います。次のような User と Repo という 2つのテーブルを作成し、そこから UserRepo という 1つのビューを生成する感じのものです。

[![Image from Gyazo](https://i.gyazo.com/4cc55989b9e7474e605fb71ba14ce0f2.png)](https://gyazo.com/4cc55989b9e7474e605fb71ba14ce0f2)
※ ビューを生成するだけために UserId を主キーとする User と Repo のテーブルを定義してます。内容に関してはあまり意味があるわけではありません。

# 準備
Roomのセットアップ方法は[［Android］Roomを使ったサンプルと解説](https://medium.com/kaleidot725/android-room-を使ったサンプルと解説-a3f2ed978af4) で紹介していますので参考にしてセットアップします。

# 実装
## User テーブルを作成する

User 情報のためのテーブルを users というテーブル名称で作成します。 User は主キーとして ID を持ち、その他に FirstName や LastName を持つユーザー情報になります。

**User.kt**

```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val firstName: String?,
    val lastName: String?
)
```

テーブルの操作には Dao が必要になります。なので Insert, Update, Delete, DeleteAll, GetAll を備えた標準的な Dao を定義します。

**UserDao.kt**

```kotlin
@Dao
interface UserDao {
    @Insert
    fun insert(user : User)

    @Update
    fun update(user : User)

    @Delete
    fun delete(user : User)

    @Query("delete from users")
    fun deleteAll()

    @Query("select * from users")
    fun getAll(): List<User>

    @Query("select * from users where id = :id")
    fun getUser(id: Int): User
}
```

## Repo テーブルを作成する

Repo 情報のためのテーブルを repos というテーブル名称で作成します。 Repo は主キーとして User のID を持ち、その他に Name を持つレポジトリ情報になります。

**Repo**

```kotlin
@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey val userId: Int,
    val name: String?
)
```

テーブルの操作には Dao が必要になります。なので Insert, Update, Delete, DeleteAll, GetAll を備えた標準的な Dao を定義します。

**RepoDao.kt**(
```kotlin
@Dao
interface RepoDao {
    @Insert
    fun insert(repo : Repo)

    @Update
    fun update(repo : Repo)

    @Delete
    fun delete(repo : Repo)

    @Query("delete from repos")
    fun deleteAll()

    @Query("select * from repos")
    fun getAll(): List<Repo>

    @Query("select * from repos where userId = :userId")
    fun getRepo(userId: Int): Repo
}
```

## UserRepo ビューを生成する

users と repos テーブルから UserRepo ビューを作成していきます。Room でビューを作成するには @DatabaseView をつけ、viewName にはビュー名称、value にはどのようなビューを生成するのか記述した SQL 文を記述します。

**SQL記述の際に気をつけること**

- SQL 文に含めるデーブル名称は、各テーブルの定義で指定した名称にする
- SELECT で指定した属性名、クラスで定義したプロパティ名が一致しなければならない。

```kotlin
@DatabaseView(
    viewName = "UserRepo",
    value = """
        SELECT users.id, users.lastName, users.firstName,
        repos.name AS repoName FROM users
        INNER JOIN repos ON users.id = repos.userId
    """
)
data class UserRepo(
    val id: Int,
    val lastName: String?,
    val firstName: String?,
    val repoName: String?
)
```

ビューを操作するにも Dao が必要になります。ビューを操作する Dao では 
@Insert や @Delete、@Update などは使えません。そのため全て @Query で操作を記述する必要があります。今回は GetAll と GetUserRepo という単純な操作を定義しました。

```kotlin
@Dao
interface UserRepoDao {
    @Query("select * from UserRepo")
    fun getAll(): List<UserRepo>

    @Query("select * from UserRepo where id = :id")
    fun getUserRepo(id: Int): UserRepo
}
```

## RoomDatabase を作成する

定義したテーブルとビューを操作するには RoomDatabase を実装したクラスが必要です。RoomDatabase の実装でやらなければいけないことは 2つです。

1つ目ですが、@Databse アノテーションをつける必要があります。@Database アノテーションを付け、entities には利用するテーブル、 views には利用するビューを指定してやります。

2つ目ですが テーブルとビューを操作する Dao を取得するメソッドを実装する必要があります。実装ですが簡単で Dao を戻り値とする関数を定義するだけです。
　
```kotlin
@Database(entities = [User::class, Repo::class], views = [UserRepo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repoDao(): RepoDao
    abstract fun userRepoDao(): UserRepoDao
}
```

これで RoomDatabase の定義が終わり、テーブルとビューを操作できるようになりました。
# 動作確認

実装が終わったのであとは動作確認してみましょう。定義した RoomDatabase を初期化し、それぞれのテーブルやビューの Dao を取得します。そして新たに User と Repo の情報をテーブルに追加した上で、UserRepo ビューから取得してみます。

```kotlin
class MainActivity : AppCompatActivity() {
    private val database : AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.IO).launch {
            testForUserRepo()
        }
    }
    
    private fun testForUserRepo() {
        val userDao = database.userDao()
        val repoDao = database.repoDao()
        val userRepoDao = database.userRepoDao()

        val newUser = User(0, Date().time.toString(), Date().time.toString())
        val newRepo = Repo(newUser.id, Date().time.toString())

        userDao.insert(newUser)
        repoDao.insert(newRepo)
        Log.v("TAG", "get view ${userRepoDao.getAll()}")
        
        userDao.delete(newUser)
        repoDao.delete(newRepo)
        Log.v("TAG", "get view ${userRepoDao.getAll()}")
    }
}
```

実行すると意図したとおり、 User と Repo があわさった UserRepo が取得できました。

```
V/TAG: get view [UserRepo(id=0, lastName=1589440137149, firstName=1589440137149, repoName=1589440137160)]
V/TAG: get view []
```
# おわりに
Room でもビューは定義できました。説明した通りテーブルといくつか異なる点があるので注意が必要ですね。作成したコードは下記に保存しているので興味があれば閲覧してみてください。

<a href="https://github.com/kaleidot725-android/room"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/room.png" width="460px"></a>

# 参考文献
- [Room 永続ライブラリ | Android Developers](https://developer.android.com/topic/libraries/architecture/room)
- [ビューを作成してデータベースに組み込む | Android Developers](https://developer.android.com/training/data-storage/room/creating-views)
