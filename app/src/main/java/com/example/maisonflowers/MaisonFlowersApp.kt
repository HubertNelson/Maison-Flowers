package com.example.maisonflowers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.maisonflowers.ui.navigation.NavGraph
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme
import com.example.maisonflowers.ui.viewmodels.CartViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.LocalImageLoader
import coil.decode.SvgDecoder
import com.example.maisonflowers.data.ThemeManager
import com.example.maisonflowers.data.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Obtener el contexto dentro del Composable
            val context = LocalContext.current

            val imageLoader = remember { // Usar remember para que se cree una sola vez
                ImageLoader.Builder(context)
                    .components {
                        add(SvgDecoder.Factory())
                    }
                    .build()
            }

            // Proporcionar el ImageLoader personalizado a la jerarquía de Composables
            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                val themeManager = remember { ThemeManager(applicationContext) }
                val currentTheme by themeManager.themeMode.collectAsState(initial = ThemeMode.SYSTEM)

                MaisonFlowersTheme(darkTheme = when (currentTheme) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
                }) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MaisonFlowersApp(themeManager = themeManager)
                    }
                }
            }
        }
    }
}

enum class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    Home("home_screen", Icons.Default.Home, "Inicio"),
    Category("category_screen", Icons.Default.Apps, "Categorías"),
    Search("search_screen", Icons.Default.Search, "Buscar"),
    Cart("cart_screen", Icons.Default.ShoppingCart, "Carrito"),
    Account("account_screen", Icons.Default.Person, "Cuenta")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaisonFlowersApp(themeManager: ThemeManager) {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val bottomBarRoutes = listOf(
                BottomNavItem.Home.route,
                BottomNavItem.Category.route,
                BottomNavItem.Search.route,
                BottomNavItem.Cart.route,
                BottomNavItem.Account.route
            )

            if (currentDestination?.route in bottomBarRoutes ||
                currentDestination?.hierarchy?.any { it.route in bottomBarRoutes } == true) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.height(80.dp)
                ) {
                    val items = listOf(
                        BottomNavItem.Home,
                        BottomNavItem.Category,
                        BottomNavItem.Search,
                        BottomNavItem.Cart,
                        BottomNavItem.Account
                    )
                    items.forEach { screen ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = {
                                if (screen == BottomNavItem.Cart) {
                                    val cartItemCount = cartViewModel.cartItems.sumOf { it.quantity }
                                    BadgedBox(
                                        badge = {
                                            if (cartItemCount > 0) {
                                                Badge { Text(cartItemCount.toString()) }
                                            }
                                        }
                                    ) {
                                        Icon(screen.icon, contentDescription = screen.label)
                                    }
                                } else {
                                    Icon(screen.icon, contentDescription = screen.label)
                                }
                            },
                            label = { Text(screen.label) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
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
            themeManager = themeManager
        )
    }
}
