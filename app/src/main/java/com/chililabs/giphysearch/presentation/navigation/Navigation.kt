package com.chililabs.giphysearch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chililabs.giphysearch.domain.model.Gif
import com.chililabs.giphysearch.presentation.detail.DetailScreen
import com.chililabs.giphysearch.presentation.search.SearchScreen

sealed class Screen(val route: String) {
    object Search : Screen("search")
    object Detail : Screen("detail/{gifId}/{title}/{username}/{rating}/{previewUrl}/{originalUrl}/{width}/{height}/{importDatetime}") {
        fun createRoute(gif: Gif): String {
            return "detail/${gif.id}/${gif.title}/${gif.username}/${gif.rating}/${encodeUrl(gif.previewUrl)}/${encodeUrl(gif.originalUrl)}/${gif.width}/${gif.height}/${gif.importDatetime}"
        }

        private fun encodeUrl(url: String): String {
            return url.replace("/", "SLASH")
                .replace(":", "COLON")
                .replace("?", "QUESTION")
                .replace("&", "AND")
                .replace("=", "EQUALS")
        }
    }
}

fun decodeUrl(encoded: String): String {
    return encoded.replace("SLASH", "/")
        .replace("COLON", ":")
        .replace("QUESTION", "?")
        .replace("AND", "&")
        .replace("EQUALS", "=")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        composable(Screen.Search.route) {
            SearchScreen(
                onGifClick = { gif ->
                    navController.navigate(Screen.Detail.createRoute(gif))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("gifId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("username") { type = NavType.StringType },
                navArgument("rating") { type = NavType.StringType },
                navArgument("previewUrl") { type = NavType.StringType },
                navArgument("originalUrl") { type = NavType.StringType },
                navArgument("width") { type = NavType.IntType },
                navArgument("height") { type = NavType.IntType },
                navArgument("importDatetime") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gif = Gif(
                id = backStackEntry.arguments?.getString("gifId") ?: "",
                title = backStackEntry.arguments?.getString("title") ?: "",
                username = backStackEntry.arguments?.getString("username") ?: "",
                rating = backStackEntry.arguments?.getString("rating") ?: "",
                previewUrl = decodeUrl(backStackEntry.arguments?.getString("previewUrl") ?: ""),
                originalUrl = decodeUrl(backStackEntry.arguments?.getString("originalUrl") ?: ""),
                width = backStackEntry.arguments?.getInt("width") ?: 0,
                height = backStackEntry.arguments?.getInt("height") ?: 0,
                importDatetime = backStackEntry.arguments?.getString("importDatetime") ?: ""
            )

            DetailScreen(
                gif = gif,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}