package com.habittracker.rootreflect.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.habittracker.rootreflect.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val Plusjakartasans = FontFamily(
    Font(R.font.plusjakartasansextrabold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.plusjakartasansextrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),

    Font(R.font.plusjakartasansbold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.plusjakartasansbolditalic, FontWeight.Bold, FontStyle.Italic),

    Font(R.font.plusjakartasanssemibold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.plusjakartasanssemibolditalic, FontWeight.SemiBold, FontStyle.Italic),

    Font(R.font.plusjakartasansmedium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.plusjakartasansmediumitalic, FontWeight.Medium, FontStyle.Italic),

    Font(R.font.plusjakartasansregular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.plusjakartasansitalic, FontWeight.Normal, FontStyle.Italic),

    Font(R.font.plusjakartasanslight, FontWeight.Light, FontStyle.Normal),
    Font(R.font.plusjakartasanslightitalic, FontWeight.Light, FontStyle.Italic),

    Font(R.font.plusjakartasansextralight, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.plusjakartasansextralightitalic, FontWeight.ExtraLight, FontStyle.Italic)
)

val CustomTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Plusjakartasans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)