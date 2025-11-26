package com.example.gengolearning.model

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Text
import com.example.gengolearning.ui.theme.BgBlue
import com.example.gengolearning.ui.theme.White
import com.gengolearning.app.R

@Composable
fun BottomNavBar(navController: NavController) {

    val items= listOf(
        BottomNavBarItems(stringResource(R.string.home_button), "home", R.drawable.outline_home_24),
        BottomNavBarItems(stringResource(R.string.learning_button), "learning", R.drawable.book),
        BottomNavBarItems(stringResource(R.string.setting_button), "settings", R.drawable.outline_settings_24)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute= navBackStackEntry?.destination?.route


    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
    )
        {
    NavigationBar(containerColor = BgBlue) {
        items.forEach { item->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {saveState= true}
                        launchSingleTop= true
                        restoreState= true
                    }
                },
                icon= { Icon(painter = painterResource(id = item.icon), contentDescription = item.title, tint = White)},
                label =  {
                    if (currentRoute == item.route)
                    Text( item.title,
                    color= White)},
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = BgBlue
                )
            )
        }
    }
    }

}