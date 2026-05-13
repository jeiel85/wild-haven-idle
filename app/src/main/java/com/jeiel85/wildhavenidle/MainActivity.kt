package com.jeiel85.wildhavenidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import com.jeiel85.wildhavenidle.presentation.home.HomeScreen
import com.jeiel85.wildhavenidle.presentation.home.HomeUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animals = AnimalDefinitions.mvpAnimals
        val protectedAnimals = AnimalDefinitions.initialProtectedAnimals()
        val productionPerSecond = BalanceCalculator.calculateTotalProductionPerSecond(
            sanctuaryLevel = 1,
            protectedAnimals = protectedAnimals,
            definitions = animals,
        )

        setContent {
            WildHavenTheme {
                HomeScreen(
                    uiState = HomeUiState(
                        carePoint = 0.0,
                        sanctuaryLevel = 1,
                        productionPerSecond = productionPerSecond,
                        protectedAnimalCount = protectedAnimals.size,
                        discoveredAnimalCount = 1,
                        totalAnimalCount = animals.size,
                    ),
                )
            }
        }
    }
}
