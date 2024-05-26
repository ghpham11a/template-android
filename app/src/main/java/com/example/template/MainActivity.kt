package com.example.template

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.example.template.ui.components.bottomnavigation.BottomNavigation
import com.example.template.ui.components.bottomnavigation.BottomNavigationItem
import com.example.template.ui.screens.alpha.AlphaScreen
import com.example.template.ui.screens.bravo.BravoScreen
import com.example.template.ui.screens.charlie.CharlieScreen
import com.example.template.ui.screens.delta.DeltaScreen
import com.example.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient
import com.amazonaws.services.cognitoidentityprovider.model.AdminGetUserRequest
import com.amazonaws.services.cognitoidentityprovider.model.AdminGetUserResult
import com.example.template.ui.screens.auth.AddInfoScreen
import com.example.template.ui.screens.auth.AuthScreen
import com.example.template.ui.screens.auth.CodeVerificationScreen
import com.example.template.ui.screens.auth.EnterPasswordScreen
import com.example.template.ui.screens.auth.EnterPasswordViewModel
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
                val sharedPrefsAuthKeyExists = sharedPreferences.contains(Constants.SHARED_PREFERENCES_KEY_AUTH_TOKEN)
                if (isUserStateSignedIn && !sharedPrefsAuthKeyExists) {
                    sharedPreferences.edit().remove(Constants.SHARED_PREFERENCES_KEY_AUTH_TOKEN).apply()
                }
                if (!isUserStateSignedIn) {
                    sharedPreferences.edit().remove(Constants.SHARED_PREFERENCES_KEY_AUTH_TOKEN).apply()
                }
            }
            override fun onError(e: Exception) {

            }
        })
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
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
        navigation(Constants.Route.BRAVO_LIST, route = Constants.Route.BRAVO_TAB) {
            composable(Constants.Route.BRAVO_LIST) { BravoScreen(navController) }
        }
        composable(BottomNavigationItem.Bravo.screen_route) {
            BravoScreen(navController)
        }
        composable(BottomNavigationItem.Charlie.screen_route) {
            CharlieScreen(navController)
        }
        composable(BottomNavigationItem.Delta.screen_route) {
            DeltaScreen(navController)
        }
        navigation(Constants.Route.AUTH_HUB, route = Constants.Route.AUTH) {
            composable(Constants.Route.AUTH_HUB) { AuthScreen(navController) }
            composable(Constants.Route.AUTH_CODE_VERIFICATION) { CodeVerificationScreen(navController) }
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