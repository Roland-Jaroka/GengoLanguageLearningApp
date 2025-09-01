package com.example.mylanguagelearningapp.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.Text
import com.example.mylanguagelearningapp.R
import com.example.mylanguagelearningapp.ui.theme.BgBlue
import com.example.mylanguagelearningapp.ui.theme.Blue
import com.example.mylanguagelearningapp.ui.theme.White
import com.google.android.gms.common.SignInButton
import com.google.firebase.database.collection.LLRBNode

@Composable
fun BottomNavBar(navController: NavController) {

    val items= listOf(
        BottomNavBarItems("Home", "home", R.drawable.outline_home_24),
        BottomNavBarItems("Learning", "learning", R.drawable.book),
        BottomNavBarItems("Settings", "settings", R.drawable.outline_settings_24)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute= navBackStackEntry?.destination?.route


    Box(modifier = Modifier
        .fillMaxWidth()
        .background(White)
        .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
        ,
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