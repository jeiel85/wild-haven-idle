package com.jeiel85.wildhavenidle.data.model

data class ProtectedAnimal(
    val animalId: String,
    val recoveryStage: Int = 1,
    val protectedAt: Long = System.currentTimeMillis(),
)
