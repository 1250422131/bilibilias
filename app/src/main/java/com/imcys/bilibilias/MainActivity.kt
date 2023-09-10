package com.imcys.bilibilias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.imcys.bilibilias.base.router.LocalNavController
import com.imcys.bilibilias.base.router.Screen
import com.imcys.bilibilias.base.router.SplashRouter
import com.imcys.bilibilias.base.router.userNavHostGraph
import com.imcys.bilibilias.common.base.components.FullScreenScaffold
import com.imcys.bilibilias.splash.ui.Splash
import com.imcys.bilibilias.ui.authentication.AuthScreen
import com.imcys.bilibilias.ui.authentication.AuthenticationMethodScreen
import com.imcys.bilibilias.ui.download.Download
import com.imcys.bilibilias.ui.home.Home
import com.imcys.bilibilias.ui.theme.BILIBILIASTheme
import com.imcys.bilibilias.ui.tool.Tool
import com.imcys.bilibilias.ui.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BILIBILIASTheme {
                val navController = rememberNavController()
                NavHost(
                    navController,
                    startDestination = SplashRouter.App.route,
                    Modifier.statusBarsPadding()
                ) {
                    composable(SplashRouter.App.route) { Splash(navController) }
                    composable(SplashRouter.Screen.route) { Screen() }
                    composable(SplashRouter.AuthMethod.route) { AuthenticationMethodScreen(navController) }
                    composable(SplashRouter.AuthScreen.route) { AuthScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun Screen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    FullScreenScaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(tonalElevation = 0.dp, containerColor = MaterialTheme.colorScheme.onBackground) {
                items.forEach { screen ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painterResource(screen.icon),
                                contentDescription = null,
                                tint = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.inversePrimary
                                }
                            )
                        },
                        label = {
                            Text(
                                stringResource(screen.resourceId),
                                color = if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.inversePrimary
                                }
                            )
                        },
                        alwaysShowLabel = selected
                    )
                }
            }
        }
    ) { innerPadding ->
        // todo 添加网域层 https://developer.android.com/topic/architecture/domain-layer?hl=zh-cn
        CompositionLocalProvider(LocalNavController provides navController) {
            val userViewModel: UserViewModel = hiltViewModel()
            NavHost(
                navController,
                Screen.Home.route,
                Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) { Home() }
                composable(Screen.Tool.route) { Tool() }
                composable(Screen.Download.route) { Download() }
                userNavHostGraph(userViewModel)
            }
        }
    }
}

private val items = listOf(
    Screen.Home,
    Screen.Tool,
    Screen.Download,
    Screen.User,
)
