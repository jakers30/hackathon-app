package com.raite.studyroom.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * 60-30-10 palette (spec section 5.3).
 *
 *   60% Neutral  -> backgrounds, cards, body text
 *   30% Teal     -> app bars, navigation, headers, chips
 *   10% Orange   -> primary call-to-action ONLY
 *
 * The Material 3 [androidx.compose.material3.ColorScheme] maps these as:
 *   primary            = teal   (default buttons, app bars, FAB base)
 *   secondaryContainer = teal tint (chips, selected rows)
 *   tertiary           = orange (use ONLY for the CTA buttons: Generate,
 *                                 Create Room, Publish Quiz)
 */

// ---- Neutral (60%) ----------------------------------------------------------
val NeutralLight = Color(0xFFF6F8F8)
val CardLight = Color(0xFFFFFFFF)
val TextLight = Color(0xFF10201F)
val NeutralDark = Color(0xFF0E1415)
val CardDark = Color(0xFF151D1F)
val TextDark = Color(0xFFE4ECEB)

// ---- Teal (30%) -------------------------------------------------------------
val Teal = Color(0xFF0F766E)
val TealTint = Color(0xFFD9EEEB)
val TealDeep = Color(0xFF0B5E57)
val TealDark = Color(0xFF5EEAD4)
val TealContainerDark = Color(0xFF123A38)

// ---- Orange (10%) -----------------------------------------------------------
val Orange = Color(0xFFC2410C)
val OrangeContainerLight = Color(0xFFFFDBC8)
val OrangeDark = Color(0xFFFDBA74)
val OrangeContainerDark = Color(0xFF7A3A12)
val OrangeOnDark = Color(0xFF3B1500)

// ---- Variants / outline -----------------------------------------------------
val SurfaceVariantLight = Color(0xFFDDE4E3)
val OnSurfaceVariantLight = Color(0xFF414B49)
val OutlineLight = Color(0xFF707976)
val SurfaceVariantDark = Color(0xFF3F4947)
val OnSurfaceVariantDark = Color(0xFFBFC9C6)
val OutlineDark = Color(0xFF899390)

// ---- Error ------------------------------------------------------------------
val ErrorLight = Color(0xFFB3261E)
val ErrorDark = Color(0xFFF2B8B5)
val OnErrorDark = Color(0xFF601410)
