package com.ehsankhormali.userprofileusingfirebase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ehsankhormali.userprofileusingfirebase.screens.Login
import com.ehsankhormali.userprofileusingfirebase.screens.Profile
import com.ehsankhormali.userprofileusingfirebase.screens.Register

@Composable
fun Navigation(navController:NavHostController,startingScreen:String){

    NavHost(navController = navController, startDestination = startingScreen){

        composable(Screens.Profile.name){
            Profile(navController = navController)
        }

        composable(Screens.Login.name){
            Login(navController = navController)
        }

        composable(Screens.Register.name){
            Register(navController = navController)
        }
    }

}