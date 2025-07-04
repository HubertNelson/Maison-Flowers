package com.example.maisonflowers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.maisonflowers.ui.navigation.NavGraph
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme
import com.example.maisonflowers.ui.viewmodels.CartViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.isSystemInDarkTheme // Importar para isSystemInDarkTheme
import androidx.compose.runtime.collectAsState // Importar para collectAsState
import androidx.compose.ui.platform.LocalContext // Importar para LocalContext
import com.example.maisonflowers.data.ThemeManager // Importar ThemeManager
import com.example.maisonflowers.data.ThemeMode // Importar ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Obtener el ThemeManager y el modo de tema actual
            val context = LocalContext.current
            val themeManager = remember { ThemeManager(context) }
            val themeMode by themeManager.themeMode.collectAsState(initial = ThemeMode.SYSTEM) // Observa el modo de tema

            // Determinar si el tema oscuro debe estar activo
            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme() // Usa la configuración del sistema
                ThemeMode.LIGHT -> false // Fuerza el tema claro
                ThemeMode.DARK -> true // Fuerza el tema oscuro
            }

            MaisonFlowersTheme(darkTheme = darkTheme) { // Pasar el valor de darkTheme al tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MaisonFlowersApp(themeManager = themeManager) // Pasar themeManager a MaisonFlowersApp
                }
            }
        }
    }
}

@Composable
fun MaisonFlowersApp(themeManager: ThemeManager) { // Recibe themeManager
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        "home_screen",
        "category_screen",
        "search_screen",
        "cart_screen",
        "account_screen"
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    val navItems = listOf(
                        "home_screen" to Pair(Icons.Filled.Home, "Home"),
                        "category_screen" to Pair(Icons.Filled.Apps, "Categories"),
                        "search_screen" to Pair(Icons.Filled.Search, "Search"),
                        "cart_screen" to Pair(Icons.Filled.ShoppingCart, "Cart"),
                        "account_screen" to Pair(Icons.Filled.Person, "Account")
                    )

                    navItems.forEachIndexed { index, (route, iconLabel) ->
                        val isSelected = currentRoute?.startsWith(route) == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(iconLabel.first, contentDescription = iconLabel.second) },
                            label = { Text(iconLabel.second) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                                unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            cartViewModel = cartViewModel,
            paddingValues = paddingValues,
            themeManager = themeManager // Pasar themeManager al NavGraph
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaisonFlowersTheme {
        // Para el preview, necesitamos una instancia simulada de ThemeManager
        val context = LocalContext.current
        val themeManager = remember { ThemeManager(context) }
        MaisonFlowersApp(themeManager = themeManager)
    }
}
