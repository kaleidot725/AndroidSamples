package jp.kaleidot725.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import jp.kaleidot725.sample.ui.theme.SQLDelightTheme
import jpkaleidot725sample.HockeyPlayer
import jpkaleidot725sample.PlayerQueries

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainPage() }

        val driver: SqlDriver = AndroidSqliteDriver(Database.Schema, applicationContext, "test.db")
        val database = Database(driver)

        val playerQueries: PlayerQueries = database.playerQueries

        playerQueries.insert(player_number = 10, full_name = "Corey Perry")
        println(playerQueries.selectAll().executeAsList())

        val player = HockeyPlayer(10, "Ronald McDonald")
        playerQueries.insertFullPlayerObject(player)

        val players = playerQueries.selectAll().executeAsList()
        Toast.makeText(applicationContext, players.toString(), Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun MainPage() {
    SQLDelightTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            Text("Android Test")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPage_Preview() {
    MainPage()
}