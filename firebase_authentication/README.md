# 2020/04/18 ［Android］Google アカウントを利用して Firebase にサインインする

# はじめに

Firebase には FireabaseUI Auth という予め用意された UI を使ってサインインする方法と Fireabase SDK を利用してサインインする 2 つの方法があります。今回は Fireabase SDK を利用して Firebase にサインインする方法を試してみます。また Firebase には Mail・Google・Twitter・Facebook などでサインインする方法があるのですが、色々試すのは大変なので Google を使ったサインインするようにしてみます。

# 準備

Firebase を利用するには Firebase プロジェクト、 Android プロジェクトの作成が必要になります。なので下記の手順でセットアップをしていきます。

## Firebase のプロジェクトを作成する

次の手順で Firebase のプロジェクトを作成していきます。

1. [Firebase] にサインインし、「プロジェクト」の作成を選択する。
2. 「プロジェクト名」に任意の名称を入力して、「続行」を選択する。
3. 「Googleアナリティクス」を選択し、「続行」を選択する
4. 「Googleアナリティクスアカウント」を選択し、「続行」を選択する
5. [Firebase のコンソール](https://console.firebase.google.com)に移動し、作成した Firebase プロジェクトを選択する。


作成したプロジェクトに次の設定を行い、 Android アプリから Firebase を利用できるようにします。

1. [Firebase のコンソール](https://console.firebase.google.com)で　Firebase プロジェクトを選択する。
2. アプリを追加 を選択し、プラットフォーム で Android を選択する。
3. Androidパッケージ名の入力が求められるので、Android プロジェクトのパッケージ名を入力する。
4. デバッグ用の署名証明書を入力し、アプリを登録を選択する。(署名証明書の作成は[こちら](https://developers.google.com/android/guides/client-auth))
5. 設定ファイルのダウンロード や Firebase SDK の追加 や   
   アプリを実行してインストールを確認 は後で確認するので、次へを選択する。
6.  [Firebase のコンソール](https://console.firebase.google.com)に戻り、Authentication -> Sign-in methodを選択する
7.  Sign-in method で Google を有効にする。
8.  最後に プロジェクトの概要 -> 作成したアプリ名称 設定アイコン -> google-service.json でファイルをダウンロードする。

## Android プロジェクトを作成する

そして次の手順で Android Studio で Android プロジェクトを作成します。これで Android から Firebase が利用できるようになります。

1. ［Android Studio］Android プロジェクトを作成する。
2. ［Andriod Studio］app に google-service.json をコピーする
3. ［Android Studio］app の build.gradle で必要なライブラリを加える

```groovy
buildscript {
    ext.kotlin_version = '1.3.71'
    repositories {
        google() // 追加する
        jcenter()

    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.1'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath 'com.google.gms:google-services:4.3.3' // 追加する
    }
}

allprojects {
    repositories {
        google() // 追加する
        jcenter()

    }
}
```

```groovy
apply plugin: 'com.google.gms.google-services'

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.core:core-ktx:1.2.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'

    // Firebase Authentication
    implementation 'com.firebaseui:firebase-ui-auth:6.2.1' // 追加
    implementation 'com.google.firebase:firebase-auth:19.3.0' // 追加

    // Kotlin Coroutine
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5' // 必要に応じて追加
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.5' // 必要に応じて追加

    // KTX
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.3.0-alpha01' // 必要に応じて追加
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha01' // 必要に応じて追加

    // LiveEvent
    implementation "com.github.hadilq.liveevent:liveevent:1.0.1" // 必要に応じて追加
}

```

# 実装


**初期化する**

次のコードで GoogleSignInClient と FirebaseAuth クラスのオブジェクトの初期化をします。GoogleSignInClient は Google アカウントへのサインインを制御するクラス、 FirebaseAuth は Firebase へのサインインを制御するクラスになります。これらの 2つのオブジェクトを利用して Google アカウントを利用した Firebase へのサインインを実装していきます。

```kotlin
class GoogleAuthController(private val activity: AppCompatActivity) {
    // FirebaseAuth の生成
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // GoogleSignInClient の生成
    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(activity, googleSignInOptions)
    }

    // GoogleSignInClient のオプション、今回は特に設定が必要ないのでデフォルト値的なものを利用する
    private val googleSignInOptions: GoogleSignInOptions by lazy {
        val signInOption = GoogleSignInOptions.DEFAULT_SIGN_IN
        val idToken = activity.getString(R.string.default_web_client_id)
        GoogleSignInOptions.Builder(signInOption).requestIdToken(idToken).requestEmail().build()
    }
}
```


**サインインする**

まずはFirebaseへのサインインに利用する Google アカウントを選択する必要があります。アカウントの選択は GoogleSignInClient の signInIntent 経由で選択できるようになっているので signInIntent を取得して startActivityForResult にセットして Intent を起動してやります。


```kotlin
class GoogleAuthController(private val activity: AppCompatActivity) {
    fun startSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    
    companion object {
        private const val RC_SIGN_IN = 100
    }
}
```

startActivityForResult で signInIntent を起動してやると次のような画面が出てくるので、表示された Google アカウントを選択して Firebase にサインインに利用するアカウントを決めてやります。

[![Image from Gyazo](https://i.gyazo.com/73282dbe5b43b50c252dbf46149a401c.png)](https://gyazo.com/73282dbe5b43b50c252dbf46149a401c)

アカウントの選択が完了したら、onActivityResult で選択した Google のアカウント情報(GoogleSignInAccount) を 引数の data(Intent) から取得してやります。あとは取得した アカウント情報(GoogleSignInAccount) から credential を生成し、FirebathAuth.signInWithCredential に credential を渡して実行すればサインインできます。あとサインイン完了の通知を受け取りたいこともあると思います、その場合は firebaseAuth.signInWithCredential の戻り値の Task の addOnCompleteListener でリスナーを追加してやります。今回はサインイン完了の処理はあとからカスタマイズできるようにサインイン開始時に引数で関数を渡し、完了時に呼び出す形にしています。

```kotlin
class GoogleAuthController(private val activity: AppCompatActivity) {
   private var completed: (FirebaseUser) -> (Unit) = {}

   fun startSignIn(completed: (FirebaseUser) -> (Unit)) {
      this.completed = completed
      val signInIntent = googleSignInClient.signInIntent
      activity.startActivityForResult(signInIntent, RC_SIGN_IN)
   }
    
   fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        ︙
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            if (firebaseAuth.currentUser == null) {
                return@addOnCompleteListener
            }

            completed(firebaseAuth.currentUser!!)
        }
    }
   

}
```


**サインアウトする**

サインアウトは簡単です、Google と Firebase 両方をサインアウトしてやれば良いです。

```kotlin
class GoogleAuthController(private val activity: AppCompatActivity) {
    fun startSignOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }
}
```

**最終的なコード**

最終的にこのようなコードになりました、あとはこいつを Activity で呼び出してやればサインイン・サインアウトができるようになります。

```kotlin

class GoogleAuthController(private val activity: AppCompatActivity) {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val googleSignInOptions: GoogleSignInOptions by lazy {
        val signInOption = GoogleSignInOptions.DEFAULT_SIGN_IN
        val idToken = activity.getString(R.string.default_web_client_id)
        GoogleSignInOptions.Builder(signInOption).requestIdToken(idToken).requestEmail().build()
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(activity, googleSignInOptions)
    }

    private var completed: (FirebaseUser) -> (Unit) = {}

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    fun startSignIn(completed: (FirebaseUser) -> (Unit)) {
        this.completed = completed
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun startSignOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != RC_SIGN_IN) {
            return
        }

        val clazz = ApiException::class.java
        val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(clazz)
        if (account == null) {
            return
        }

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            if (firebaseAuth.currentUser == null) {
                return@addOnCompleteListener
            }

            completed(firebaseAuth.currentUser!!)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 100
    }
}
```


# 動作確認

あと動作を確認するための UI を作ってやります。Button を押すとサインイン・サインアウト、サインインできたらユーザートークンを取得し更新するシンプルなUIです。

[![Image from Gyazo](https://i.gyazo.com/759d2fe0ab330f37c7d42ef28ca63548.png)](https://gyazo.com/759d2fe0ab330f37c7d42ef28ca63548)

```kotlin
class MainActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val googleAuthController: GoogleAuthController by lazy {
        GoogleAuthController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userIdTextView = findViewById<TextView>(R.id.user_id_text_view)
        val signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            googleAuthController.startSignIn {
                coroutineScope.launch {
                    val token = Tasks.await(it.getIdToken(true)).token
                    withContext(Dispatchers.Main) {
                        userIdTextView.text = token
                    }
                }
            }
        }

        val signOutButton = findViewById<Button>(R.id.sign_out_button)
        signOutButton.setOnClickListener {
            googleAuthController.startSignOut()
            userIdTextView.text = ""
        }

        coroutineScope.launch {
            val currentUser = googleAuthController.currentUser
            if (currentUser != null) {
                val token = Tasks.await(currentUser.getIdToken(true)).token
                withContext(Dispatchers.Main) {
                    userIdTextView.text = token
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleAuthController.onActivityResult(requestCode, resultCode, data)
    }
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/user_id_text_view"
        android:layout_width="0dp"
        android:layout_height="0dp"a
        android:gravity="center"
        android:textSize="16sp"
        app:layout_constraintBottom_toTopOf="@id/sign_in_button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/sign_in_button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Sign In"
        app:layout_constraintTop_toBottomOf="@id/user_id_text_view"
        app:layout_constraintBottom_toTopOf="@id/sign_out_button"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

    <Button
        android:id="@+id/sign_out_button"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Sign Out"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/sign_in_button"
        app:layout_constraintBottom_toBottomOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

サインインするとユーザートークンが表示され、

[![Image from Gyazo](https://i.gyazo.com/9b7730286e714427e2f414a785382173.png)](https://gyazo.com/9b7730286e714427e2f414a785382173)

サインアウトするとユーザートークンが消えますね。

[![Image from Gyazo](https://i.gyazo.com/f659b3d20bee30b4b8c4fd9db17fd719.png)](https://gyazo.com/f659b3d20bee30b4b8c4fd9db17fd719)

# まとめ

Google アカウントを利用した Firebase へのサインインは Google アカウント情報を取得するために Intent の起動、 Intent の結果取得を自身で制御するので少しややこしいです。あと Google アカウントを利用したサインインは公式ドキュメントもあまり充実していないような気がするので、自分で少しずつ試していくしかなさそうです。

<a href="https://github.com/kaleidot725-android/firebase_authentication"><img src="https://github-link-card.s3.ap-northeast-1.amazonaws.com/kaleidot725-android/firebase_authentication.png" width="460px"></a>

# 参考文献

- [Authenticate Using Google Sign-In on Android](https://firebase.google.com/docs/auth/android/google-signin)
