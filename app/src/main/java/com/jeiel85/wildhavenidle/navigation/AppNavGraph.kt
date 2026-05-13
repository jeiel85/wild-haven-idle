package com.jeiel85.wildhavenidle.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import com.jeiel85.wildhavenidle.presentation.animals.AnimalListScreen
import com.jeiel85.wildhavenidle.presentation.animals.AnimalListViewModelFactory
import com.jeiel85.wildhavenidle.presentation.archive.ArchiveScreen
import com.jeiel85.wildhavenidle.presentation.archive.ArchiveViewModelFactory
import com.jeiel85.wildhavenidle.presentation.home.HomeScreen
import com.jeiel85.wildhavenidle.presentation.home.HomeViewModelFactory
import com.jeiel85.wildhavenidle.presentation.settings.SettingsScreen

@Composable
fun AppNavGraph(gameRepository: GameRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel(factory = HomeViewModelFactory(gameRepository)),
                onNavigateToAnimals = { navController.navigate(Routes.ANIMALS) },
                onNavigateToArchive = { navController.navigate(Routes.ARCHIVE) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
            )
        }
        composable(Routes.ANIMALS) {
            AnimalListScreen(
                viewModel = viewModel(factory = AnimalListViewModelFactory(gameRepository)),
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Routes.ARCHIVE) {
            ArchiveScreen(
                viewModel = viewModel(factory = ArchiveViewModelFactory(gameRepository)),
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                gameRepository = gameRepository,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
