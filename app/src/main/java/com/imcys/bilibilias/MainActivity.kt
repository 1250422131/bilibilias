package com.imcys.bilibilias

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.imcys.bilibilias.base.router.Screen
import com.imcys.bilibilias.ui.authentication.login.loginAuthRoute
import com.imcys.bilibilias.ui.authentication.login.navigateToLoginAuth
import com.imcys.bilibilias.ui.authentication.method.authMethodRoute
import com.imcys.bilibilias.ui.authentication.method.navigateToAuthMethod
import com.imcys.bilibilias.ui.home.ROUTE_HOME
import com.imcys.bilibilias.ui.home.navigateToHome
import com.imcys.bilibilias.ui.splash.ROUTE_SPLASH
import com.imcys.bilibilias.ui.splash.splashRoute
import com.imcys.bilibilias.ui.theme.BILIBILIASTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            BILIBILIASTheme {
                TransparentSystemBars()
                val navController = rememberNavController()
                NavHost(
                    navController,
                    startDestination = ROUTE_SPLASH,
                    Modifier
                ) {
                    splashRoute(
                        onNavigateToAuthMethod = navController::navigateToAuthMethod,
                        onNavigateToHome = navController::navigateToHome
                    )
                    // region 登录认证
                    authMethodRoute(onNavigateToLoginAuth = navController::navigateToLoginAuth)
                    loginAuthRoute(onNavigateToHome = navController::navigateToHome)
                    // endregion
                    // todo 放置到另一个文件中
                    composable(ROUTE_HOME) { Screen() }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun Screen() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        Scaffold(Modifier.fillMaxSize(), bottomBar = {
            AsBottomBar(navController)
        }) {
            MainScreen(navController, modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun AsBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar(tonalElevation = 0.dp, containerColor = MaterialTheme.colorScheme.onBackground) {
        items.forEach { screen ->
            val selected = navBackStackEntry?.destination?.isTopLevelDestinationInHierarchy(screen.route) ?: false
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

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: String) =
    this?.hierarchy?.any { it.route == destination } == true

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
            isNavigationBarContrastEnforced = false,
        )
    }
}

private val items = listOf(
    Screen.Home,
    Screen.Tool,
    Screen.Download,
    Screen.User,
)
