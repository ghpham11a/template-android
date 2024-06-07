package com.example.template.ui.components.bottomnavigation

import com.example.template.R
import com.example.template.Screen

sealed class BottomNavigationItem(var title:String, var icon:Int, var screenRoute:String){

    object Alpha : BottomNavigationItem("Home", R.drawable.ic_android_black_24dp, Screen.Home.route)
    object Bravo: BottomNavigationItem("Features",R.drawable.ic_android_black_24dp, Screen.Features.route)
    object Delta: BottomNavigationItem("Profile",R.drawable.ic_android_black_24dp, Screen.Profile.route)
}