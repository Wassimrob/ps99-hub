package com.pixelmon.utm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pixelmon.utm.features.team.TeamManagerScreen
import com.pixelmon.utm.features.tournament.TournamentScreen
import com.pixelmon.utm.ui.theme.PixelmonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PixelmonTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppScaffold(navController = navController)
                }
            }
        }
    }
}

private enum class AppDestination(val route: String, val label: String) {
    HOME("home", "Home"),
    TOURNAMENT("tournament", "Tournament"),
    TEAM_MANAGER("team", "UTM")
}

@Composable
private fun AppScaffold(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.HOME.route
        ) {
            composable(AppDestination.HOME.route) {
                HomeScreen()
            }
            composable(AppDestination.TOURNAMENT.route) {
                TournamentScreen(paddingValues)
            }
            composable(AppDestination.TEAM_MANAGER.route) {
                TeamManagerScreen(paddingValues)
            }
        }
    }
}

@Composable
private fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        AppDestination.HOME,
        AppDestination.TOURNAMENT,
        AppDestination.TEAM_MANAGER
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { dest ->
            NavigationBarItem(
                selected = currentDestination?.route == dest.route,
                onClick = {
                    navController.navigate(dest.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    // Placeholder icons using built-in android resources are not directly available in Compose
                    // You can add custom icons in res/drawable and reference them here
                    Icon(painter = painterResource(android.R.drawable.star_big_on), contentDescription = dest.label)
                },
                label = { Text(dest.label) }
            )
        }
    }
}

@Composable
private fun HomeScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Text(
            text = "Pixelmon-inspired UI\nRandom Tournament + UTM (Team Manager)",
            style = MaterialTheme.typography.titleMedium
        )
    }
}