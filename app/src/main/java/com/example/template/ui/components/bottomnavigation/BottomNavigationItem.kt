package com.example.template.ui.components.bottomnavigation

import com.example.template.R

sealed class BottomNavigationItem(var title:String, var icon:Int, var screen_route:String){

    object Alpha : BottomNavigationItem("Alpha", R.drawable.ic_android_black_24dp,"home")
    object Bravo: BottomNavigationItem("Bravo",R.drawable.ic_android_black_24dp,"my_network")
    object Charlie: BottomNavigationItem("Charlie",R.drawable.ic_android_black_24dp,"add_post")
    object Delta: BottomNavigationItem("Delta",R.drawable.ic_android_black_24dp,"notification")
}