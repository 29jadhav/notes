package com.vivek.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vivek.notes.constants.RouteConsts
import com.vivek.notes.features.add_note_feature.presentation.AddNoteScreen
import com.vivek.notes.features.home_feature.presentation.HomeScreen
import com.vivek.notes.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SetNavigation()
            }
        }
    }
}


@Composable
fun SetNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = RouteConsts.HOME) {
        composable(route = RouteConsts.HOME) {
            HomeScreen(onAddNoteClick = { noteId ->
                navController.navigate("${RouteConsts.ADD_NOTE}/$noteId")
            })
        }
        composable(
            route = RouteConsts.ADD_NOTE + "/{id}",
            arguments = listOf(navArgument("id") {
                this.type = NavType.IntType
                this.defaultValue = -1
            })
        ) {

            AddNoteScreen(navigateToBack = {
                navController.popBackStack()
            })
        }
    }
}

