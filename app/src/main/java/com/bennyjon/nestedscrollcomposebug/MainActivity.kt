package com.bennyjon.nestedscrollcomposebug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bennyjon.nestedscrollcomposebug.ui.theme.NestedScrollComposeBugTheme
import com.bennyjon.nestedscrollcomposebug.utils.focusableBorder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestedScrollComposeBugTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainNavHost()
                }
            }
        }
    }

    @Composable
    fun MainNavHost(
        modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController()
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = "home"
        ) {
            composable(route = "home") {
                HomeScreen(
                    examples = ExamplesDestinations.values().map { ExampleData(it.text, it.name) },
                    navHostController = navController
                )
            }

            for (destination in ExamplesDestinations.values()) {
                composable(route = destination.name) {
                    destination.screen()
                }
            }
        }
    }

    data class ExampleData(val text: String, val destination: String)

    @Composable
    fun HomeScreen(examples: List<ExampleData>, navHostController: NavHostController) {
        val commonModifier = Modifier.padding(8.dp)

        Column(
            commonModifier
                .verticalScroll(rememberScrollState())
                ) {
            for (example in examples) {
                Button(
                    modifier = commonModifier.fillMaxWidth().focusableBorder(),
                    onClick = { navHostController.navigate(route = example.destination) }) {
                    Text(text = example.text)
                }
            }
        }
    }
}
