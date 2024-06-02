package com.example.template

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.template.ui.components.bottomnavigation.BottomNavigation
import com.example.template.ui.components.bottomnavigation.BottomNavigationItem
import com.example.template.ui.screens.alpha.AlphaScreen
import com.example.template.ui.screens.featurelist.FeatureListScreen
import com.example.template.ui.screens.delta.DeltaScreen
import com.example.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.example.template.ui.screens.auth.AddInfoScreen
import com.example.template.ui.screens.auth.AuthScreen
import com.example.template.ui.screens.auth.CodeVerificationScreen
import com.example.template.ui.screens.auth.EnterPasswordScreen
import com.example.template.ui.screens.snag.SnagScreen
import com.example.template.utils.Constants
import com.example.template.utils.Constants.BOTTOM_NAVIGATION_ROUTES

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TemplateTheme {
                MainScreen()
            }
        }

        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails> {
            override fun onResult(userStateDetails: UserStateDetails) {
                val sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                val isUserStateSignedIn = userStateDetails.userState == UserState.SIGNED_IN
                val sharedPrefsAuthKeyExists = sharedPreferences.contains(Constants.SHARED_PREFERENCES_KEY_ID_TOKEN)
                if ((isUserStateSignedIn && !sharedPrefsAuthKeyExists) || !isUserStateSignedIn) {
                    AWSMobileClient.getInstance().signOut()
                    sharedPreferences.edit().remove(Constants.SHARED_PREFERENCES_KEY_ID_TOKEN).apply()
                    sharedPreferences.edit().remove(Constants.SHARED_PREFERENCES_KEY_ACCESS_TOKEN).apply()
                    sharedPreferences.edit().remove(Constants.SHARED_PREFERENCES_KEY_USERNAME).apply()
                    sharedPreferences.edit().remove(Constants.SHARED_PREFERENCES_KEY_EXPIRATION_DATE).apply()
                }
            }
            override fun onError(e: Exception) {

            }
        })
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (BOTTOM_NAVIGATION_ROUTES.contains(currentRoute)) {
                BottomNavigation(navController = navController)
            }
        }
    ) {
        NavigationGraph(
            navController = navController,
            currentRoute = currentRoute ?: ""
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    currentRoute: String
) {
    NavHost(
        navController,
        startDestination = BottomNavigationItem.Alpha.screen_route,
        enterTransition = {
            if (BOTTOM_NAVIGATION_ROUTES.contains(currentRoute)) {
                EnterTransition.None
            } else {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(300)
                )
            }
        },
        exitTransition = {
            if (BOTTOM_NAVIGATION_ROUTES.contains(currentRoute)) {
                ExitTransition.None
            } else {
                slideOutHorizontally(
                    targetOffsetX = { 1000 },
                    animationSpec = tween(300)
                )
            }
        }
    ) {
        composable(BottomNavigationItem.Alpha.screen_route) {
            AlphaScreen(navController)
        }
        navigation(Constants.Route.FEATURES_LIST, route = Constants.Route.FEATURES_TAB) {
            composable(Constants.Route.FEATURES_LIST) { FeatureListScreen(navController) }
        }
        composable(BottomNavigationItem.Bravo.screen_route) {
            FeatureListScreen(navController)
        }
        composable(BottomNavigationItem.Delta.screen_route) {
            DeltaScreen(navController)
        }
        navigation(Constants.Route.AUTH_HUB, route = Constants.Route.AUTH) {
            composable(Constants.Route.AUTH_HUB) { AuthScreen(navController) }
            composable(
                String.format(Constants.Route.AUTH_CODE_VERIFICATION, "{username}", "{password}"),
                arguments = listOf(
                    navArgument("username") { type = NavType.StringType },
                    navArgument("password") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                CodeVerificationScreen(
                    navController,
                    backStackEntry.arguments?.getString("username").toString(),
                    backStackEntry.arguments?.getString("password").toString()
                )
            }
            composable(
                String.format(Constants.Route.AUTH_ENTER_PASSWORD, "{username}"),
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                EnterPasswordScreen(
                    navController,
                    backStackEntry.arguments?.getString("username").toString()
                )
            }
            composable(
                String.format(Constants.Route.AUTH_ADD_INFO, "{username}"),
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                AddInfoScreen(
                    navController,
                    backStackEntry.arguments?.getString("username").toString()
                )
            }
            composable(
                Constants.Route.SNAG,
                arguments = listOf(
                    navArgument("message") {
                        type = NavType.StringType
                        defaultValue = ""}
                )
            ) { backStackEntry ->
                SnagScreen(
                    navController = navController,
                    message = backStackEntry.arguments?.getString("message") ?: ""
                )
            }
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