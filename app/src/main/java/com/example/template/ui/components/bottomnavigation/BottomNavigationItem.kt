package com.example.template.ui.components.bottomnavigation

import com.example.template.R
import com.example.template.utils.Constants

sealed class BottomNavigationItem(var title:String, var icon:Int, var screen_route:String){

    object Alpha : BottomNavigationItem("Alpha", R.drawable.ic_android_black_24dp, Constants.Route.ALPHA_TAB)
    object Bravo: BottomNavigationItem("Bravo",R.drawable.ic_android_black_24dp, Constants.Route.BRAVO_TAB)
    object Charlie: BottomNavigationItem("Charlie",R.drawable.ic_android_black_24dp, Constants.Route.CHARLIE_TAB)
    object Delta: BottomNavigationItem("Delta",R.drawable.ic_android_black_24dp, Constants.Route.DELTA_TAB)
}