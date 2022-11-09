package com.example.didyoufeedthedog

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.TextClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.didyoufeedthedog.ui.theme.DidYouFeedTheDogTheme
import java.text.DateFormat
import java.util.*
import androidx.compose.material.Scaffold
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.didyoufeedthedog.DogDestination
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

lateinit var context: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext

        setContent {
            DidYouFeedTheDogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DidYouFeedTheDogApp()
                }
            }
        }
    }

    private fun saveState() {
        runBlocking {
            val viewModel by viewModels<FeedingsViewModel>()
            applicationContext.dataStore.edit { feedings ->
                feedings[stringSetPreferencesKey("feedings")] = viewModel.state.feedings.toSet()
            }
        }
    }
    override fun onStop() {
        saveState()
        super.onStop()
    }
    override fun onDestroy() {
        saveState()
        super.onDestroy()
    }

}

@Composable
fun Greeting(
    onButtonSelected: () -> Unit
) {
    //val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())
    val date = DateFormat.getDateInstance().format(Date())
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "It's")
        displayTextClock()
        Text(text = "on $date", modifier = Modifier.padding(bottom = 20.dp))
        Text(text = "Did you feed the dog?")
        Button(
            onClick = {
                onButtonSelected()
            }
        ) {
            Text("I fed the dog!")
        }
    }
}

@Composable
fun displayTextClock() {
    AndroidView(
        factory = { context ->
            TextClock(context).apply {
                // on below line we are setting 12 hour format.
                format12Hour?.let { this.format12Hour = "hh:mm a" }
                // on below line we are setting time zone.
                timeZone?.let { this.timeZone = it }
                // on below line we are setting text size.
                textSize.let { this.textSize = 30f }
            }
        }
    )
}

@Composable
fun Feeding(feedingText: String) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Row (modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(feedingText)
            }
        }
    }
}

@Composable
fun Feedings(
    feedingTextList: MutableList<String>
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            "Last five feedings:",
            modifier = Modifier.padding(all = 10.dp)
        )
        feedingTextList.forEach {feedingText ->
            Feeding(feedingText)
        }
    }
}

@Composable
private fun BottomNavigation(
    allScreens: List<DogDestination>,
    onTabSelected: (DogDestination) -> Unit,
    currentScreen: DogDestination,
    modifier: Modifier = Modifier
) {
    BottomNavigation(modifier) {
        allScreens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null
                    )
                },
                label = { Text(screen.label) },
                selected = currentScreen == screen,
                onClick = {
                    onTabSelected(screen)
                }

            )
        }
    }
}

@Composable
fun DidYouFeedTheDogApp() {
    DidYouFeedTheDogTheme {
        val navController = rememberNavController()
        // These are needed to highlight the correct tab in the bottom navigation.
        val currentBackStack by navController.currentBackStackEntryAsState()
        // Fetch your currentDestination:
        val currentDestination = currentBackStack?.destination
        // Change the variable to this and use Overview as a backup screen if this returns null
        val currentScreen = dogTabRowScreens.find { it.route == currentDestination?.route } ?: Home

        val viewModel = viewModel<FeedingsViewModel>()
        val state = viewModel.state

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    allScreens = dogTabRowScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Home.route) {
                    Greeting(onButtonSelected = {
                        // Increment feedings
                        viewModel.viewModelScope.launch {
                            viewModel.addFeeding(context, state.feedings)
                        }
                        // Navigate to feeding log page
                        navController.navigateSingleTopTo(Feedings.route)
                    })
                }
                composable(route = Feedings.route) {
                    Feedings(state.feedings)
                }
            }
        }
    }
}

// This version of the navigate() function ensures that there aren't multiple copies of the same
// page in the stack trace if you click the same tab multiple times in a row.
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun DefaultPreview() {
    DidYouFeedTheDogTheme {
        Greeting(onButtonSelected = {})
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 620)
@Composable
fun FeedingsPreview() {
    DidYouFeedTheDogTheme {
        Feedings(mutableListOf())
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    DidYouFeedTheDogTheme {
        BottomNavigation(
            allScreens = dogTabRowScreens,
            onTabSelected = {},
            currentScreen = Home
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 620)
@Composable
fun DidYouFeedTheDogAppPreview() {
    DidYouFeedTheDogTheme {
        DidYouFeedTheDogApp()
    }
}