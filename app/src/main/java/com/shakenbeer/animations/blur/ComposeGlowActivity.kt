package com.shakenbeer.animations.blur

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shakenbeer.animations.theme.AnimationsTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.graphics.createBitmap
import com.shakenbeer.animations.R


class ComposeGlowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationsTheme {
                GlowingBackground()
            }
        }
    }
}

private val GlowEasing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)

// Teal state (Default / Variant3)
private val TealCenter = Color(0xFF0A2334)
private val TealEdge = Color(0xFF0A3433)
// Purple state (Variant2)
private val PurpleCenter = Color(0xFF473253)
private val PurpleEdge = Color(0xFF3A2049)

// Falloff: RGB lerps center→edge, alpha follows smoother step down to 0
private fun glowStops(
    centerColor: Color,
    edgeColor: Color,
    count: Int = 48
): Array<Pair<Float, Color>> =
    Array(count) { i ->
        val t = i / (count - 1f)
        val falloff = 1f - (t * t * t * (t * (t * 6f - 15f) + 10f))
        t to lerp(centerColor, edgeColor, t).copy(alpha = falloff)
    }

private fun noiseBitmap(size: Int = 128): ImageBitmap {
    val bmp = createBitmap(size, size, Bitmap.Config.ALPHA_8)
    val rnd = java.util.Random(42)
    val pixels = ByteArray(size * size) { (rnd.nextInt(3)).toByte() } // 0..2 alpha
    bmp.copyPixelsFromBuffer(java.nio.ByteBuffer.wrap(pixels))
    return bmp.asImageBitmap()
}

/**
 * Loads a noise texture whose signal lives in the alpha channel
 * (white pixels with varying alpha, like a Figma noise-layer export)
 * into an ALPHA_8 bitmap suitable for tiling via ImageShader.
 */
fun loadNoiseAlpha8(res: Resources): ImageBitmap {
    val opts = BitmapFactory.Options().apply {
        inScaled = false // no density scaling — keep grains at 1:1 px
    }
    val src = BitmapFactory.decodeResource(res, R.drawable.noise, opts)
    val alpha8 = src.extractAlpha() // ALPHA_8 bitmap from the alpha channel
    src.recycle()
    return alpha8.asImageBitmap()
}

@Preview
@Composable
private fun GlowingBackground() {
    val backgroundColor = Color(0xFF09181E)

    val heightDp = remember { Animatable(570f) }
    val offsetYDp = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }          // invisible at first
    val colorProgress = remember { Animatable(0f) }  // 0 = teal, 1 = purple
    val context = LocalContext.current

    // Built once; tiny (64 KB)
    val noiseBrush = remember {
        ShaderBrush(
            ImageShader(
                noiseBitmap(),
                TileMode.Repeated,
                TileMode.Repeated
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(1000)

        // Entry — 800 ms: fade in + contract + rise + teal→purple
        val entrySpec = tween<Float>(800, easing = GlowEasing)
        coroutineScope {
            launch { alpha.animateTo(1f, entrySpec) }
            launch { heightDp.animateTo(509f, entrySpec) }
            launch { offsetYDp.animateTo(-43f, entrySpec) }
            launch { colorProgress.animateTo(1f, entrySpec) }
        }

        // Loop: 800 ms pause before each 2000 ms transition
        val loopSpec = tween<Float>(2000, easing = GlowEasing)
        var expanded = false
        while (true) {
            delay(800)
            expanded = !expanded
            coroutineScope {
                if (expanded) {
                    // Variant2 → Variant3: expand + sink + dim + purple→teal
                    launch { heightDp.animateTo(570f, loopSpec) }
                    launch { offsetYDp.animateTo(0f, loopSpec) }
                    launch { alpha.animateTo(0.7f, loopSpec) }
                    launch { colorProgress.animateTo(0f, loopSpec) }
                } else {
                    // Variant3 → Variant2: contract + rise + brighten + teal→purple
                    launch { heightDp.animateTo(509f, loopSpec) }
                    launch { offsetYDp.animateTo(-43f, loopSpec) }
                    launch { alpha.animateTo(1f, loopSpec) }
                    launch { colorProgress.animateTo(1f, loopSpec) }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 480.dp, height = 570.dp)
                .graphicsLayer { this.alpha = alpha.value }
                .drawBehind {
                    val p = colorProgress.value
                    val stops = glowStops(
                        centerColor = lerp(TealCenter, PurpleCenter, p),
                        edgeColor = lerp(TealEdge, PurpleEdge, p)
                    )
                    val w = size.width
                    val h = heightDp.value.dp.toPx()
                    val center = Offset(
                        x = w / 2f,
                        y = h / 2f + offsetYDp.value.dp.toPx()
                    )
                    withTransform({
                        scale(scaleX = 1f, scaleY = h / w, pivot = center)
                    }) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                *stops,
                                center = center,
                                radius = w / 2f
                            ),
                            radius = w / 2f,
                            center = center
                        )
                    }
                    drawRect(brush = noiseBrush)
                }
        )
    }
}

@Preview
@Composable
private fun GlowingBackgroundFirst() {
    val backgroundColor = Color(0xFF09181E)
    val glowColor = Color.Green //Color(0xFF0A3433)

    val heightDp = remember { Animatable(570f) }
    val offsetYDp = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) } // invisible at first

    LaunchedEffect(Unit) {
        // 1 second before anything happens
        delay(1000)

        // Entry — 800 ms: fade in + contract + rise
        val entrySpec = tween<Float>(800, easing = GlowEasing)
        coroutineScope {
            launch { alpha.animateTo(1f, entrySpec) }
            launch { heightDp.animateTo(509f, entrySpec) }
            launch { offsetYDp.animateTo(-43f, entrySpec) }
        }

        // Breathing loop: each 2000 ms transition preceded by an 800 ms pause
        val loopSpec = tween<Float>(2000, easing = GlowEasing)
        var expanded = false
        while (true) {
            delay(800)
            expanded = !expanded
            coroutineScope {
                if (expanded) {
                    // Variant2 → Variant3: expand + sink + dim
                    launch { heightDp.animateTo(570f, loopSpec) }
                    launch { offsetYDp.animateTo(0f, loopSpec) }
                    launch { alpha.animateTo(0.7f, loopSpec) }
                } else {
                    // Variant3 → Variant2: contract + rise + brighten
                    launch { heightDp.animateTo(509f, loopSpec) }
                    launch { offsetYDp.animateTo(-43f, loopSpec) }
                    launch { alpha.animateTo(1f, loopSpec) }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 480.dp, height = 570.dp)
                .drawBehind {
                    val w = size.width
                    val h = heightDp.value.dp.toPx()
                    val center = Offset(
                        x = w / 2f,
                        y = h / 2f + offsetYDp.value.dp.toPx()
                    )
                    withTransform({
                        scale(scaleX = 1f, scaleY = h / w, pivot = center)
                    }) {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colorStops = glowStops(glowColor, alpha.value),
                                center = center,
                                radius = w / 2f
                            ),
                            radius = w / 2f,
                            center = center
                        )
                    }
                }
        )
    }
}

// Precomputed once: 24 stops following a smoother step falloff.
// Smooth derivative everywhere => no visible "edges" between segments.
private fun glowStops(color: Color, peakAlpha: Float): Array<Pair<Float, Color>> =
    Array(24) { i ->
        val t = i / 23f
        // smoother step, inverted: 1 at center, 0 at edge, zero slope at both ends
        val falloff = 1f - (t * t * t * (t * (t * 6f - 15f) + 10f))
        t to color.copy(alpha = peakAlpha * falloff)
    }

@Composable
private fun GlowingBackgroundFirstTry() {
    val backgroundColor = Color(0xFF09181E)
    val glowColor = Color(0xFF0A3433)

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(500.dp) // full glow diameter, including falloff
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = glowAlpha),
                                glowColor.copy(alpha = 0f)
                            )
                        )
                    )
                }
        )
    }
}

@Composable
private fun ComposeBlur() {
    val glowColor = Color(0xFF0A3433)
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF09181E)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(200.dp)
                    .graphicsLayer { alpha = glowAlpha }
                    .background(glowColor, RoundedCornerShape(100.dp))
            )
        }
    }
}

@Composable
private fun ComposeBlurViaColor() {
    val glowColor = Color(0xFF0A3433)

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF09181E)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(150.dp),
            contentAlignment = Alignment.Center

        ) {
            Box(
                Modifier
                    .size(200.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = glowColor.copy(alpha = glowAlpha),
                            cornerRadius = CornerRadius(100.dp.toPx())
                        )
                    }
            )
        }
    }
}