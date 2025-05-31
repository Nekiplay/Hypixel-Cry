package com.nekiplay.hypixelcry.utils

import java.awt.Color

object SpecialColor {
    private const val MIN_CHROMA_SECS = 1
    private const val MAX_CHROMA_SECS = 60
    private val startTime = SimpleTimeMark.now()

    @JvmStatic
    fun String.toSpecialColor() = Color(toSpecialColorInt(), true)

    @JvmStatic
    fun String.toSpecialColorInt(): Int {
        val (chroma, alpha, red, green, blue) = decompose(this)
        val (hue, sat, bri) = Color.RGBtoHSB(red, green, blue, null)

        val adjustedHue = if (chroma > 0) (hue + (startTime.passedSince().inWholeMilliseconds / 1000f / chromaSpeed(chroma) % 1)).let {
            if (it < 0) it + 1f else it
        } else hue

        return (alpha and 0xFF) shl 24 or (Color.HSBtoRGB(adjustedHue, sat, bri) and 0x00FFFFFF)
    }

    private fun decompose(csv: String) = csv.split(":").mapNotNull { it.toIntOrNull() }.toIntArray()
    private fun chromaSpeed(speed: Int) = (255 - speed) / 254f * (MAX_CHROMA_SECS - MIN_CHROMA_SECS) + MIN_CHROMA_SECS
}