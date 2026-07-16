package com.example.gengolearning.model

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.components.global.NavBarAnimatedIcon
import com.example.gengolearning.ui.features.navigation.Route
import com.example.gengolearning.ui.theme.AppColorTheme
import com.example.gengolearning.ui.theme.BgBlue
import com.gengolearning.app.R

@Composable
fun BottomNavBar(navController: NavController,
                 colorTheme: AppColorTheme) {

    val items= listOf(
        BottomNavBarItems(stringResource(R.string.home_button), Route.Home, R.drawable.home,
            animation =  when (colorTheme) {
                 AppColorTheme.SUNSET -> R.raw.home_sunset
                 AppColorTheme.MIDNIGHT_TEAL -> R.raw.home_teal
                 AppColorTheme.Autumn -> R.raw.home_autumn
                else -> R.raw.home
            }),
        BottomNavBarItems(stringResource(R.string.learning_button), Route.GrammarList, R.drawable.study,
            animation =  when (colorTheme) {
                AppColorTheme.SUNSET -> R.raw.book_sunset
                AppColorTheme.MIDNIGHT_TEAL -> R.raw.book_teal
                AppColorTheme.Autumn -> R.raw.book_autumn
                else -> R.raw.book
            }),
        BottomNavBarItems(stringResource(R.string.setting_button), Route.Settings, R.drawable.settings,
            animation =  when (colorTheme) {
                AppColorTheme.SUNSET -> R.raw.settings_sunset
                AppColorTheme.MIDNIGHT_TEAL -> R.raw.settings_teal
                AppColorTheme.Autumn -> R.raw.settings_autumn
                else -> R.raw.settings
            })
    )



    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute= navBackStackEntry?.destination


    Surface (modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp, bottom = 20.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 15.dp,
        color = MaterialTheme.colorScheme.background
    )
        {
    NavigationBar(containerColor = MaterialTheme.colorScheme.background,
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
                icon= {

//                    Image(painter = painterResource(id = item.icon),
//                    contentDescription = item.title,
//                    modifier = Modifier
//                        .size(30.dp))

                    NavBarAnimatedIcon(
                        itemPath = item.animation,
                        isSelected = currentRoute?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true
                    )



                      },
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
    BottomNavBar(navController = NavController(LocalContext.current), colorTheme = AppColorTheme.SUNSET)
}