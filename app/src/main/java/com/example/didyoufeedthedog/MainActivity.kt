package com.example.didyoufeedthedog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

@Composable
fun Greeting() {
    val time = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())
    val date = DateFormat.getDateInstance().format(Date())
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "It's $time on $date.")
        Text(text = "Did you feed the dog?")
        Button(
            onClick = {}
        ) {
            Text("I fed the dog!")
        }
    }
}

@Composable
fun Feeding(time: String, date: String) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
        Row (modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Dog fed at $time on $date.")
            }
        }
    }
}

@Composable
fun Feedings() {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            "Last five feedings:",
            modifier = Modifier.padding(all = 10.dp)
        )
        Feeding("8:00 AM", "Jul 21, 2022")
        Feeding("12:05 PM", "Jul 21, 2022")
        Feeding("12:05 PM", "Jul 21, 2022")
        Feeding("12:05 PM", "Jul 21, 2022")
        Feeding("12:05 PM", "Jul 21, 2022")
    }
}

@Composable
private fun BottomNavigation(modifier: Modifier = Modifier) {
    BottomNavigation(modifier) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text("Home")
            },
            selected = true,
            onClick = {}
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null
                )
            },
            label = {
                Text("History")
            },
            selected = false,
            onClick = {}
        )
    }
}

@Composable
fun DidYouFeedTheDogApp() {
    DidYouFeedTheDogTheme {
        Scaffold(
            bottomBar = { BottomNavigation() }
        ) {
            Greeting()
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun DefaultPreview() {
    DidYouFeedTheDogTheme {
        Greeting()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 620)
@Composable
fun FeedingsPreview() {
    DidYouFeedTheDogTheme {
        Feedings()
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationPreview() {
    DidYouFeedTheDogTheme {
        BottomNavigation()
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 620)
@Composable
fun DidYouFeedTheDogAppPreview() {
    DidYouFeedTheDogTheme {
        DidYouFeedTheDogApp()
    }
}