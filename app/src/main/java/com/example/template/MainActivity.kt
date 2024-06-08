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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.template.ui.components.bottomnavigation.BottomNavigation
import com.example.template.ui.components.bottomnavigation.BottomNavigationItem
import com.example.template.ui.screens.home.HomeScreen
import com.example.template.ui.screens.features.FeaturesScreen
import com.example.template.ui.screens.profile.ProfileScreen
import com.example.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.example.template.ui.screens.auth.AddInfoScreen
import com.example.template.ui.screens.auth.AuthHubScreen
import com.example.template.ui.screens.auth.CodeVerificationScreen
import com.example.template.ui.screens.auth.EnterPasswordScreen
import com.example.template.ui.screens.auth.NewPasswordScreen
import com.example.template.ui.screens.auth.ResetPasswordScreen
import com.example.template.ui.screens.editprofile.EditProfileScreen
import com.example.template.ui.screens.loginandsecurity.LoginAndSecurityScreen
import com.example.template.ui.screens.passwordresetsucess.PasswordResetSuccess
import com.example.template.ui.screens.publicprofile.PublicProfileScreen
import com.example.template.ui.screens.snag.SnagScreen
import com.example.template.ui.screens.stepsguide.StepsGuideScreen
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
                    sharedPreferences.edit().remove(Constants.SHARED_PREFERENCES_KEY_SUB).apply()
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
        startDestination = BottomNavigationItem.Alpha.screenRoute,
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
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Features.route) { FeaturesScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.LoginAndSecurity.route) { LoginAndSecurityScreen(navController) }
        composable(
            Screen.PublicProfile.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            PublicProfileScreen(
                navController,
                backStackEntry.arguments?.getString("username").toString()
            )
        }
        composable(Screen.StepsGuide.route) { StepsGuideScreen(navController) }
        composable(Screen.EditProfile.route) { EditProfileScreen(navController) }
        composable(Screen.AuthHub.route) { AuthHubScreen(navController) }
        composable(Screen.ResetPassword.route) { ResetPasswordScreen(navController) }
        composable(
            Screen.CodeVerification.route,
            arguments = listOf(
                navArgument("verificationType") { type = NavType.StringType },
                navArgument("username") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CodeVerificationScreen(
                navController,
                backStackEntry.arguments?.getString("verificationType").toString(),
                backStackEntry.arguments?.getString("username").toString(),
                backStackEntry.arguments?.getString("password").toString()
            )
        }
        composable(
            Screen.NewPassword.route,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("code") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            NewPasswordScreen(
                navController,
                backStackEntry.arguments?.getString("username").toString(),
                backStackEntry.arguments?.getString("code").toString()
            )
        }
        composable(
            Screen.EnterPassword.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            EnterPasswordScreen(
                navController,
                backStackEntry.arguments?.getString("username").toString()
            )
        }
        composable(
            Screen.AddNewUserInfo.route,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            AddInfoScreen(
                navController,
                backStackEntry.arguments?.getString("username").toString()
            )
        }
        composable(
            Screen.Snag.route,
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
        composable(Screen.ResetPasswordSuccess.route) {
            PasswordResetSuccess(
                navController = navController
            )
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