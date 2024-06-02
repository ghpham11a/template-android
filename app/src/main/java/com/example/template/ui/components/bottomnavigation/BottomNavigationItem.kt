package com.example.template.ui.components.bottomnavigation

import com.example.template.R
import com.example.template.utils.Constants

sealed class BottomNavigationItem(var title:String, var icon:Int, var screen_route:String){

    object Alpha : BottomNavigationItem("Home", R.drawable.ic_android_black_24dp, Constants.Route.HOME_TAB)
    object Bravo: BottomNavigationItem("Features",R.drawable.ic_android_black_24dp, Constants.Route.FEATURES_TAB)
    object Delta: BottomNavigationItem("Profile",R.drawable.ic_android_black_24dp, Constants.Route.PROFILE_TAB)
}