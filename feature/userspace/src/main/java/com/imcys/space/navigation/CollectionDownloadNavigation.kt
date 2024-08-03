package com.imcys.space.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.imcys.space.CollectionDownloadRoute

const val ROUTE_COLLECTION_DOWNLOAD = "collection_download"
const val itemsBV = "items_bv"
fun NavController.navigateToCollectionDownload(args: String) {
    navigate("$ROUTE_COLLECTION_DOWNLOAD/${args}")
}

fun NavGraphBuilder.collectionDownloadRoute() = composable(
    "$ROUTE_COLLECTION_DOWNLOAD/{$itemsBV}",
    listOf(
        navArgument(itemsBV) {
            type = NavType.StringType
        }
    )
) {
    CollectionDownloadRoute()
}
