package com.jeiel85.wildhavenidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.core.time.SystemTimeProvider
import com.jeiel85.wildhavenidle.data.local.GameStateDataStore
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import com.jeiel85.wildhavenidle.presentation.home.HomeScreen
import com.jeiel85.wildhavenidle.presentation.home.HomeViewModel
import com.jeiel85.wildhavenidle.presentation.home.HomeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val timeProvider = SystemTimeProvider
        val gameRepository = GameRepository(
            localDataStore = GameStateDataStore(
                context = this,
                defaultNowMillis = timeProvider::nowMillis,
            ),
            timeProvider = timeProvider,
        )
        val homeViewModelFactory = HomeViewModelFactory(gameRepository)

        setContent {
            WildHavenTheme {
                val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)
                HomeScreen(
                    viewModel = homeViewModel,
                )
            }
        }
    }
}
