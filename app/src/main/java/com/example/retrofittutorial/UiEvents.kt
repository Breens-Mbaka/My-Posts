package com.example.retrofittutorial

sealed class UiEvents {
    data class SnackBarEvent(val message: String) : UiEvents()
}