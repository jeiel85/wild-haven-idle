package com.jeiel85.wildhavenidle.presentation.components

object RecoveryStageLabel {
    fun of(stage: Int): String = when {
        stage >= 20 -> "보호 완료"
        stage >= 10 -> "적응"
        stage >= 5 -> "안정"
        else -> "구조 직후"
    }
}
