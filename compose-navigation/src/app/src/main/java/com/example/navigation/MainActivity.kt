package com.example.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "profile") {
                composable("profile") {
                    Profile(navController)
                }
                composable("friendList") {
                    FriendList(navController)
                }
            }
        }
    }
}

@Composable
fun Profile(navController: NavController) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Hello Profile!",
                style = TextStyle(fontSize = 22.sp, color = Color.Black)
            )
            Button(onClick = {
                navController.navigate("friendList")
            }) {
                Text(text = "Navigate friendList")
            }
        }
    }
}

@Composable
fun FriendList(navController: NavController) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Hello FriendList!",
                style = TextStyle(fontSize = 22.sp, color = Color.Black)
            )
            Button(onClick = {
                navController.navigate("profile")
            }) {
                Text(text = "Navigate profile")
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Profile(navController)
}

@Preview
@Composable
fun FriendListPreview() {
    val navController = rememberNavController()
    FriendList(navController)
}