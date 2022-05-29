#  2020/04/08 [Android] Firebase Cloud Firestore を使ってデータを追加・削除・取得する

# はじめに

Firebase の Cloud Firestore を使ったことがありませんでした。今更な感じですがどんなものか知るために Cloud Firestore を使ってデータの追加・削除・取得をやってみたいと思います。

```
-------------------------------------------------------------------------------------------
◆ Cloud Firestore とは？ ◆
-------------------------------------------------------------------------------------------
Firebase の Cloud Firestore は クラウドホスト型の NoSQLデータベース です。
Cloud Firestore は iOS アプリや Android アプリおよびWebアプリから SDK を介して直接アクセスできます。
-------------------------------------------------------------------------------------------
```


# Firebase のプロジェクトを作成する

Firebase ではプロジェクトを作成し、そのプロジェクトで設定を行うことで Cloud Firestore を利用できるようにします。 なのでまずは次の手順で Firebase のプロジェクトを作成していきます。

1. [Firebase] にログインし、「プロジェクト」の作成を選択する。
  (FirebaseはGoogleのアカウントでログインできます。)
2. 「プロジェクト名」に任意の名称を入力して、「続行」を選択する。
3. 「Googleアナリティクス」を有効・無効どちらか選択し、「続行」を選択する
4. 「Googleアナリティクスアカウント」を選択し、「続行」を選択する
   (特に何もなければ、Default Account for Firebase でよいはず)
5. [Firebase のコンソール](https://console.firebase.google.com)に移動し、作成した Firebase プロジェクトを選択する。


# Android プロジェクトを作成する

Android プロジェクトを作成して、Cloud Firestore を利用できるようにします。
まずば Firebase プロジェクト上で次の操作でセットアップを行います。

1. [Firebase のコンソール](https://console.firebase.google.com)で　Firebase プロジェクトを選択する。
2. **アプリを追加** を選択し、**プラットフォーム** で Android を選択する。
3. **Androidパッケージ名**の入力が求められるので、Android プロジェクトのパッケージ名を入力し、次へを選択する。
4. **設定ファイルのダウンロード**で、google-service.json をダウンロードする。
5. **Firebase SDK の追加** や **アプリを実行してインストールを確認**は後で確認するので、次へを選択する。

そして Android Studio 上で Android プロジェクトのセットアップを行います。

1. ［Android Studio］Android プロジェクトを作成する。
2. ［Andriod Studio］app に google-service.json をコピーする
3. ［Android Studio］app の build.gradle で必要なライブラリを加える

```groovy
dependencies {
       ︙
    def firebase_analytics_version = "17.3.0"
    implementation "com.google.firebase:firebase-analytics:${firebase_analytics_version}"

    def firebase_fire_store_version = "21.4.2"
    implementation "com.google.firebase:firebase-firestore:${firebase_fire_store_version}"
       ︙
}
```

これで Android アプリ から Cloud Firestore にアクセスできるようになります。


# Cloud Firestore で Task を追加・削除・取得する

Cloud Firestore は NoSQLデータベースで、ドキュメントデータベースのデータモデルに従ってデータを管理します。データモデルとしてコレクション・ドキュメント・データとありこれを組み合わせてデータを管理します。

| データ | |
| ------- | ------- |
| コレクション | 複数のドキュメントを格納し、<br>データの編集やクエリなどを実行するためのデータモデル |
| ドキュメント | データを格納するデータモデル |
| データ | 単純な文字列から数値などを格納するデータモデル<br>[(サポートしているデータ・タイプ](https://firebase.google.com/docs/firestore/manage-data/data-types)) |


[![Image from Gyazo](https://i.gyazo.com/36e35912e49cc43ea096c8c1b37addb3.png)](https://gyazo.com/36e35912e49cc43ea096c8c1b37addb3)

今回は次の Task クラスを定義し、次のデータモデル構成で Task オブジェクトを管理してみたいと思います。

```kotlin
data class Task(val id: String, val time: Long, val name: String) {
    companion object {
        fun create(name: String): Task = Task(UUID.randomUUID().toString(), Date().time, name)
    }
}
```

[![Image from Gyazo](https://i.gyazo.com/b25503d4c827b1043528acad13f9d5dc.png)](https://gyazo.com/b25503d4c827b1043528acad13f9d5dc)
a
## データベースを取得する

Cloud Firestore でデータベースを制御するには FirebaseFireStore オブジェクトを利用します。次のコードで FirebaseFireStore オブジェクトを取得します。

```kotlin
private val database : FirebaseFirestore get() = FirebaseFirestore.getInstance()
```


## ドキュメントを追加し、Taskデータを保存する


どのコレクションのドキュメントを追加するか決めます。`FirebaseFirestore.collection(name: String)` に名前を入力しコレクションを取得します。コレクションを取得したら`Collection.document(name:String)`に任意の名前を入力しドキュメントを作成します。名前を指定しない `Collection.document()` でも作成でき、これだとドキュメントの名称は自動で生成されます。

ドキュメントができたらデータを追加するだけですが、データは `Map<String, *>` である必要があります。今回は `Task.toMap` を用意しました、これで `Map<String, *>` に変換して `Document.set` してやれば Task オブジェクトを保存できます。

```kotlin
suspend fun add(task: Task): Boolean {
    try {
        val collection = database.collection("CollectionName")
        val document = collection.document(task.id)
        val data = task.toHashMap()
        document.set(data).await()
        return true
    } catch (e: Exception) {
        return false
    }
}

```

```kotlin
fun Task.toMap(): Map<String, *> {
    return hashMapOf(
        "id" to this.id,
        "time" to this.time,
        "name" to this.name
    )
}
```

## ドキュメントを削除し、Taskデータを削除する

どのコレクションのドキュメントを削除するか決めます。`FirebaseFirestore.collection(name: String)` に名前を入力しコレクションを取得します。コレクションを取得したら`Collection.document(name:String)`で削除するドキュメントを取得します。あとは`document.delete()`を実行するとドキュメントが削除され、そこに格納されていた Task データも削除されます。

```kotlin
suspend fun delete(task: Task): Boolean {
    try {
        val collection = database.collection("CollectionName")
        val document = collection.document(task.id)
        document.delete().await()
        return true
    } catch (e: Exception) {
        return false
    }
}
```


## ドキュメントを検索し、 ある Task データを取得する

どのコレクションのドキュメントを検索するか決めます。`FirebaseFirestore.collection(name: String)` に名前を入力しコレクションを取得します。コレクションには`limit`や`orderBy`などのクエリが用意されており、取得するドキュメントを指定できるようになっています。今回は`limit`を使ってある上限数を以内のドキュメントを取得するようにします。

ドキュメントからデータを取得するだけですが、データは `Map<String, *>` であるので、`Task`に変換する必要があります。今回は `Map<String, *>.toTask` を用意して、これで取得したドキュメントのデータを `Task` に変換してやれば Task オブジェクトの取得ができます。

```kotlin
suspend fun fetchTask(limit: Long): List<Task> {
    try {
        val collection = database.collection("CollectionName")
        val documents = collection.limit(limit).get().documents
        return documents.map { it.data }.mapNotNull { it?.toTask() }
    } catch (e: Exception) {
        return listOf()
    }
}
```

```kotlin
fun Map<String, Any>.toTask(): Task {
    val id = this["id"] as String
    val time = this["time"] as Long
    val name = this["name"] as String
    return Task(id, time, name)
}
```


## 実際に動かしてみる

これでドキュメントの追加・削除・取得ができるようになりました。
これらの処理を TaskRepository にまとめると次のような感じになります。

```kotlin
class TaskRepository {
    private val database : FirebaseFirestore get() = FirebaseFirestore.getInstance()

    suspend fun add(task: Task): Boolean {
        try {
            val collection = database.collection(COLLECTION_PATH)
            val document = collection.document(task.id)
            val data = task.toHashMap()
            document.set(data).await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

   suspend fun delete(task: Task): Boolean {
        try {
            val collection = database.collection(COLLECTION_PATH)
            val document = collection.document(task.id)
            document.delete().await()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun fetchTask(limit: Long): List<Task> {
        try {
            val collection = database.collection("CollectionName")
            val documents = collection.limit(limit).get().documents
            return documents.map { it.data }.mapNotNull { it?.toTask() }
        } catch (e: Exception) {
            return listOf()
        }
    }
    
    companion object {
        private const val COLLECTION_PATH = "tasks"
    }
}
```

次のように`TaskRepository`を使って取得・追加・削除の処理を記述してみましょうか。

```kotlin
class MainActivity : AppCompatActivity() {
    private val repository: TaskRepository = TaskRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            // ①現在のタスク一覧を取得する
            println("①現在のタスク一覧を取得する ▶ " + repository.fetchTask(100))

            // ②新しいタスクを追加する
            val newTask = Task.create("New Task")
            repository.add(newTask)
            println("①新しいタスクを追加する ▶ " + repository.fetchTask(100))

            // ③新しく追加したタスクを削除する
            repository.delete(newTask)
            println("③新しく追加したタスクを削除する ▶ " + repository.fetchTask(100))
        }
    }
}
```

実行結果は次のようになりました、取得・追加・削除がうまく動作していますね。

```
①現在のタスク一覧を取得する ▶ []
②新しいタスクを追加する ▶ [Task(id=8895dfb2-4aa0-4da0-bf3e-72da7a192fb8, time=1586591310782, name=New Task)]
③新しく追加したタスクを削除する ▶ []
```

# おわりに

Firebase Cloud Firestore のチュートリアルではさらっとドキュメントデータベースであることが書かれていますが、ドキュメントデータベースであることがわからないとどれだけ API の仕様を眺めて Cloud Firestore を理解できないですね。NoSQLやドキュメントデータベースのコンセプトがわかると一気に理解できるようになりました。まだデータ構造など何が最適化わからないですが、非常に強力な開発環境だと思うので少しずづ使えるようになっていきたいですね。

<a href="https://github.com/kaleidot725-android/firebase_cloud_firestore"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/firebase_cloud_firestore.png" width="460px"></a>
