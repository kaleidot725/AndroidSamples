package jp.kaleidot725.sample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            RequestPermission()
            RequestPermissionUsingAccompanist()
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