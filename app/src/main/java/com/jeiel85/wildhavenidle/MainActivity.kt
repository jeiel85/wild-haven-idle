package com.jeiel85.wildhavenidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.core.time.SystemTimeProvider
import com.jeiel85.wildhavenidle.data.local.GameStateDataStore
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import com.jeiel85.wildhavenidle.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val timeProvider = SystemTimeProvider
        val gameRepository = GameRepository(
            localDataStore = GameStateDataStore(
                context = this,
                defaultNowMillis = timeProvider::nowMillis,
            ),
            timeProvider = timeProvider,
        )

        setContent {
            WildHavenTheme {
                AppNavGraph(gameRepository = gameRepository)
            }
        }
    }
}
