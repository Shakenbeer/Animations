package com.shakenbeer.animations.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shakenbeer.animations.R


val allertaFontFamily = FontFamily(
    Font(R.font.allerta_stencil_regular)
)

val Typography = Typography(
    defaultFontFamily = allertaFontFamily,
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
)