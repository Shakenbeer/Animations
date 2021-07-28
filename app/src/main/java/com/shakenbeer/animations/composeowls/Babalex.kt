package com.shakenbeer.animations.composeowls

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue
import kotlin.math.sign

@ExperimentalPagerApi
@Composable
fun Babalex(size: Int) {
    val pagerState = rememberPagerState(
        pageCount = owls.size,
        initialOffscreenLimit = 2
    )

    HorizontalPager(
        state = pagerState,
        itemSpacing = (size / 12).dp,
        modifier = Modifier
            .fillMaxSize()
            .height(size.dp)
    ) { page: Int ->

        val owlImage = owls[page].image

        Box(
            modifier = Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val signedPageOffset = calculateCurrentOffsetForPage(page)
                    val pageOffset = signedPageOffset.absoluteValue

                    // We animate the scaleX + scaleY, between 35% and 100%
                    lerp(
                        start = 0.35f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    translationX = signedPageOffset.sign * (size / 2).dp.toPx() * (1 - scaleX)
                    translationY = (size / 2).dp.toPx() * (1 - scaleY)
                }
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(id = owlImage),
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}