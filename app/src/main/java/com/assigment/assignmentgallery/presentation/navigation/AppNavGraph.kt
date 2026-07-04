package com.assigment.assignmentgallery.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.assigment.assignmentgallery.presentation.create_post.CreatePostScreen
import com.assigment.assignmentgallery.presentation.photo_detail.PhotoDetailScreen
import com.assigment.assignmentgallery.presentation.photo_list.PhotoListScreen

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PhotoList.route
    ) {

        composable(
            route = Screen.PhotoList.route
        ) {
            PhotoListScreen(

                onPhotoClick = { photoId ->

                    navController.navigate(
                        Screen.PhotoDetail.createRoute(photoId)
                    )

                },

                onCreatePostClick = {

                    navController.navigate(
                        Screen.CreatePost.route
                    )

                },

                onUploadImageClick = {

                    // Not required now.
                    // Leave empty.

                }

            )
        }

        composable(
            route = Screen.PhotoDetail.route,
            arguments = listOf(
                navArgument("photoId") {
                    type = NavType.IntType
                }
            )
        ) {

            PhotoDetailScreen(

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.CreatePost.route
        ) {

            CreatePostScreen( onCloseClick = {

                navController.popBackStack()

            })
        }


    }
}