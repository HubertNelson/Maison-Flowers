package com.example.maisonflowers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.maisonflowers.ui.screens.AccountScreen
import com.example.maisonflowers.ui.screens.CategoryScreen
import com.example.maisonflowers.ui.screens.HomeScreen
import com.example.maisonflowers.ui.screens.LoginScreen
import com.example.maisonflowers.ui.screens.ProductListScreen
import com.example.maisonflowers.ui.screens.RegisterScreen
import com.example.maisonflowers.ui.screens.SplashScreen // Importa SplashScreen
import com.example.maisonflowers.ui.screens.WelcomeScreen1 // Importa WelcomeScreen (la que te generé antes)
import com.example.maisonflowers.ui.screens.WelcomeScreen2
import com.example.maisonflowers.ui.screens.WelcomeScreen3
import com.example.maisonflowers.ui.screens.WelcomeScreen4

@Composable
fun NavGraph(navController: NavHostController) {
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
            HomeScreen(navController = navController)
        }

        composable("category_screen") {
            CategoryScreen(navController = navController)
        }

        // Ruta para la lista de productos por categoría
        composable(
            route = "product_list_screen/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            ProductListScreen(navController = navController, categoryName = categoryName)
        }

        composable("account_screen") {
            AccountScreen(navController = navController)
        }
    }
}
