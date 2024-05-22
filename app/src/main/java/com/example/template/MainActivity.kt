package com.example.template

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.template.ui.components.bottomnavigation.BottomNavigation
import com.example.template.ui.components.bottomnavigation.BottomNavigationItem
import com.example.template.ui.screens.alpha.AlphaScreen
import com.example.template.ui.screens.alpha.AlphaViewModel
import com.example.template.ui.screens.bravo.BravoScreen
import com.example.template.ui.screens.charlie.CharlieScreen
import com.example.template.ui.screens.delta.DeltaScreen
import com.example.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val alphaViewModel: AlphaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemplateTheme {
                MainScreen(alphaViewModel)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(alphaViewModel: AlphaViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        NavigationGraph(navController = navController, alphaViewModel = alphaViewModel)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, alphaViewModel: AlphaViewModel) {
    NavHost(navController, startDestination = BottomNavigationItem.Alpha.screen_route) {
        composable(BottomNavigationItem.Alpha.screen_route) {
            AlphaScreen(alphaViewModel)
        }
        composable(BottomNavigationItem.Bravo.screen_route) {
            BravoScreen()
        }
        composable(BottomNavigationItem.Charlie.screen_route) {
            CharlieScreen()
        }
        composable(BottomNavigationItem.Delta.screen_route) {
            DeltaScreen()
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TemplateTheme {
//        MainScreen()
//    }
//}