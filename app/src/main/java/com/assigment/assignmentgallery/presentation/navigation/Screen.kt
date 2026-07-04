package com.assigment.assignmentgallery.presentation.navigation


sealed class Screen(val route: String) {

    data object PhotoList : Screen("photo_list")

    data object PhotoDetail : Screen("photo_detail/{photoId}") {

        fun createRoute(photoId: Int): String {
            return "photo_detail/$photoId"
        }
    }

    data object CreatePost : Screen("create_post")

}