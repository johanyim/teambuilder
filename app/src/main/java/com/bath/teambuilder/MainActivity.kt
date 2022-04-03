package com.bath.teambuilder

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bath.teambuilder.ui.screens.ExploreScreenBody
import com.bath.teambuilder.ui.screens.LoginScreenBody
import com.bath.teambuilder.ui.theme.TeambuilderTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeambuilderTheme {
                App()
            }
        }
    }
}
enum class BottomRoutes(val icon: ImageVector) {
    Explore(icon = Icons.Outlined.Explore),
    Search(icon = Icons.Outlined.Search),
    AllGroups(icon = Icons.Outlined.Groups)
}

@Composable
private fun App() {
    val navController = rememberNavController()
    /*
    The NavController is the central component when using Navigation in Compose;
    it keeps track of back stack entries, moves the stack forward,
    enables back stack manipulation, and navigating between screen states.
    Because NavController is central to navigation it has to be created first in order to navigate to destinations.
     */
    var hasLoggedIn by rememberSaveable { mutableStateOf<Boolean>(true)}

    if(!hasLoggedIn) {
        LoginScreenBody(onLoginClick = {hasLoggedIn=true})
    }
    else {
        Scaffold(
            bottomBar = {
                BottomNavigation(navController = navController)
            }
        ) {
            MainNavHost(navController = navController)
        }
    }
}

/*
Main Bottom Navigation Bar logic.
Create Bottom Navigation Bar with icons and destinations
Code adapted from official docs : https://developer.android.com/jetpack/compose/navigation#bottom-nav
 */
@Composable
fun BottomNavigation(navController: NavController) {
    val items = BottomRoutes.values().toList()
    BottomNavigation(
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState() //get top item in nav stack
        val currentRoute = navBackStackEntry?.destination?.route //gets its name if not null
        Log.d("BottomNavigation", "currentRoute is : $currentRoute")
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.name) }, // add icon defined in enum class
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f), //mute color if item not selected.
                alwaysShowLabel = true,
                selected = currentRoute?.startsWith(item.name) ?: false, //assert whether current composable is selected to change black color
                //startsWith is used to match inner pages as well e.g. "FakeCall/{scenario_name}"
                onClick = {
                    //navigate to clicked composable
                    Log.d("BottomNavigation", "item_name is : ${item.name}")


                    //Go back when user clicks the bottom navigation icon of pageX whilst being on inner screen pageX/{<args>}
                    if (currentRoute?.substringBefore(delimiter = '/', missingDelimiterValue = "").equals(item.name)) { //default to empty string if '/' not found
                        navController.navigate(item.name) {
                            popUpTo(BottomRoutes.Search.name)
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(item.name) {
                            popUpTo(BottomRoutes.Search.name) { saveState = true }

                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true

                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController, modifier : Modifier = Modifier) {
    NavHost(navController = navController, startDestination = BottomRoutes.Search.name, modifier = modifier) {
        //Navigation Graph goes here as trailing lambda.
        //When using Navigation within Compose, routes are represented as strings.

        composable(route = BottomRoutes.Explore.name) {
            //Text(text = "All Venues screen")
            ExploreScreenBody() {}
        }

        composable(route = BottomRoutes.Search.name) {
            Text(text = "Search Screen")
        }

        composable(route = BottomRoutes.AllGroups.name) {
            Text(text = "All Groups screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TeambuilderTheme {
        App()
    }
}
