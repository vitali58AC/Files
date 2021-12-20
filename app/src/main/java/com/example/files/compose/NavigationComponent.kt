package com.example.files.compose

import com.example.files.compose.custom_content.CourseDetailScreen
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.files.MainActivityViewModel
import com.example.files.compose.custom_content.CustomContentProviderScreen
import com.example.files.compose.custom_content.UserDetailScreen
import com.example.files.custom_content_provider.CustomContentViewModel
import com.example.files.utils.Constants

@Composable
fun NavigationComponent(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    context: Context,
    customContentViewModel: CustomContentViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Constants.HOME
    ) {
        composable(Constants.HOME) {
            MainScreen(
                viewModel = viewModel,
                onCustomContentClick = { navController.navigate((Constants.COSTUME_PROVIDER)) }
            )
        }
        composable(Constants.COSTUME_PROVIDER) {
            CustomContentProviderCall(navController, context, customContentViewModel)
        }
        composable(
            route = "${Constants.USER_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val userId = entry.arguments?.getLong("id")
            val user = customContentViewModel.getUser(userId)
            UserDetailScreen(user = user)
        }
        composable(
            route = "${Constants.COURSE_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val courseId = entry.arguments?.getLong("id")
            val course = customContentViewModel.getCourse(courseId)
            CourseDetailScreen(course = course)
        }
    }
}

private fun navigateToSingleAccount(
    navController: NavHostController,
    id: Long,
    route: String
) {
    navController.navigate("$route/$id")
}


@Composable
fun CustomContentProviderCall(
    navController: NavHostController,
    context: Context,
    customContentViewModel: CustomContentViewModel
) {
    CustomContentProviderScreen(
        onUserClick = { id ->
            navigateToSingleAccount(
                navController,
                id,
                Constants.USER_DETAIL
            )
        },
        onCourseClick = { id ->
            navigateToSingleAccount(
                navController,
                id,
                Constants.COURSE_DETAIL
            )
        },
        viewModel = customContentViewModel,
        onClickAdd = {
            Toast.makeText(
                context,
                customContentViewModel.addDeleteOperationResult.value,
                Toast.LENGTH_SHORT
            ).show()
        },
        onClickSearch = { id ->
            customContentViewModel.getUserFromId(id,
                { longId ->
                    if (longId != -1L) {
                        navigateToSingleAccount(
                            navController,
                            longId,
                            Constants.USER_DETAIL
                        )
                    }
                },
                {
                    Toast.makeText(
                        context,
                        customContentViewModel.errorWithGetFromId.value,
                        Toast.LENGTH_SHORT
                    ).show()
                })
        },
        onClickDelete = {
            Toast.makeText(
                context,
                customContentViewModel.addDeleteOperationResult.value,
                Toast.LENGTH_SHORT
            ).show()
        },
        onClickClear = {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    Toast.makeText(
                        context,
                        customContentViewModel.addDeleteOperationResult.value,
                        Toast.LENGTH_SHORT
                    ).show()
                }, 0
            )
        },
        onClickUpdate = {
            Toast.makeText(
                context,
                customContentViewModel.addDeleteOperationResult.value,
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}
