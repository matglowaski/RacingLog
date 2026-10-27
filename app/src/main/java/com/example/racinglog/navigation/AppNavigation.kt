package com.example.racinglog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.racinglog.ui.screens.home.HomeScreen
import com.example.racinglog.ui.screens.register.RegisterScreen
import com.example.racinglog.ui.screens.analysis.AnalysisScreen
import com.example.racinglog.ui.screens.history.HistoryScreen
import com.example.racinglog.viewmodel.SimulatorViewModel
import com.example.racinglog.viewmodel.LapViewModel
import com.example.racinglog.viewmodel.HistoryViewModel
import com.example.racinglog.viewmodel.AnalysisViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val lapViewModel: LapViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val analysisViewModel: AnalysisViewModel = hiltViewModel()
    val simulatorViewModel: SimulatorViewModel = hiltViewModel()

    val screens = listOf(
        Screen.Home to "Início",
        Screen.Register to "Registrar",
        Screen.Analysis to "Análise",
        Screen.History to "Histórico"
    )

    val backStackEntry by
    navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { (screen, title) ->
                    NavigationBarItem(
                        selected =
                            backStackEntry?.destination?.route ==
                                    screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        },
                        icon = { Text("•") },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(simulatorViewModel)
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    simulatorViewModel = simulatorViewModel,
                    lapViewModel = lapViewModel
                )
            }

            composable(Screen.Analysis.route) {
                AnalysisScreen(
                    simulatorViewModel = simulatorViewModel,
                    analysisViewModel = analysisViewModel
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(historyViewModel)
            }
        }
    }
}

