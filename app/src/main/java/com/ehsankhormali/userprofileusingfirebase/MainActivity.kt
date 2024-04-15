package com.ehsankhormali.userprofileusingfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ehsankhormali.userprofileusingfirebase.navigation.Navigation
import com.ehsankhormali.userprofileusingfirebase.navigation.Screens
import com.ehsankhormali.userprofileusingfirebase.ui.theme.UserProfileUsingFirebaseTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserProfileUsingFirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileApp()
                }
            }
        }
    }
}

@Composable
fun ProfileApp(navController: NavHostController =rememberNavController()){
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val startingScreen =
        if (firebaseUser == null){
            Screens.Login.name
        }else{
            Screens.Profile.name
        }
    Navigation(navController = navController, startingScreen = startingScreen)
}

@Preview(showBackground = true)
@Composable
fun AppPreview(){
    ProfileApp()
}