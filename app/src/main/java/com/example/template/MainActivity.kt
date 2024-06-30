package com.example.template

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.template.ui.screens.home.HomeScreen
import com.example.template.ui.screens.features.FeaturesScreen
import com.example.template.ui.screens.profile.ProfileScreen
import com.example.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.example.template.ui.screens.addpayout.AddPayoutScreen
import com.example.template.ui.screens.auth.AddInfoScreen
import com.example.template.ui.screens.auth.AuthHubScreen
import com.example.template.ui.screens.auth.CodeVerificationScreen
import com.example.template.ui.screens.auth.EnterPasswordScreen
import com.example.template.ui.screens.auth.NewPasswordScreen
import com.example.template.ui.screens.auth.ResetPasswordScreen
import com.example.template.ui.screens.editprofile.EditProfileScreen
import com.example.template.ui.screens.filterlist.FilterListScreen
import com.example.template.ui.screens.loginandsecurity.LoginAndSecurityScreen
import com.example.template.ui.screens.map.MapScreen
import com.example.template.ui.screens.passwordresetsucess.PasswordResetSuccess
import com.example.template.ui.screens.paymentshub.PaymentMethodsScreen
import com.example.template.ui.screens.paymentshub.PaymentsHubScreen
import com.example.template.ui.screens.paymentshub.PayoutMethodsScreen
import com.example.template.ui.screens.paymentshub.YourPaymentsScreen
import com.example.template.ui.screens.personalinfo.PersonalInfoScreen
import com.example.template.ui.screens.publicprofile.PublicProfileScreen
import com.example.template.ui.screens.snag.SnagScreen
import com.example.template.ui.screens.thing.ThingScreen
import com.example.template.ui.screens.thing.ThingBuilderScreen
import com.example.template.ui.screens.thing.ThingIntroScreen
import com.example.template.ui.screens.thinglist.ThingListScreen
import com.example.template.ui.screens.xmlview.XMLViewScreen
import com.example.template.utils.Constants
import com.example.template.utils.Constants.BOTTOM_NAVIGATION_ROUTES
import com.example.template.utils.removeValues

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            TemplateTheme(darkTheme = false) {
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
                    sharedPreferences.removeValues()
                }
            }
            override fun onError(e: Exception) {

            }
        })
    }
}

sealed class BottomNavigationItem(var title:String, var icon:Int, var screenRoute:String){
    object Alpha : BottomNavigationItem("Home", R.drawable.ic_android_black_24dp, Screen.Home.route)
    object Bravo: BottomNavigationItem("Features",R.drawable.ic_android_black_24dp, Screen.Features.route)
    object Delta: BottomNavigationItem("Profile",R.drawable.ic_android_black_24dp, Screen.Profile.route)
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.Alpha,
        BottomNavigationItem.Bravo,
        BottomNavigationItem.Delta
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = true,
                label = {
                    Text(item.title)
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = item.title)
                }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted
        } else {
            // Handle permission denial
        }
    }

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
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            PublicProfileScreen(
                navController,
                backStackEntry.arguments?.getString("userId").toString()
            )
        }
        composable(
            Screen.Thing.route,
            arguments = listOf(
                navArgument("thingId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ThingScreen(
                navController,
                backStackEntry.arguments?.getString("thingId").toString()
            )
        }
        composable(Screen.ThingIntro.route) { ThingIntroScreen(navController) }
        composable(Screen.ThingList.route) { ThingListScreen(navController) }
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
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            AddInfoScreen(
                navController,
                backStackEntry.arguments?.getString("username").toString(),
                backStackEntry.arguments?.getString("phoneNumber").toString()
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
        composable(
            Screen.ThingBuilder.route,
            arguments = listOf(
                navArgument("thingId") { type = NavType.StringType },
                navArgument("action") { defaultValue = "" },
                navArgument("mode") { defaultValue = "" },
                navArgument("steps") { defaultValue = "" }
            )
        ) { backStackEntry ->
            ThingBuilderScreen(
                navController,
                backStackEntry.arguments?.getString("thingId") ?: "NULL",
                backStackEntry.arguments?.getString("action") ?: "CREATE",
                backStackEntry.arguments?.getString("mode") ?: "SHEET",
                backStackEntry.arguments?.getString("steps") ?: "",
                closeButton = {
                    TextButton(
                        onClick = {
                            navController.navigateUp()
                        },
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            )
        }
        composable(Screen.FilterList.route) {
            FilterListScreen(navController)
        }
        composable(Screen.PersonalInfo.route) {
            PersonalInfoScreen(navController)
        }
        composable(Screen.PaymentsHub.route) {
            PaymentsHubScreen(navController)
        }
        composable(Screen.PaymentMethods.route) {
            PaymentMethodsScreen(navController)
        }
        composable(Screen.YourPayments.route) {
            YourPaymentsScreen(navController)
        }
        composable(Screen.PayoutMethods.route) {
            PayoutMethodsScreen(navController)
        }
        composable(Screen.XMLView.route) { XMLViewScreen(navController) }
        composable(Screen.Map.route) { MapScreen(navController) }
        composable(Screen.PaymentMethods.route) { PaymentMethodsScreen(navController) }
        composable(Screen.PayoutMethods.route) { PayoutMethodsScreen(navController) }
        composable(Screen.AddPayout.route) { AddPayoutScreen(navController) }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TemplateTheme {
//        MainScreen()
//    }
//}