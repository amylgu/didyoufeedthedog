package com.example.didyoufeedthedog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

interface DogDestination {
    val icon: ImageVector
    val route: String
    val label: String
}

object Home : DogDestination {
    override val icon = Icons.Default.Home
    override val route = "home"
    override val label = "Home"
}

object Feedings : DogDestination {
    override val icon = Icons.Default.List
    override val route = "feedings"
    override val label = "Feedings"
}

// List of screens used in the process of determining the current screen
val dogTabRowScreens = listOf(Home, Feedings)