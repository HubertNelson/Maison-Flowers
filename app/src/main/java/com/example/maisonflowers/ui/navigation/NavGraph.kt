package com.example.maisonflowers.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.maisonflowers.ui.screens.SplashScreen
import com.example.maisonflowers.ui.screens.WelcomeScreen1
import com.example.maisonflowers.ui.screens.WelcomeScreen2
import com.example.maisonflowers.ui.screens.WelcomeScreen3
import com.example.maisonflowers.ui.screens.WelcomeScreen4
import com.example.maisonflowers.ui.screens.LoginScreen
import com.example.maisonflowers.ui.screens.RegisterScreen
import com.example.maisonflowers.ui.screens.HomeScreen
import com.example.maisonflowers.ui.screens.CategoryScreen
import com.example.maisonflowers.ui.screens.ProductListScreen
import com.example.maisonflowers.ui.screens.AccountScreen
import com.example.maisonflowers.ui.screens.SearchScreen
import com.example.maisonflowers.ui.screens.CartScreen
import com.example.maisonflowers.ui.viewmodels.CartViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maisonflowers.ui.screens.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues // recibe los paddingValues del Scaffold externo
) {
    // Obtener una única instancia de CartViewModel a nivel de NavGraph.
    // Esta instancia persistirá mientras el NavHost esté activo y será compartida.
    // val cartViewModel: CartViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("welcome1") {
            WelcomeScreen1(navController = navController)
        }

        composable("welcome2") {
            WelcomeScreen2(navController = navController)
        }

        composable("welcome3") {
            WelcomeScreen3(navController = navController)
        }

        composable("welcome4") {
            WelcomeScreen4(navController = navController)
        }

        // Ruta para login
        composable("login_screen") {
            LoginScreen(navController = navController)
        }

        // Ruta para register
        composable("register_screen") {
            RegisterScreen(navController = navController)
        }

        composable("home_screen") {
            // Pasar la instancia compartida del ViewModel
            HomeScreen(navController = navController, cartViewModel = cartViewModel, paddingValues = paddingValues)
        }

        composable("category_screen") {
            // Pasar la instancia compartida del ViewModel
            CategoryScreen(navController = navController, cartViewModel = cartViewModel, paddingValues = paddingValues)
        }

        // Ruta para la lista de productos por categoría
        composable(
            route = "product_list_screen/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            ProductListScreen(navController = navController, categoryName = categoryName, cartViewModel = cartViewModel, paddingValues = paddingValues)
        }

        composable("account_screen") {
            AccountScreen(navController = navController, paddingValues = paddingValues)
        }

        composable("search_screen") {
            SearchScreen(navController = navController, cartViewModel = cartViewModel, paddingValues = paddingValues)
        }

        composable("cart_screen") {
            CartScreen(navController = navController, cartViewModel = cartViewModel, paddingValues = paddingValues)
        }

        composable("settings_screen") {
            SettingsScreen(navController = navController, paddingValues = paddingValues)
        }
    }
}
