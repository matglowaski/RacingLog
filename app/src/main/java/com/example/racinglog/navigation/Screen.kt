package com.example.racinglog.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Register : Screen("register")
    data object Analysis : Screen("analysis")
    data object History : Screen("history")
}
