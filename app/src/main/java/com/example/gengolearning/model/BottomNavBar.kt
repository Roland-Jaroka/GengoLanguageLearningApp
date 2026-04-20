package com.example.gengolearning.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun BottomNavBar(navController: NavController) {

    val items= listOf(
        BottomNavBarItems(stringResource(R.string.home_button), Route.Home, R.drawable.home),
        BottomNavBarItems(stringResource(R.string.learning_button), Route.GrammarList, R.drawable.study),
        BottomNavBarItems(stringResource(R.string.setting_button), Route.Settings, R.drawable.settings)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute= navBackStackEntry?.destination


    Surface (modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp, bottom = 20.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 15.dp,
    )
        {
    NavigationBar(containerColor = White,
        modifier = Modifier) {
        items.forEach { item->
            NavigationBarItem(
                selected = false,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {saveState= true}
                        launchSingleTop= true
                        restoreState= true
                    }
                },
                icon= { Image(painter = painterResource(id = item.icon),
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(30.dp))},
                label =  {
                    if (currentRoute?.hierarchy?.any{
                        it.hasRoute(item.route::class)
                        } == true)
                    Text( item.title,
                    color= BgBlue)},
            )
        }
    }
    }

}

@Preview
@Composable
private fun Preview() {
    BottomNavBar(navController = NavController(LocalContext.current))
}