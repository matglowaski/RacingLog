package com.example.racinglog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.racinglog.ui.screens.auth.LoginScreen
import com.example.racinglog.ui.screens.auth.SignUpScreen
import com.example.racinglog.ui.screens.home.HomeScreen
import com.example.racinglog.ui.screens.register.RegisterScreen
import com.example.racinglog.ui.screens.analysis.AnalysisScreen
import com.example.racinglog.ui.screens.history.HistoryScreen
import com.example.racinglog.viewmodel.AuthViewModel
import com.example.racinglog.viewmodel.SimulatorViewModel
import com.example.racinglog.viewmodel.LapViewModel
import com.example.racinglog.viewmodel.HistoryViewModel
import com.example.racinglog.viewmodel.AnalysisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = hiltViewModel()
    val lapViewModel: LapViewModel = hiltViewModel()
    val historyViewModel: HistoryViewModel = hiltViewModel()
    val analysisViewModel: AnalysisViewModel = hiltViewModel()
    val simulatorViewModel: SimulatorViewModel = hiltViewModel()

    val isLoggedIn = authViewModel.isLoggedIn

    val screens = listOf(
        Screen.Home to "Início",
        Screen.Register to "Registrar",
        Screen.Analysis to "Análise",
        Screen.History to "Histórico"
    )

    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            if (isLoggedIn) {
                TopAppBar(
                    title = {
                        Text("RACING LOG")
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                authViewModel.signOut()
                                lapViewModel.clearEditingLap()

                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Home.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Text("SAIR")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isLoggedIn) {
                NavigationBar {
                    screens.forEach { (screen, title) ->
                        NavigationBarItem(
                            selected =
                                backStackEntry?.destination?.route == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Home.route) {
                                        saveState = false
                                    }
                                    launchSingleTop = true
                                    restoreState = false
                                }
                            },
                            icon = {
                                Text("•")
                            },
                            label = {
                                Text(title)
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) {
                Screen.Home.route
            } else {
                Screen.Login.route
            },
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    onCreateAccount = {
                        navController.navigate(Screen.SignUp.route)
                    },
                    viewModel = authViewModel
                )
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    },
                    viewModel = authViewModel
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(simulatorViewModel)
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    simulatorViewModel = simulatorViewModel,
                    lapViewModel = lapViewModel,
                    onSaveSuccess = {
                        historyViewModel.loadLaps()
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Analysis.route) {
                AnalysisScreen(
                    simulatorViewModel = simulatorViewModel,
                    analysisViewModel = analysisViewModel
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = historyViewModel,
                    onEditClick = { lap ->
                        lapViewModel.setLapToEdit(lap)
                        simulatorViewModel.selectSimulatorById(lap.simulatorId)

                        navController.navigate(Screen.Register.route)
                    }
                )
            }
        }
    }
}