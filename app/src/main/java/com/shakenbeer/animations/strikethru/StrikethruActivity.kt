package com.shakenbeer.animations.strikethru

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shakenbeer.animations.theme.AnimationsTheme
import com.shakenbeer.animations.theme.allertaFontFamily

class StrikethruActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Center
                    ) {
                        StrikethruIcon()
                        val annotatedString = buildAnnotatedString {
                            val text = "Styling Android blog post"
                            append(text)
                            addStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colors.secondary,
                                    textDecoration = TextDecoration.Underline,
                                    fontFamily = allertaFontFamily
                                ),
                                start = 0,
                                end = text.length
                            )
                            addStringAnnotation(
                                tag = "URL",
                                annotation = "https://blog.stylingandroid.com/compose-strikethru-animation/",
                                start = 0,
                                end = text.length
                            )
                        }
                        val uriHandler = LocalUriHandler.current
                        ClickableText(
                            modifier = Modifier.align(BottomCenter).padding(16.dp),
                            text = annotatedString,
                            onClick = {
                                annotatedString.getStringAnnotations("URL", it, it)
                                    .firstOrNull()?.let { annotation ->
                                        uriHandler.openUri(annotation.item)
                                    }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun StrikethruIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.Filled.ShoppingCart
) {
    Surface(color = MaterialTheme.colors.background) {
        var enabled by remember { mutableStateOf(false) }
        val transition = updateTransition(enabled, label = "Transition")
        val progress by transition.animateFloat(label = "Progress") { state ->
            if (state) 1f else 0f
        }
        val overlay = StrikethruOverlay(
            color = MaterialTheme.colors.primary,
            widthDp = 4.dp,
            getProgress = { progress }
        )
        Icon(
            modifier = modifier
                .clickable { enabled = !enabled }
                .padding(8.dp)
                .animatedOverlay(overlay)
                .padding(12.dp)
                .size(52.dp),
            imageVector = imageVector,
            tint = MaterialTheme.colors.primary,
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun StrikethruIconPreview() {
    AnimationsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Box {
                StrikethruIcon()
            }
        }
    }
}

interface AnimatedOverlay {
    fun drawOverlay(drawScope: DrawScope)
}

class StrikethruOverlay(
    private val color: Color = Color.Black,
    private var widthDp: Dp = 4.dp,
    private val getProgress: () -> Float
) : AnimatedOverlay {

    @Suppress("MagicNumber")
    override fun drawOverlay(drawScope: DrawScope) {

        with(drawScope) {
            Log.d("NEU_DEBUG", "drawOverlay: ${size.toDpSize()}")
            val width = density.run { widthDp.toPx() }
            val halfWidth = width / 2f
            val progressHeight = size.height * getProgress()
            rotate(-45f) {
                drawLine(
                    color = color,
                    start = Offset(size.center.x + halfWidth, 0f),
                    end = Offset(size.center.x + halfWidth, progressHeight),
                    strokeWidth = width,
                    blendMode = BlendMode.Clear
                )
                drawLine(
                    color = color,
                    start = Offset(size.center.x - halfWidth, 0f),
                    end = Offset(size.center.x - halfWidth, progressHeight),
                    strokeWidth = width
                )
            }
        }
    }
}

@Suppress("MagicNumber")
fun Modifier.animatedOverlay(animatedOverlay: AnimatedOverlay) = this.then(
    Modifier
        .graphicsLayer {
            // This is required to render to an offscreen buffer
            // The Clear blend mode will not work without it
            alpha = 0.99f
        }
        .drawWithContent {
            drawContent()
            animatedOverlay.drawOverlay(this)
        }
)