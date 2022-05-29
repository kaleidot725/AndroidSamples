package jp.kaleidot725.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
