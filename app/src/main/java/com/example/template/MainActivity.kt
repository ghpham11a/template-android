package com.example.template

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.template.ui.screens.features.FeaturesScreen
import com.example.template.ui.screens.profile.ProfileScreen
import com.example.template.ui.theme.TemplateTheme
import dagger.hilt.android.AndroidEntryPoint
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.example.template.ui.screens.addpayout.AddBankInfoScreen
import com.example.template.ui.screens.addpayout.AddPayoutScreen
import com.example.template.ui.screens.auth.AddInfoScreen
import com.example.template.ui.screens.auth.AuthHubScreen
import com.example.template.ui.screens.auth.CodeVerificationScreen
import com.example.template.ui.screens.auth.EnterPasswordScreen
import com.example.template.ui.screens.auth.NewPasswordScreen
import com.example.template.ui.screens.auth.ResetPasswordScreen
import com.example.template.ui.screens.availability.AvailabilityScreen
import com.example.template.ui.screens.chathub.ChatHubScreen
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
import com.example.template.ui.screens.proxycallhub.ProxyCallHubScreen
import com.example.template.ui.screens.publicprofile.PublicProfileScreen
import com.example.template.ui.screens.schedulerhub.ConflictsScreen
import com.example.template.ui.screens.schedulerhub.SchedulerHubScreen
import com.example.template.ui.screens.schedulerhub.SchedulerScreen
import com.example.template.ui.screens.sendpaymenthub.PaymentAmountScreen
import com.example.template.ui.screens.sendpaymenthub.SendPaymentHubScreen
import com.example.template.ui.screens.snag.SnagScreen
import com.example.template.ui.screens.tabbedlist.TabbedListScreen
import com.example.template.ui.screens.thing.ThingScreen
import com.example.template.ui.screens.thing.ThingBuilderScreen
import com.example.template.ui.screens.thing.ThingIntroScreen
import com.example.template.ui.screens.thinglist.ThingListScreen
import com.example.template.ui.screens.videocallhub.VideoCallHubScreen
import com.example.template.ui.screens.videocallhub.VideoCallScreen
import com.example.template.ui.screens.voicecallhub.VoiceCallHubScreen
import com.example.template.ui.screens.voicecallhub.VoiceCallScreen
import com.example.template.ui.screens.xmlview.XMLViewScreen
import com.example.template.utils.Constants
import com.example.template.utils.Constants.BOTTOM_NAVIGATION_ROUTES
import com.example.template.utils.removeValues
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            TemplateTheme(darkTheme = false) {
                MainScreen(navController)
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
                Log.d("AWSMobileClient", "Error: $e")
            }
        })

        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun handleIntent(intent: Intent) {
        val data = intent.data
        data?.let {
            val pathSegments = it.pathSegments
            val queryParameterNames = it.queryParameterNames
            GlobalScope.launch(Dispatchers.Main) {
                delay(1000)
                // normal route
                // navController.navigate("")
                // tab route
                // navController.navigate("") {
                //     navController.graph.startDestinationRoute?.let { screen_route ->
                //         popUpTo(screen_route) {
                //             saveState = true
                //         }
                //     }
                //     launchSingleTop = true
                //     restoreState = true
                // }
            }
        }
    }
}

sealed class BottomNavigationItem(var title:String, var icon:Int, var screenRoute:String){
    object Features: BottomNavigationItem("Features",R.drawable.ic_android_black_24dp, Screen.Features.route)
    object Profile: BottomNavigationItem("Profile",R.drawable.ic_android_black_24dp, Screen.Profile.route)
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavigationItem.Features,
        BottomNavigationItem.Profile
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
fun MainScreen(navController: NavHostController) {

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
        startDestination = BottomNavigationItem.Features.screenRoute,
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
        composable(
            Screen.AddBankInfo.route,
            arguments = listOf(
                navArgument("country") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            AddBankInfoScreen(
                navController,
                backStackEntry.arguments?.getString("country").toString()
            )
        }
        composable(Screen.Availability.route) {
            AvailabilityScreen(navController)
        }
        composable(Screen.TabbedList.route) {
            TabbedListScreen(navController = navController)
        }
        composable(Screen.SendPaymentHub.route) {
            SendPaymentHubScreen(navController = navController)
        }
        composable(
            Screen.PaymentAmount.route,
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            PaymentAmountScreen(
                navController,
                backStackEntry.arguments?.getString("accountId").toString()
            )
        }
        composable(Screen.ProxyCallHub.route) {
            ProxyCallHubScreen(navController = navController)
        }
        composable(Screen.ChatHub.route) {
            ChatHubScreen(navController = navController)
        }
        composable(Screen.VoiceCallHub.route) {
            VoiceCallHubScreen(navController = navController)
        }
        composable(
            Screen.VoiceCall.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            VoiceCallScreen(
                navController,
                backStackEntry.arguments?.getString("id").toString()
            )
        }
        composable(Screen.VideoCallHub.route) {
            VideoCallHubScreen(navController = navController)
        }
        composable(
            Screen.VideoCall.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            VideoCallScreen(
                navController,
                backStackEntry.arguments?.getString("id").toString()
            )
        }
        composable(Screen.SchedulerHub.route) {
            SchedulerHubScreen(navController = navController)
        }
        composable(
            Screen.Scheduler.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("availabilityType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            SchedulerScreen(
                navController,
                backStackEntry.arguments?.getString("userId").toString(),
                backStackEntry.arguments?.getString("availabilityType").toString()
            )
        }
        composable(
            Screen.Conflicts.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("availabilityType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ConflictsScreen(
                navController,
                backStackEntry.arguments?.getString("userId").toString(),
                backStackEntry.arguments?.getString("availabilityType").toString()
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